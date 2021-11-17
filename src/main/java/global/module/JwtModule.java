package global.module;

import com.github.scribejava.core.base64.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
	private static final String AES_KEY = "JWT-TEST-KEY";
	
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
				.setHeader(headerMap)
				.setIssuer("oauth2")
				.setSubject("auth")
				.setAudience(id)
				.addClaims(claimsMap)
				.setExpiration(expire)
				.setNotBefore(now)
				.setIssuedAt(now)
				.setId(id)
				.signWith(SignatureAlgorithm.HS256, Base64.encode(AES_KEY.getBytes()))
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
		return Jwts.parser().setSigningKey(Base64.encode(AES_KEY.getBytes())).parseClaimsJws(jwt);
	}
}