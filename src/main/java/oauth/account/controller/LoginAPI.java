package oauth.account.controller;

import global.module.API;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import oauth.account.bean.LoginResponseBean;
import oauth.account.process.AccountGetProcess;
import oauth.account.process.AccountPostProcess;
import oauth.account.process.AccountPutProcess;

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
	 * 정보 제공 동의 갱신 URL 응답 메서드
	 *
	 * @param accessCookie: [String] 접근 토큰 쿠키
	 *
	 * @return [Response] 응답 객체
	 */
	@PUT
	@Path("/put")
	public Response putAuthorizationUrlResponse(@PathParam("access") String accessCookie)
	{
		return new AccountPutProcess(request, response).putAgreementUrlResponse(accessCookie);
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
	
	/**
	 * 자동 로그인 응답 메서드
	 *
	 * @param accessCookie: [String] 접근 토큰 쿠키
	 * @param refreshCookie: [String] 리프레쉬 토큰 쿠키
	 *
	 * @return [Response] 응답 객체
	 */
	@POST
	@Path("/auto")
	public Response autoLoginResponse(@CookieParam("access") String accessCookie, @CookieParam("refresh") String refreshCookie)
	{
		return new AccountPostProcess(request, response).postAutoLoginResponse(accessCookie, refreshCookie);
	}
}