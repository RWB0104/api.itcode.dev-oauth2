package dev.itcode.oauth2.core.config;

import dev.itcode.oauth2.core.oauth2.OAuth2Customizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

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
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.oauth2Login(oAuth2Customizer);
		
		return http.build();
	}
	
	/**
	 * CORS 메서드
	 */
	@Bean
	public CorsWebFilter corsWebFilter()
	{
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(List.of("https://itcode.dev", "https://project.itcode.dev"));
		corsConfig.setMaxAge(8000L);
		corsConfig.addAllowedMethod("*");
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		
		return new CorsWebFilter(source);
	}
}
