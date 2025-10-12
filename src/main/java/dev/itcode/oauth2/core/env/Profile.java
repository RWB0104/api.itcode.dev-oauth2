package dev.itcode.oauth2.core.env;

import lombok.Getter;

/**
 * 프로필 enum
 *
 * @author RWB
 * @since 2025.09.13 Sat 02:58:00
 */
@Getter
public enum Profile
{
	DEVELOPMENT("development"),
	TEST("test"),
	PRODUCTION("production");
	
	/**
	 * 프로필
	 */
	private final String profile;
	
	/**
	 * 생성자 메서드
	 *
	 * @param profile (String) 환경
	 */
	Profile(String profile)
	{
		this.profile = profile;
	}
}
