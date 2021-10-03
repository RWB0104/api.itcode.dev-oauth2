package oauth.account.controller;

import global.module.API;
import oauth.account.process.AccountPostProcess;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

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
	 * @param code: [String] 인증 코드
	 *
	 * @return [Response] 응답 객체
	 */
	@GET
	@Path("/{platform}")
	public Response userInfoResponse(@PathParam("platform") String platform, @QueryParam("code") String code)
	{
		return new AccountPostProcess(request, response).postUserInfoResponse(platform, code);
	}
}