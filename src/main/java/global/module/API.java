package global.module;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

/**
 * API 추상 클래스
 *
 * @author RWB
 * @since 2021.09.29 Wed 22:34:27
 */
abstract public class API
{
	@Context
	protected HttpServletRequest request;
	
	@Context
	protected HttpServletResponse response;
	
	@Context
	protected UriInfo uriInfo;
}