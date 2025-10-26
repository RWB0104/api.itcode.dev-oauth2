package dev.itcode.oauth2.api.me.service;

import dev.itcode.oauth2.api.base.dto.ApiResponseDto;
import dev.itcode.oauth2.api.me.dto.MeDto;
import reactor.core.publisher.Mono;

/**
 * 내 정보 GET 서비스 인터페이스
 *
 * @author RWB
 * @since 2025.10.26 Sun 04:58:13
 */
public interface IMeGetService
{
	/**
	 * 내 정보 응답 반환 비동기 메서드
	 *
	 * @param authorization (String) 인증 헤더
	 *
	 * @return (Mono) 내 정보 응답 객체
	 */
	Mono<ApiResponseDto<MeDto>> getMe(String authorization);
}
