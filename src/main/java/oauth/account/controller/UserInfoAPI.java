package oauth.account.controller;

import global.module.API;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import oauth.account.process.AccountGetProcess;

/**
 * 사용자 정보 API 클래스
 *
 * @author RWB
 * @since 2021.10.02 Sat 00:29:46
 */
@Path("/userinfo")
public class UserInfoAPI extends API
{
	/**
	 * 사용자 정보 응답 메서드
	 *
	 * @param platform: [String] 플랫폼
	 * @param access: [String] 접근 토큰
	 *
	 * @return [Response] 응답 객체
	 */
	@GET
	@Path("")
	public Response userInfoResponse(@CookieParam("platform") String platform, @CookieParam("access") String access)
	{
		return new AccountGetProcess(request, response).getUserInfoResponse(platform, access);
	}
}