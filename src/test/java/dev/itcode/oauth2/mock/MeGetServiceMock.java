package dev.itcode.oauth2.mock;

import dev.itcode.oauth2.api.base.dto.ApiResponseDto;
import dev.itcode.oauth2.api.me.dto.MeDto;
import dev.itcode.oauth2.api.me.service.IMeGetService;
import dev.itcode.oauth2.core.oauth2.Platform;
import org.springframework.boot.test.context.TestComponent;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 내 정보 GET 서비스 모킹 메서드
 *
 * @author RWB
 * @since 2025.10.26 Sun 04:46:00
 */
@TestComponent
public class MeGetServiceMock implements IMeGetService
{
	/**
	 * 내 정보 응답 반환 비동기 메서드
	 *
	 * @param authorization (String) 인증 헤더
	 *
	 * @return (Mono) 내 정보 응답 객체
	 */
	@Override
	public Mono<ApiResponseDto<MeDto>> getMe(String authorization)
	{
		return Mono.just(new MeDto(Platform.GOOGLE, "홍길동", "user@example.com", "https://placehold.co/64"))
				.map(meDto -> ApiResponseDto.<MeDto>builder()
						.uuid(UUID.randomUUID().toString())
						.body(meDto)
						.path("/api/me")
						.build());
	}
	
	/**
	 * 유효성 결과 응답 반환 비동기 메서드
	 *
	 * @param authorization (String) 인증 헤더
	 *
	 * @return (Mono) 유효성 결과 응답 객체
	 */
	@Override
	public Mono<ApiResponseDto<Boolean>> getMeValidate(String authorization)
	{
		return Mono.just(Math.random() > 0.5)
				.map(is -> ApiResponseDto.<Boolean>builder()
						.uuid(UUID.randomUUID().toString())
						.body(is)
						.path("/api/me")
						.build());
	}
}
