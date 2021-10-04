package global.module;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * CORS 필터 클래스
 *
 * @author RWB
 * @since 2021.10.02 Sat 15:42:04
 */
@Provider
public class CorsFilter implements ContainerResponseFilter
{
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException
	{
		String origin = requestContext.getHeaderString("origin");
		
		// origin이 유효하고, itcode.dev 계열의 URL일 경우
		if (origin != null && origin.contains("itcode.dev"))
		{
			responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
			responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
			responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
			responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type");
		}
	}
}