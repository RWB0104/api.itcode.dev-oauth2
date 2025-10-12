package dev.itcode.oauth2.api.me.dto;

import dev.itcode.oauth2.core.oauth2.Platform;

/**
 * 내 정보 레코드 클래스
 *
 * @param name 이름
 * @param email 이메일
 * @param picture 프로필 사진 URL
 *
 * @author RWB
 * @since 2025.10.10 Fri 19:38:57
 */
public record MeDto(Platform platform, String name, String email, String picture)
{
	// empty
}
