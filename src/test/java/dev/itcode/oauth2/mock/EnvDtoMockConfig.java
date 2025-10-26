package dev.itcode.oauth2.mock;

import dev.itcode.oauth2.core.env.EnvDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Env 환경변수 DTO 모킹 설정 클래스
 *
 * @author RWB
 * @since 2025.10.26 Sun 02:14:57
 */
@TestConfiguration
public class EnvDtoMockConfig
{
	/**
	 * EnvDto 반환 메서드
	 *
	 * @return (EnvDto) EnvDto 객체
	 */
	@Bean
	public EnvDto envDto()
	{
		return new EnvDtoMock();
	}
}
