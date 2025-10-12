package dev.itcode.oauth2.core.filter;

import dev.itcode.oauth2.core.env.EnvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 헤더 필터
 *
 * @author RWB
 * @since 2025.09.13 Sat 04:56:35
 */
@Component
@RequiredArgsConstructor
public class HeaderFilter implements WebFilter
{
	private final EnvDto envDto;
	
	private final String[] whitelists = {
			"/login/oauth2/code"
	};
	
	/**
	 * 필터 메서드
	 *
	 * @param exchange (ServerWebExchange) ServerWebExchange 객체
	 * @param chain (WebFilterChain) WebFilterChain 객체
	 */
	@Override
	@NonNull
	public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain)
	{
		String path = exchange.getRequest().getURI().getPath();
		
		// 화이트 리스트 대상일 경우, 필터 스킵
		if (Arrays.stream(whitelists).anyMatch(path::startsWith))
		{
			return chain.filter(exchange);
		}
		
		String referer = exchange.getRequest().getHeaders().getFirst("Referer");
		
		// 리퍼러가 없을 경우
		if (referer == null)
		{
			return errorResponse(exchange);
		}
		
		return Mono.just(referer)
				.map(ref ->
				{
					List<String> allows = envDto.getCorsOrigins();
					
					return allows.stream().anyMatch(ref::startsWith);
				})
				.flatMap(hasValid ->
				{
					// 유효한 리퍼러일 경우
					if (hasValid)
					{
						return chain.filter(exchange);
					}
					
					// 아닐 경우
					else
					{
						return errorResponse(exchange);
					}
				});
	}
	
	/**
	 * 에러 응답 메서드
	 *
	 * @param exchange (ServerWebExchange) ServerWebExchange 객체
	 */
	private Mono<Void> errorResponse(ServerWebExchange exchange)
	{
		exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
		return exchange.getResponse().setComplete();
	}
}
