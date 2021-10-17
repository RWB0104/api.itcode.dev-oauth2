package oauth.account.process;

import global.module.Process;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;

/**
 * 계정 GET 프로세스 클래스
 *
 * @author RWB
 * @since 2021.10.02 Sat 00:53:52
 */
public class AccountDeleteProcess extends Process
{
	/**
	 * 생성자 메서드
	 *
	 * @param request: [HttpServletRequest] HttpServletRequest 객체
	 * @param response: [HttpServletResponse] HttpServletResponse 객체
	 */
	public AccountDeleteProcess(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
	}
	
	/**
	 * 연동 해제 응답 반환 메서드
	 *
	 * @param platform: [String] 플랫폼
	 * @param accessCookie: [String] 접근 토큰 쿠키
	 *
	 * @return [Response] 응답 객체
	 */
	public Response deleteInfo(String platform, String accessCookie)
	{
		return null;
	}
}