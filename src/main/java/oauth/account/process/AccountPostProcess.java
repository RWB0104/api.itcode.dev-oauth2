package oauth.account.process;

import com.github.scribejava.core.model.OAuth2AccessToken;
import global.bean.ResponseBean;
import global.module.Process;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import oauth.account.module.AuthModule;

import javax.management.BadAttributeValueExpException;
import java.util.Objects;

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
		
		ResponseBean<String> responseBean = new ResponseBean<>();
		
		HttpSession session = request.getSession();
		
		// 로그인 응답 생성 시도
		try
		{
			Object sessionState = Objects.requireNonNull(session.getAttribute("state"));
			
			// 고유 상태값이 일치하지 않을 경우
			if (!state.equals(sessionState))
			{
				throw new BadAttributeValueExpException("state is mismatched");
			}
			
			AuthModule authModule = getAuthModule(platform);
			
			OAuth2AccessToken oAuth2AccessToken = authModule.getAccessToken(code);
			
			String accessToken = oAuth2AccessToken.getAccessToken();
			String refreshToken = oAuth2AccessToken.getRefreshToken();
			
			NewCookie accessCookie = new NewCookie("access", accessToken, "/oauth2", ".itcode.dev", "access token", -1, true, true);
			NewCookie refreshCookie = new NewCookie("refresh", refreshToken, "/oauth2", ".itcode.dev", "refresh token", 86400 * 7 + 3600 * 9, true, true);
			NewCookie platformCookie = new NewCookie("platform", platform, "/oauth2", ".itcode.dev", "platform token", 86400 * 7 + 3600 * 9, true, true);
			
			responseBean.setFlag(true);
			responseBean.setTitle("success");
			responseBean.setMessage("authorized success");
			responseBean.setBody(null);
			
			response = Response.ok(responseBean, MediaType.APPLICATION_JSON).cookie(accessCookie, refreshCookie, platformCookie).build();
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
	 * 로그아웃 응답 반환 메서드
	 *
	 * @return [Response] 응답 객체
	 */
	public Response postLogoutResponse()
	{
		Response response;
		
		ResponseBean<String> responseBean = new ResponseBean<>();
		
		// 로그아웃 응답 생성 시도
		try
		{
			NewCookie accessCookie = new NewCookie("access", null, "/oauth2", ".itcode.dev", "access token", 0, true, true);
			NewCookie refreshCookie = new NewCookie("refresh", null, "/oauth2", ".itcode.dev", "refresh token", 0, true, true);
			NewCookie platformCookie = new NewCookie("platform", null, "/oauth2", ".itcode.dev", "platform token", 0, true, true);
			
			responseBean.setFlag(true);
			responseBean.setTitle("success");
			responseBean.setMessage("logout success");
			responseBean.setBody(null);
			
			response = Response.ok(responseBean, MediaType.APPLICATION_JSON).cookie(accessCookie, refreshCookie, platformCookie).build();
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