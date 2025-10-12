package dev.itcode.oauth2.api.base.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * API 응답 DTO 클래스
 *
 * @param <T> 응답 객체 타입
 *
 * @author RWB
 * @since 2025.10.10 Fri 19:25:56
 */
@Setter
@Getter
@Builder
public class ApiResponseDto<T>
{
	/**
	 * UUID
	 */
	private final String uuid;
	
	/**
	 * 응답
	 */
	private final T body;
	
	/**
	 * API 경로
	 */
	private final String path;
	
	/**
	 * 타임스탬프
	 */
	@Builder.Default
	private final long timestamp = System.currentTimeMillis();
	
	/**
	 * 제목
	 */
	private String title;
	
	/**
	 * 설명
	 */
	private String description;
}
