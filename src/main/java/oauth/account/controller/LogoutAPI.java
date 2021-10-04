package oauth.account.controller;

import global.module.API;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import oauth.account.process.AccountPostProcess;

/**
 * 로그아웃 API 클래스
 *
 * @author RWB
 * @since 2021.10.04 Mon 21:19:00
 */
@Path("/logout")
public class LogoutAPI extends API
{
	/**
	 * 로그아웃 응답 메서드
	 *
	 * @return [Response] 응답 객체
	 */
	@POST
	@Path("")
	public Response logoutResponse()
	{
		return new AccountPostProcess(request, response).postLogoutResponse();
	}
}