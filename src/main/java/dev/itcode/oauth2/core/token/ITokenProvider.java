package dev.itcode.oauth2.core.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Map;

/**
 * 토큰 프로바이더 인터페이스
 *
 * @author RWB
 * @since 2025.10.26 Sun 00:42:57
 */
public interface ITokenProvider
{
	/**
	 * JWT 발급 메서드
	 *
	 * @param id (String) 아이디
	 * @param claims (Map) 내용
	 *
	 * @return (String) JWT
	 */
	String publish(String id, Map<String, ?> claims);
	
	/**
	 * 복호화 메서드
	 *
	 * @param token (String) 토큰
	 *
	 * @return (Jws) Claims
	 */
	Jws<Claims> decrypt(String token);
}
