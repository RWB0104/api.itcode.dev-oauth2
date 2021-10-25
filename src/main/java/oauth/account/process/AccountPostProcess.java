package oauth.account.process;

import com.github.scribejava.core.model.OAuth2AccessToken;
import global.bean.ResponseBean;
import global.module.JwtModule;
import global.module.Process;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import oauth.account.module.AuthModule;

import javax.management.BadAttributeValueExpException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * 계정 POST 프로세스 클래스
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
			
			HashMap<String, Object> accessMap = new HashMap<>();
			accessMap.put("access", accessToken);
			accessMap.put("platform", platform);
			
			HashMap<String, Object> refreshMap = new HashMap<>();
			refreshMap.put("refresh", refreshToken);
			refreshMap.put("platform", platform);
			
			String accessJwt = JwtModule.generateJwt(state, accessMap);
			String refreshJwt = JwtModule.generateJwt(state, refreshMap);
			
			NewCookie accessCookie = new NewCookie("access", accessJwt, "/oauth2", ".itcode.dev", "access token", -1, true, true);
			NewCookie refreshCookie = new NewCookie("refresh", refreshJwt, "/oauth2", ".itcode.dev", "refresh token", refreshToken == null ? 0 : 86400 * 7 + 3600 * 9, true, true);
			
			responseBean.setFlag(true);
			responseBean.setTitle("success");
			responseBean.setMessage("authorized success");
			responseBean.setBody(null);
			
			response = Response.ok(responseBean, MediaType.APPLICATION_JSON).cookie(accessCookie, refreshCookie).build();
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
	 * 자동 로그인 응답 반환 메서드
	 *
	 * @param accessCookie: [String] 접근 토큰 쿠키
	 * @param refreshCookie: [String] 리프레쉬 토큰 쿠키
	 *
	 * @return [Response] 응답 객체
	 */
	public Response postAutoLoginResponse(String accessCookie, String refreshCookie)
	{
		Response response;
		
		ResponseBean<String> responseBean = new ResponseBean<>();
		
		// 자동 로그인 시도
		try
		{
			// 접근 토큰 쿠키가 있을 경우
			if (accessCookie != null)
			{
				responseBean.setFlag(true);
				responseBean.setTitle("success");
				responseBean.setMessage("auto authorized success");
				responseBean.setBody(null);
				
				response = Response.ok(responseBean, MediaType.APPLICATION_JSON).build();
			}
			
			// 리프레쉬 토큰 쿠키가 없을 경우
			else if (refreshCookie == null)
			{
				responseBean.setFlag(false);
				responseBean.setTitle("fail");
				responseBean.setMessage("refresh token is empty");
				responseBean.setBody(null);
				
				response = Response.ok(responseBean, MediaType.APPLICATION_JSON).build();
			}
			
			// 리프레쉬 토큰 쿠키가 있을 경우
			else
			{
				Jws<Claims> refreshJws = JwtModule.openJwt(refreshCookie);
				
				String refreshToken = refreshJws.getBody().get("refresh", String.class);
				String platform = refreshJws.getBody().get("platform", String.class);
				
				AuthModule authModule = getAuthModule(platform);
				
				OAuth2AccessToken oAuth2AccessToken = authModule.getRefreshAccessToken(refreshToken);
				
				String accessToken = oAuth2AccessToken.getAccessToken();
				
				HashMap<String, Object> accessMap = new HashMap<>();
				accessMap.put("access", accessToken);
				accessMap.put("platform", platform);
				
				HashMap<String, Object> refreshMap = new HashMap<>();
				refreshMap.put("refresh", refreshToken);
				refreshMap.put("platform", platform);
				
				String uuid = UUID.randomUUID().toString();
				
				String accessJwt = JwtModule.generateJwt(uuid, accessMap);
				String refreshJwt = JwtModule.generateJwt(uuid, refreshMap);
				
				NewCookie newAccessCookie = new NewCookie("access", accessJwt, "/oauth2", ".itcode.dev", "access token", -1, true, true);
				NewCookie newRefreshCookie = new NewCookie("refresh", refreshJwt, "/oauth2", ".itcode.dev", "refresh token", 86400 * 7 + 3600 * 9, true, true);
				
				responseBean.setFlag(true);
				responseBean.setTitle("success");
				responseBean.setMessage("auto authorized success");
				responseBean.setBody(null);
				
				response = Response.ok(responseBean, MediaType.APPLICATION_JSON).cookie(newAccessCookie, newRefreshCookie).build();
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
			
			NewCookie newAccessCookie = new NewCookie("access", null, "/oauth2", ".itcode.dev", "access token", 0, true, true);
			NewCookie newRefreshCookie = new NewCookie("refresh", null, "/oauth2", ".itcode.dev", "refresh token", 0, true, true);
			
			response = Response.status(Response.Status.BAD_REQUEST).entity(responseBean).type(MediaType.APPLICATION_JSON).cookie(newAccessCookie, newRefreshCookie).build();
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
			
			responseBean.setFlag(true);
			responseBean.setTitle("success");
			responseBean.setMessage("logout success");
			responseBean.setBody(null);
			
			response = Response.ok(responseBean, MediaType.APPLICATION_JSON).cookie(accessCookie, refreshCookie).build();
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