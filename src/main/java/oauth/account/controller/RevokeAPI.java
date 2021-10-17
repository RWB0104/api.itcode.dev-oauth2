package oauth.account.controller;

import global.module.API;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import oauth.account.process.AccountGetProcess;

/**
 * 취소 API 클래스
 *
 * @author RWB
 * @since 2021.10.18 Mon 01:19:30
 */
@Path("/revoke")
public class RevokeAPI extends API
{
	/**
	 * 연동 해제 URL 응답 메서드
	 *
	 * @param platform: [String] 플랫폼
	 *
	 * @return [Response] 응답 객체
	 */
	@DELETE
	@Path("/{platform}")
	public Response authorizationUrlResponse(@PathParam("platform") String platform)
	{
		return new AccountGetProcess(request, response).getAuthorizationUrlResponse(platform);
	}
}