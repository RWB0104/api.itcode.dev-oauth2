package dev.itcode.oauth2.core.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2LoginSpec;
import org.springframework.stereotype.Component;

/**
 * OAuth2 커스토마이저 클래스
 *
 * @author RWB
 * @since 2025.09.12 Fri 19:05:59
 */
@Component
@RequiredArgsConstructor
public class OAuth2Customizer implements Customizer<OAuth2LoginSpec>
{
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	
	/**
	 * 커스토마이저 메서드
	 *
	 * @param oAuth2LoginSpec (OAuth2LoginSpec) OAuth2LoginSpec 객체
	 */
	@Override
	public void customize(OAuth2LoginSpec oAuth2LoginSpec)
	{
		oAuth2LoginSpec.authenticationSuccessHandler(oAuth2SuccessHandler);
	}
}
