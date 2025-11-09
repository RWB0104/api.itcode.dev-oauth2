package dev.itcode.oauth2.api.me.service;

import dev.itcode.oauth2.api.base.dto.ApiResponseDto;
import dev.itcode.oauth2.api.me.dto.MeDto;
import dev.itcode.oauth2.api.me.module.MeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 내 정보 GET 서비스
 *
 * @author RWB
 * @since 2025.10.06 Mon 11:49:09
 */
@Component
@RequiredArgsConstructor
public class MeGetService implements IMeGetService
{
	private final MeModule module;
	
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
		return Mono.fromCallable(() -> module.getMeModule(authorization))
				.map(dto -> ApiResponseDto.<MeDto>builder()
						.uuid(UUID.randomUUID().toString())
						.path("/api/me")
						.body(dto)
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
		return Mono.fromCallable(() ->
				{
					try
					{
						module.getMeModule(authorization);
						
						return true;
					}
					
					catch (Exception _)
					{
						return false;
					}
				})
				.map(is -> ApiResponseDto.<Boolean>builder()
						.uuid(UUID.randomUUID().toString())
						.path("/api/me/validate")
						.body(is)
						.build());
	}
}
