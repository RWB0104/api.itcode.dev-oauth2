package oauth.account.controller;

import global.module.API;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import oauth.account.bean.LoginResponseBean;
import oauth.account.process.AccountGetProcess;
import oauth.account.process.AccountPostProcess;

/**
 * 로그인 API 클래스
 *
 * @author RWB
 * @since 2021.09.30 Thu 20:44:43
 */
@Path("/login")
public class LoginAPI extends API
{
	/**
	 * 인증 URL 응답 메서드
	 *
	 * @param platform: [String] 플랫폼
	 *
	 * @return [Response] 응답 객체
	 */
	@GET
	@Path("/{platform}")
	public Response authorizationUrlResponse(@PathParam("platform") String platform)
	{
		return new AccountGetProcess(request, response).getAuthorizationUrlResponse(platform);
	}
	
	/**
	 * 로그인 응답 메서드
	 *
	 * @param platform: [String] 플랫폼
	 * @param loginResponseBean: [LoginResponseBean] LoginResponseBean 객체
	 *
	 * @return [Response] 응답 객체
	 */
	@POST
	@Path("/{platform}")
	public Response loginResponse(@PathParam("platform") String platform, LoginResponseBean loginResponseBean)
	{
		return new AccountPostProcess(request, response).postLoginResponse(platform, loginResponseBean.getCode(), loginResponseBean.getState());
	}
	
	@POST
	@Path("/auto")
	public Response autoLoginResponse(@CookieParam("refresh") String refresh)
	{
		return new AccountPostProcess(request, response).postAutoLoginResponse(refresh);
	}
}