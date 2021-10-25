package global.module;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oauth.account.module.AuthModule;
import oauth.account.module.GithubAuthModule;
import oauth.account.module.GoogleAuthModule;
import oauth.account.module.KakaoAuthModule;
import oauth.account.module.NaverAuthModule;

/**
 * 프로세스 추상 클래스
 *
 * @author RWB
 * @since 2021.09.30 Thu 01:14:25
 */
abstract public class Process
{
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	/**
	 * 생성자 메서드
	 *
	 * @param request: [HttpServletRequest] HttpServletResponse 객체
	 * @param response: [HttpServletResponse] HttpServletResponse 객체
	 */
	protected Process(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
	}
	
	/**
	 * 인증 모듈 반환 메서드
	 *
	 * @param platform: [String] 플랫폼
	 *
	 * @return [AuthModule] AuthModule 객체
	 *
	 * @throws NullPointerException 유효하지 않은 플랫폼
	 */
	protected AuthModule getAuthModule(String platform) throws NullPointerException
	{
		return switch (platform)
				{
					case "naver" -> NaverAuthModule.getInstance();
					case "google" -> GoogleAuthModule.getInstance();
					case "kakao" -> KakaoAuthModule.getInstance();
					case "github" -> GithubAuthModule.getInstance();
					default -> throw new NullPointerException(Util.builder("'", platform, "' is invalid platform"));
				};
	}
}