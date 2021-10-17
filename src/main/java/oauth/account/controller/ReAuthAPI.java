package oauth.account.controller;

import global.module.API;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import oauth.account.process.AccountGetProcess;

/**
 * 재인증 API 클래스
 *
 * @author RWB
 * @since 2021.10.18 Mon 01:24:31
 */
@Path("/reauth")
public class ReAuthAPI extends API
{
	/**
	 * 재인증 URL 응답 메서드
	 *
	 * @param accessCookie: [String] 접근 토큰 쿠키
	 *
	 * @return [Response] 응답 객체
	 */
	@GET
	@Path("")
	public Response reAuthorizationUrlResponse(@CookieParam("access") String accessCookie)
	{
		return new AccountGetProcess(request, response).getReAuthorizationUrlResponse(accessCookie);
	}
}