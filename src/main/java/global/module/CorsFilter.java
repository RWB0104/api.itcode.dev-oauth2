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
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "project.itcode.dev");
	}
}