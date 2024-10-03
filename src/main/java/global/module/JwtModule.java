package global.module;

import com.github.scribejava.core.base64.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 모듈 클래스
 *
 * @author RWB
 * @since 2021.10.02 Sat 01:30:06
 */
public class JwtModule
{
	private static final String AES_KEY = "c907e5e522151738bdbc0f0d0d21beec6d4c123b414cc309aa18602702ab40d0d8b30baf2e40c877f8bbeb061a90137981db0de5a8a20b6fb8bda762f9ad1811";
	
	/**
	 * JWT 생성 및 반환 메서드
	 *
	 * @param id: [String] 아이디
	 * @param claimsMap: [Map<String, Object>] 맵
	 *
	 * @return [String] JWT
	 */
	public static String generateJwt(String id, Map<String, Object> claimsMap)
	{
		Date now = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.HOUR, 1);
		
		Date expire = calendar.getTime();
		
		HashMap<String, Object> headerMap = new HashMap<>();
		headerMap.put("alg", "HS256");
		headerMap.put("typ", "JWT");
		
		return Jwts.builder()
				.header().add(headerMap).and()
				.issuer("oauth2")
				.subject("auth")
				.audience().add(id).and()
				.claims(claimsMap)
				.expiration(expire)
				.notBefore(now)
				.issuedAt(now)
				.id(id)
				.signWith(Keys.hmacShaKeyFor(AES_KEY.getBytes()))
				.compact();
	}
	
	/**
	 * JWT 요소 반환 함수
	 *
	 * @param jwt: [String] JWT
	 *
	 * @return [Jws<Claims>] JWT 요소
	 */
	public static Jws<Claims> openJwt(String jwt)
	{
		return Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(AES_KEY.getBytes()))
				.build()
				.parseSignedClaims(jwt);
	}
}