package global.module;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * CORS 필터 클래스
 *
 * @author RWB
 * @since 2021.10.02 Sat 15:42:04
 */
@Provider
public class CorsFilter implements ContainerResponseFilter
{
	/**
	 * 필터 메서드
	 *
	 * @param requestContext: [ContainerRequestContext] ContainerRequestContext 객체
	 * @param responseContext: [ContainerResponseContext] ContainerResponseContext 객체
	 */
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
	{
		String origin = requestContext.getHeaderString("origin");
		
		// origin이 유효하고, itcode.dev 계열의 URL일 경우
		if (origin != null && origin.contains("itcode.dev"))
		{
			responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
			responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
			responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
			responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type");
		}
	}
}