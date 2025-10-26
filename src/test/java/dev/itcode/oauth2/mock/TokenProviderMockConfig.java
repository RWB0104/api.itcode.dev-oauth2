package dev.itcode.oauth2.mock;

import dev.itcode.oauth2.core.token.ITokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 토큰 프로바이더 모킹 설정 클래스
 *
 * @author RWB
 * @since 2025.10.26 Sun 02:08:03
 */
@TestConfiguration
public class TokenProviderMockConfig
{
	/**
	 * ITokenProvider 반환 메서드
	 *
	 * @return (ITokenProvider) ITokenProvider 인터페이스
	 */
	@Bean
	public ITokenProvider tokenProvider()
	{
		return new TokenProviderMock();
	}
}
