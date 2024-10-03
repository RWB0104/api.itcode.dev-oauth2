package oauth.account.controller;

import global.module.API;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * 핑 API 클래스
 * 
 * @author RWB
 * @since 2024.10.03 Thu 23:18:58
 */
@Path("/ping")
public class PingAPI extends API
{
	/**
	 * 핑 응답 메서드
	 * 
	 * @return [Response] 응답 객체
	 */
	@GET
	public Response pingResponse()
	{
		return Response.noContent().build();
	}
}
