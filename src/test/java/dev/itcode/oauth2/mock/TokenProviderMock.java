package dev.itcode.oauth2.mock;

import dev.itcode.oauth2.core.oauth2.Platform;
import dev.itcode.oauth2.core.token.ITokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.test.context.TestComponent;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 토큰 프로바이더 모킹 클래스
 *
 * @author RWB
 * @since 2025.10.26 Sun 02:07:37
 */
@TestComponent
public class TokenProviderMock implements ITokenProvider
{
	private final Key key = Keys.hmacShaKeyFor(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
	
	/**
	 * JWT 발급 메서드
	 *
	 * @param id (String) 아이디
	 * @param claims (Map) 내용
	 *
	 * @return (String) JWT
	 */
	@Override
	public String publish(String id, Map<String, ?> claims)
	{
		return Jwts.builder()
				.subject(id)
				.issuer("api.itcode.dev-oauth2-test")
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 3600_000))
				.claims(claims)
				.signWith(key)
				.compact();
	}
	
	/**
	 * 복호화 메서드
	 *
	 * @param token (String) 토큰
	 *
	 * @return (Jws) Claims
	 */
	@Override
	public Jws<Claims> decrypt(String token)
	{
		HashMap<String, String> map = new HashMap<>();
		map.put("platform", Platform.GOOGLE.getPlatform());
		map.put("name", "tester");
		map.put("email", "test@example.com");
		map.put("picture", "http://placehold.co/64");
		
		String mockToken = publish("ed873b27-4e0e-4b5b-bde8-c612ab102bf7", map);
		
		return Jwts.parser()
				.verifyWith((SecretKey) key)
				.build()
				.parseSignedClaims(mockToken);
	}
}
