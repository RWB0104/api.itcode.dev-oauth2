package oauth.account.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 로그인 응답 객체 클래스
 *
 * @author RWB
 * @since 2021.10.04 Mon 05:33:49
 */
@Getter
@Setter
@RequiredArgsConstructor
public class LoginResponseBean
{
	// 인증 코드
	private String code;
	
	// 상태 고유값
	private String state;
	
	/**
	 * URL 복호화된 인증 코드 반환 메서드
	 *
	 * @return [String] URL 복호화된 인증 코드
	 */
	public String getCode()
	{
		return URLDecoder.decode(code, StandardCharsets.UTF_8);
	}
}