package dev.itcode.oauth2.mock;

import dev.itcode.oauth2.api.me.service.IMeGetService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 내 정보 GET 서비스 모킹 컨트롤러 메서드
 *
 * @author RWB
 * @since 2025.10.26 Sun 04:45:54
 */
@TestConfiguration
public class MeGetServiceMockConfig
{
	/**
	 * IMeGetController 반환 메서드
	 *
	 * @return (IMeGetController) IMeGetController 인터페이스
	 */
	@Bean
	public IMeGetService meGetService()
	{
		return new MeGetServiceMock();
	}
}
