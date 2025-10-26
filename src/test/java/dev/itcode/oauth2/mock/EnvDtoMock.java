package dev.itcode.oauth2.mock;

import dev.itcode.oauth2.core.env.EnvDto;
import org.springframework.boot.test.context.TestComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Env 환경변수 DTO 모킹 클래스
 *
 * @author RWB
 * @since 2025.10.26 Sun 02:11:13
 */
@TestComponent
public class EnvDtoMock extends EnvDto
{
	/**
	 * 프론트엔드 URL 반환 메서드
	 *
	 * @return (String)
	 */
	@Override
	public String getFrontendUrl()
	{
		return "http://localhost:3000";
	}
	
	/**
	 * CORS 허용 도메인 반환 메서드
	 *
	 * @return (List) CORS 허용 도메인
	 */
	@Override
	public List<String> getCorsOrigins()
	{
		return new ArrayList<>();
	}
}
