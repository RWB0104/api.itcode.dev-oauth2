package oauth.account.controller;

import global.module.API;
import oauth.account.process.AccountGetProcess;
import oauth.account.process.AccountPostProcess;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

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
	 * @param code: [String] 인증 코드
	 * @param state: [String] 고유 상태값
	 *
	 * @return [Response] 응답 객체
	 */
	@POST
	@Path("/{platform}")
	public Response loginResponse(@PathParam("platform") String platform, @QueryParam("code") String code, @QueryParam("state") String state)
	{
		return new AccountPostProcess(request, response).postLoginResponse(platform, code, state);
	}
}