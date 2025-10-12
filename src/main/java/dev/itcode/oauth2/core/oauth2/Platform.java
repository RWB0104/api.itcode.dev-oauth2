package dev.itcode.oauth2.core.oauth2;

import lombok.Getter;

@Getter
public enum Platform
{
	GITHUB("github"),
	GOOGLE("google"),
	KAKAO("kakao"),
	NAVER("naver");
	
	/**
	 * 플랫폼
	 */
	private final String platform;
	
	/**
	 * 생성자 메서드
	 *
	 * @param platform (String) 플랫폼
	 */
	Platform(String platform)
	{
		this.platform = platform;
	}
}
