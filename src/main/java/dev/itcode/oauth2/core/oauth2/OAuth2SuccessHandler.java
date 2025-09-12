package dev.itcode.oauth2.core.oauth2;

import dev.itcode.oauth2.core.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;

/**
 * OAuth2 성공 핸들러 클래스
 *
 * @author RWB
 * @since 2025.09.12 Fri 19:08:33
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends RedirectServerAuthenticationSuccessHandler
{
	private final TokenProvider tokenProvider;
	
	/**
	 * 인증 성공 메서드
	 *
	 * @param webFilterExchange (WebFilterExchange) WebFilterExchange 객체
	 * @param authentication (Authentication) Authentication 객체
	 *
	 * @return (Mono) Mono 객체
	 */
	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication)
	{
		return Mono.just(authentication)
				.map(Authentication::getPrincipal)
				.cast(DefaultOAuth2User.class)
				.flatMap(defaultOAuth2User ->
				{
					HashMap<String, Object> map = new HashMap<>();
					
					map.put("email", defaultOAuth2User.getAttributes().get("email"));
					map.put("name", defaultOAuth2User.getAttributes().get("name"));
					map.put("picture", defaultOAuth2User.getAttributes().get("picture"));
					
					String id = authentication.getName();
					
					return tokenProvider.publishAsync(id, map);
				})
				.flatMap(token ->
				{
					String redirectUrl = UriComponentsBuilder
							.fromUriString("https://project.itcode.dev/oauth2/callback/google")
							.queryParam("token", token)
							.build()
							.toUriString();
					
					setLocation(URI.create(redirectUrl));
					
					return super.onAuthenticationSuccess(webFilterExchange, authentication);
				});
	}
}
