package oauth.account.process;

import global.bean.ResponseBean;
import global.module.JwtModule;
import global.module.Process;
import global.module.Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import oauth.account.module.AuthModule;

import java.util.UUID;

/**
 * 계정 PUT 프로세스 클래스
 *
 * @author RWB
 * @since 2021.10.19 Tue 21:56:32
 */
public class AccountPutProcess extends Process
{
	/**
	 * 생성자 메서드
	 *
	 * @param request: [HttpServletRequest] HttpServletRequest 객체
	 * @param response: [HttpServletResponse] HttpServletResponse 객체
	 */
	public AccountPutProcess(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
	}
	
	/**
	 * 정보 제공 동의 갱신 URL 응답 반환 메서드
	 *
	 * @param accessCookie: [String] 접근 토큰 쿠키
	 *
	 * @return [Response] 응답 객체
	 */
	public Response putUpdateAuthorizationUrl(String accessCookie)
	{
		Response response;
		
		ResponseBean<String> responseBean = new ResponseBean<>();
		
		// 정보 제공 동의 갱신 URL 응답 생성 시도
		try
		{
			String state = UUID.randomUUID().toString();
			
			Jws<Claims> jws = JwtModule.openJwt(accessCookie);
			
			String platform = jws.getBody().get("platform", String.class);
			
			AuthModule authModule = getAuthModule(platform);
			
			String url = authModule.getUpdateAuthorizationUrl(state);
			
			// URL이 null일 경우
			if (url == null)
			{
				responseBean.setFlag(false);
				responseBean.setTitle("skipped");
				responseBean.setMessage(Util.builder(platform, " doesn't need that service"));
				responseBean.setBody(null);
			}
			
			// URL이 유효할 경우
			else
			{
				request.getSession().setAttribute("state", state);
				
				responseBean.setFlag(true);
				responseBean.setTitle("success");
				responseBean.setMessage(Util.builder(platform, " reauthrorization url response success"));
				responseBean.setBody(url);
			}
			
			response = Response.ok(responseBean, MediaType.APPLICATION_JSON).build();
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