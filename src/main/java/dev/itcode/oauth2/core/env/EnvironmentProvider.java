package dev.itcode.oauth2.core.env;

import dev.itcode.oauth2.core.enums.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 환경 제공자 클래스
 *
 * @author RWB
 * @since 2025.09.13 Sat 00:25:57
 */
@Component
@RequiredArgsConstructor
public class EnvironmentProvider
{
	private final Environment environment;
	
	/**
	 * 프로필 배열 반환 메서드
	 *
	 * @return (String[]) 프로필 배열
	 */
	public String[] getProfiles()
	{
		String[] activeProfiles = environment.getActiveProfiles();
		
		// 명시된 프로필이 하나도 없을 경우 기본 프로필 반환
		if (activeProfiles.length == 0)
		{
			return environment.getDefaultProfiles();
		}
		
		return environment.getActiveProfiles();
	}
	
	/**
	 * 프로덕션 여부 반환 메서드
	 *
	 * @return (boolean) 프로덕션 여부
	 */
	public boolean isProduction()
	{
		return Arrays.asList(getProfiles()).contains(Profile.PRODUCTION.getProfile());
	}
}
