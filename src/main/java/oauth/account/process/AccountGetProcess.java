package oauth.account.process;

import com.github.scribejava.core.model.OAuthResponseException;
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
import oauth.account.bean.UserInfoBean;
import oauth.account.module.AuthModule;

import java.util.UUID;

/**
 * 계정 GET 프로세스 클래스
 *
 * @author RWB
 * @since 2021.09.30 Thu 21:00:48
 */
public class AccountGetProcess extends Process
{
	/**
	 * 생성자 메서드
	 *
	 * @param request: [HttpServletRequest] HttpServletRequest 객체
	 * @param response: [HttpServletResponse] HttpServletResponse 객체
	 */
	public AccountGetProcess(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
	}
	
	/**
	 * 인증 URL 응답 반환 메서드
	 *
	 * @param platform: [String] 플랫폼
	 *
	 * @return [Response] 응답 객체
	 */
	public Response getAuthorizationUrlResponse(String platform)
	{
		Response response;
		
		ResponseBean<String> responseBean = new ResponseBean<>();
		
		String state = UUID.randomUUID().toString();
		
		// 인증 URL 응답 생성 시도
		try
		{
			request.getSession().setAttribute("state", state);
			
			AuthModule authModule = getAuthModule(platform);
			
			responseBean.setFlag(true);
			responseBean.setTitle("success");
			responseBean.setMessage(Util.builder(platform, " authrorization url response success"));
			responseBean.setBody(authModule.getAuthorizationUrl(state));
			
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
	
	/**
	 * 사용자 정보 응답 반환 메서드
	 *
	 * @param accessCookie: [String] 접근 토큰 쿠키
	 *
	 * @return [Response] 응답 객체
	 */
	public Response getUserInfoResponse(String accessCookie)
	{
		Response response;
		
		ResponseBean<UserInfoBean> responseBean = new ResponseBean<>();
		
		// 사용자 정보 응답 생성 시도
		try
		{
			Jws<Claims> jws = JwtModule.openJwt(accessCookie);
			
			String accessToken = jws.getBody().get("access", String.class);
			String platform = jws.getBody().get("platform", String.class);
			
			AuthModule authModule = getAuthModule(platform);
			
			com.github.scribejava.core.model.Response userInfoResponse = authModule.getUserInfo(accessToken);
			
			// 응답이 정상적이지 않을 경우
			if (userInfoResponse.getCode() != 200)
			{
				throw new OAuthResponseException(userInfoResponse);
			}
			
			responseBean.setFlag(true);
			responseBean.setTitle("success");
			responseBean.setMessage("user info response success");
			responseBean.setBody(authModule.getUserInfoBean(userInfoResponse.getBody()));
			
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