package dev.itcode.oauth2.core.filter;

import dev.itcode.oauth2.core.env.EnvironmentProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;

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
	private final EnvironmentProvider environmentProvider;
	
	/**
	 * 필터 메서드
	 *
	 * @param exchange (ServerWebExchange) ServerWebExchange 객체
	 * @param chain (WebFilterChain) WebFilterChain 객체
	 */
	@Override
	@NonNull
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain)
	{
		return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Referer"))
				.zipWith(environmentProvider.getCorsOriginsAsync())
				.filter(tuple ->
				{
					String referer = tuple.getT1();
					String[] allows = tuple.getT2();
					
					return Arrays.stream(allows).anyMatch(referer::startsWith);
				})
				.then(chain.filter(exchange))
				.switchIfEmpty(Mono.defer(() ->
				{
					exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
					
					return exchange.getResponse().setComplete();
				}));
	}
}
