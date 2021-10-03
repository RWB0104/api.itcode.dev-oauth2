package oauth.account.process;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.gson.JsonObject;
import global.bean.ResponseBean;
import global.module.Process;
import oauth.account.module.AuthModule;

import javax.management.BadAttributeValueExpException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;

/**
 * 계정 GET 프로세스 클래스
 *
 * @author RWB
 * @since 2021.10.02 Sat 00:53:52
 */
public class AccountPostProcess extends Process
{
	/**
	 * 생성자 메서드
	 *
	 * @param request: [HttpServletRequest] HttpServletRequest 객체
	 * @param response: [HttpServletResponse] HttpServletResponse 객체
	 */
	public AccountPostProcess(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
	}
	
	/**
	 * 로그인 응답 반환 메서드
	 *
	 * @param platform: [String] 플랫폼
	 * @param code: [String] 인증 코드
	 * @param state: [String] 고유 상태값
	 *
	 * @return [Response] 응답 객체
	 */
	public Response postLoginResponse(String platform, String code, String state)
	{
		Response response;
		
		ResponseBean<JsonObject> responseBean = new ResponseBean<>();
		
		HttpSession session = request.getSession();
		
		// 로그인 응답 생성 시도
		try
		{
			String sessionState = session.getAttribute("state").toString();
			
			// 고유 상태값이 일치하지 않을 경우
			if (!state.equals(sessionState))
			{
				throw new BadAttributeValueExpException("state is mismatched");
			}
			
			AuthModule authModule = getAuthModule(platform);
			
			OAuth2AccessToken oAuth2AccessToken = authModule.getAccessToken(code);
			
			String accessToken = oAuth2AccessToken.getAccessToken();
			String refreshToken = oAuth2AccessToken.getRefreshToken();
			
			// 접근 토큰 쿠키
			Cookie accessCookie = new Cookie("access", accessToken);
			accessCookie.setHttpOnly(true);
			
			// 갱신 토큰 쿠키
			Cookie refreshCookie = new Cookie("refresh", refreshToken);
			refreshCookie.setHttpOnly(true);
			refreshCookie.setMaxAge(3600 * 24 * 7);
			
			this.response.addCookie(accessCookie);
			this.response.addCookie(refreshCookie);
			
			response = Response.temporaryRedirect(URI.create("https://project.itcode.dev/oauth2/home")).build();
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
		
		// 시도 후
		finally
		{
			session.invalidate();
		}
		
		return response;
	}
	
	/**
	 * 사용자 정보 응답 반환 메서드
	 *
	 * @param platform: [String] 플랫폼
	 * @param access: [String] 접근 토큰
	 *
	 * @return [Response] 응답 객체
	 */
	public Response postUserInfoResponse(String platform, String access)
	{
		Response response;
		
		ResponseBean<JsonObject> responseBean = new ResponseBean<>();
		
		// 사용자 정보 응답 생성 시도
		try
		{
			AuthModule authModule = getAuthModule(platform);
			
			com.github.scribejava.core.model.Response userInfoResponse = authModule.getUserInfo(access);
			
			System.out.println(userInfoResponse.getMessage());
			
			responseBean.setFlag(true);
			responseBean.setTitle("success");
			responseBean.setMessage("user info response success");
			responseBean.setBody(null);
			
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