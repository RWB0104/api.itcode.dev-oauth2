package dev.itcode.oauth2.core.env;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

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
	 * 프로필 배열 반환 비동기 메서드
	 *
	 * @return (Mono) 프로필 배열
	 */
	public Mono<String[]> getProfilesAsync()
	{
		return Mono.just(getProfiles());
		
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
	
	/**
	 * 프로덕션 여부 반환 비동기 메서드
	 *
	 * @return (Mono) 프로덕션 여부
	 */
	public Mono<Boolean> isProductionAsync()
	{
		return Mono.just(isProduction());
	}
	
	/**
	 * 프론트엔드 URL 반환 메서드
	 *
	 * @return (String) 프론트엔드 URL
	 */
	public String getFrontendUrl()
	{
		return Objects.requireNonNull(environment.getProperty("env.frontend-url"));
	}
	
	/**
	 * 프론트엔드 URL 반환 비동기 메서드
	 *
	 * @return (Mono) 프론트엔드 URL
	 */
	public Mono<String> getFrontendUrlAsync()
	{
		return Mono.just(getFrontendUrl());
	}
	
	/**
	 * CORS Origins 반환 메서드
	 *
	 * @return (String[]) CORS Origins
	 */
	public String[] getCorsOrigins()
	{
		return environment.getProperty("env.cors-origins", String[].class, new String[] {});
	}
	
	/**
	 * CORS Origins 반환 비동기 메서드
	 *
	 * @return (Mono) CORS Origins
	 */
	public Mono<String[]> getCorsOriginsAsync()
	{
		return Mono.just(getCorsOrigins());
	}
}
