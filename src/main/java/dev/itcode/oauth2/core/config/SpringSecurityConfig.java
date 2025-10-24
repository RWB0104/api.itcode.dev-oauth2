package dev.itcode.oauth2.core.config;

import dev.itcode.oauth2.core.env.EnvDto;
import dev.itcode.oauth2.core.filter.AuthorizationFilter;
import dev.itcode.oauth2.core.filter.HeaderFilter;
import dev.itcode.oauth2.core.oauth2.OAuth2Customizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Spring Security 설정 클래스
 *
 * @author RWB
 * @since 2025.09.12 Fri 18:45:27
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig
{
	private final OAuth2Customizer oAuth2Customizer;
	private final EnvDto envDto;
	
	private final HeaderFilter headerFilter;
	private final AuthorizationFilter authorizationFilter;
	
	/**
	 * 필터 체인 반환 메서드
	 *
	 * @param http (ServerHttpSecurity) ServerHttpSecurity 객체
	 *
	 * @return (SecurityWebFilterChain) SecurityWebFilterChain 객체
	 */
	@Bean
	public SecurityWebFilterChain getSecurityWebFilterChain(ServerHttpSecurity http)
	{
		http.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
				.cors(Customizer.withDefaults())
				.authorizeExchange(exchange -> exchange
						.pathMatchers("/api/me").authenticated()
						.anyExchange().permitAll())
				.addFilterBefore(headerFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.addFilterAt(authorizationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
				.oauth2Login(oAuth2Customizer);
		
		return http.build();
	}
	
	/**
	 * CORS 필터 반환 메서드
	 *
	 * @return (CorsWebFilter) CORS 필터
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public CorsWebFilter corsWebFilter()
	{
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setMaxAge(8000L);
		corsConfig.addAllowedHeader("*");
		corsConfig.addAllowedMethod("*");
		corsConfig.addAllowedOrigin(envDto.getFrontendUrl());
		
		envDto.getCorsOrigins().forEach(corsConfig::addAllowedOrigin);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		
		return new CorsWebFilter(source);
	}
}
