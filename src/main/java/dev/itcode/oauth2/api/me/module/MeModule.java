package dev.itcode.oauth2.api.me.module;

import dev.itcode.oauth2.api.me.dto.MeDto;
import dev.itcode.oauth2.core.token.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 내 정보 모듈
 *
 * @author RWB
 * @since 2025.10.06 Mon 11:49:35
 */
@Component
@RequiredArgsConstructor
public class MeModule
{
	private final TokenProvider tokenProvider;
	
	/**
	 * 내 정보 반환 모듈 메서드
	 *
	 * @param authorization (String) 인증 헤더
	 *
	 * @return (MeDto) 내 정보
	 */
	public MeDto getMeModule(String authorization)
	{
		String token = getTokenSubmodule(authorization);
		
		Jws<Claims> jws = tokenProvider.decrypt(token);
		Claims claims = jws.getPayload();
		
		String name = claims.get("name", String.class);
		String email = claims.get("email", String.class);
		String picture = claims.get("picture", String.class);
		
		return new MeDto(name, email, picture);
		
	}
	
	/**
	 * 토큰 반환 서브모듈 메서드
	 *
	 * @param authorization (String) 인증 헤더
	 *
	 * @return (String) 토큰
	 */
	private String getTokenSubmodule(String authorization)
	{
		return Optional.ofNullable(authorization)
				.filter(auth -> auth.startsWith("Bearer "))
				.map(auth -> auth.replaceFirst("^Bearer\\s+", ""))
				.orElseThrow(() -> new IllegalArgumentException(""));
	}
}
