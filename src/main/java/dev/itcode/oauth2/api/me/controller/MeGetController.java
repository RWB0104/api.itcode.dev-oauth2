package dev.itcode.oauth2.api.me.controller;

import dev.itcode.oauth2.api.base.dto.ApiResponseDto;
import dev.itcode.oauth2.api.me.dto.MeDto;
import dev.itcode.oauth2.api.me.service.MeGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 내 정보 GET 컨트롤러 메서드
 *
 * @author RWB
 * @since 2025.10.06 Mon 11:47:20
 */
@RestController
@RequestMapping(method = RequestMethod.GET, path = "/api/me")
@RequiredArgsConstructor
public class MeGetController
{
	private final MeGetService service;
	
	/**
	 * 내 정보 API 메서드
	 *
	 * @param authorization (String) 응답 헤더
	 *
	 * @return (Mono) 내 정보 API
	 */
	@GetMapping
	public Mono<ApiResponseDto<MeDto>> getMeApi(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization)
	{
		return service.getMe(authorization);
	}
}
