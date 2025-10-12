package dev.itcode.oauth2.core.filter;

import dev.itcode.oauth2.core.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * 인증 필터 클래스
 *
 * @author RWB
 * @since 2025.10.12 Sun 11:59:45
 */
@Component
@RequiredArgsConstructor
public class AuthorizationFilter implements WebFilter
{
	private final TokenProvider tokenProvider;
	
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
		return Mono.fromCallable(() -> Objects.requireNonNull(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION)))
				.filter(authorization -> authorization.startsWith("Bearer "))
				.map(authorization -> authorization.replaceFirst("^Bearer ", ""))
				.flatMap(token ->
						Mono.fromCallable(() -> tokenProvider.decrypt(token))
								.flatMap(claims ->
								{
									Authentication auth = new UsernamePasswordAuthenticationToken(
											claims.getPayload().getId(),
											null,
											List.of(new SimpleGrantedAuthority("ROLE_USER"))
									);
									SecurityContext context = new SecurityContextImpl(auth);
									
									return chain.filter(exchange)
											.contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
								})
				)
				.onErrorResume((_) ->
				{
					exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
					return exchange.getResponse().setComplete();
				});
	}
}
