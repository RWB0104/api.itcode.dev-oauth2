package oauth.account.process;

import global.bean.ResponseBean;
import global.module.JwtModule;
import global.module.Process;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import oauth.account.module.AuthModule;
import org.glassfish.jersey.client.authentication.RequestAuthenticationException;

/**
 * 계정 DELETE 프로세스 클래스
 *
 * @author RWB
 * @since 2021.10.02 Sat 00:53:52
 */
public class AccountDeleteProcess extends Process
{
	/**
	 * 생성자 메서드
	 *
	 * @param request: [HttpServletRequest] HttpServletRequest 객체
	 * @param response: [HttpServletResponse] HttpServletResponse 객체
	 */
	public AccountDeleteProcess(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
	}
	
	/**
	 * 연동 해제 응답 반환 메서드
	 *
	 * @param accessCookie: [String] 접근 토큰 쿠키
	 *
	 * @return [Response] 응답 객체
	 */
	public Response deleteInfoResponse(String accessCookie)
	{
		Response response;
		
		ResponseBean<String> responseBean = new ResponseBean<>();
		
		// 로그아웃 응답 생성 시도
		try
		{
			Jws<Claims> jws = JwtModule.openJwt(accessCookie);
			
			String accessToken = jws.getBody().get("access", String.class);
			String platform = jws.getBody().get("platform", String.class);
			
			AuthModule authModule = getAuthModule(platform);
			
			if (authModule.deleteInfo(accessToken))
			{
				response = new AccountPostProcess(request, this.response).postLogoutResponse();
			}
			
			else
			{
				throw new RequestAuthenticationException("revoke fail");
			}
		}
		
		// 예외
		catch (Exception e)
		{
			e.printStackTrace();
			
			responseBean.setFlag(false);
			responseBean.setTitle(e.getClass().getSimpleName());
			responseBean.setMessage(e.getMessage());
			responseBean.setBody(null);
			
			response = Response.status(Response.Status.BAD_REQUEST).entity(responseBean).type(MediaType.APPLICATION_JSON).build();
		}
		
		return response;
	}
}