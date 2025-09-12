package dev.itcode.oauth2.core.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 토큰 프로바이더 클래스
 *
 * @author RWB
 * @since 2025.09.12 Fri 19:12:14
 */
@Component
public class TokenProvider
{
	private final Key key;
	
	/**
	 * 생성자 메서드
	 */
	public TokenProvider()
	{
		key = Keys.hmacShaKeyFor(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * 생성자 메서드
	 *
	 * @param key (String) 키 문자열
	 */
	public TokenProvider(String key)
	{
		this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * 생성자 메서드
	 *
	 * @param key (byte[]) 키 바이트
	 */
	public TokenProvider(byte[] key)
	{
		this.key = Keys.hmacShaKeyFor(key);
	}
	
	/**
	 * 생성자 메서드
	 *
	 * @param key (Key) 키
	 */
	public TokenProvider(Key key)
	{
		this.key = key;
	}
	
	/**
	 * JWT 발급 메서드
	 *
	 * @param id (String) 아이디
	 * @param clams (Map) 내용
	 *
	 * @return (String) JWT
	 */
	public String publish(String id, Map<String, ?> clams)
	{
		return Jwts.builder()
				.subject(id)
				.issuer("api.itcode.dev-oauth2")
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 3600_000))
				.claims(clams)
				.signWith(key)
				.compact();
	}
	
	/**
	 * JWT 발급 비동기 메서드
	 *
	 * @param id (String) 아이디
	 * @param clams (Map) 내용
	 *
	 * @return (Mono) JWT
	 */
	public Mono<String> publishAsync(String id, Map<String, ?> clams)
	{
		return Mono.fromSupplier(() -> publish(id, clams));
	}
}
