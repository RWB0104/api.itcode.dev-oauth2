package oauth.account.module;

import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import global.module.Util;
import oauth.account.bean.ApiKeyBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * 인증 모듈 추상 클래스
 *
 * @author RWB
 * @since 2021.09.29 Wed 23:30:47
 */
abstract public class AuthModule extends DefaultApi20
{
	protected OAuth20Service service;
	
	protected AuthModule(ServiceBuilderOAuth20 serviceBuilder)
	{
		service = serviceBuilder.build(this);
	}
	
	abstract protected String getUserInfoEndPoint();
	
	/**
	 * 인증 URL 반환 메서드
	 *
	 * @param state: [String] 고유 상태값
	 *
	 * @return [String] 인증 URL
	 */
	public String getAuthorizationUrl(String state)
	{
		return service.getAuthorizationUrl(state);
	}
	
	/**
	 * 접근 토큰 반환 함수
	 *
	 * @param code: [String] 인증 코드
	 *
	 * @return [OAuth2AccessToken] 접근 토큰
	 *
	 * @throws IOException 데이터 입출력 예외
	 * @throws ExecutionException 실행 예외
	 * @throws InterruptedException 인터럽트 예외
	 */
	public OAuth2AccessToken getAccessToken(String code) throws IOException, ExecutionException, InterruptedException
	{
		return service.getAccessToken(code);
	}
	
	/**
	 * 접근 토큰 갱신 및 반환 함수
	 *
	 * @param refresh: [String] 리프레쉬 코드
	 *
	 * @return [OAuth2AccessToken] 접근 토큰
	 *
	 * @throws IOException 데이터 입출력 예외
	 * @throws ExecutionException 실행 예외
	 * @throws InterruptedException 인터럽트 예외
	 */
	public OAuth2AccessToken getRefreshAccessToken(String refresh) throws IOException, ExecutionException, InterruptedException
	{
		return service.refreshAccessToken(refresh);
	}
	
	public Response getUserInfo(String access) throws IOException, ExecutionException, InterruptedException
	{
		OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, getUserInfoEndPoint());
		service.signRequest(access, oAuthRequest);
		
		return service.execute(oAuthRequest);
	}
	
	/**
	 * API 키 객체 반환 메서드
	 *
	 * @param platform: [String] 플랫폼
	 *
	 * @return [ApiKeyBean] API 키 객체
	 */
	protected static ApiKeyBean getApiKeyBean(String platform)
	{
		ApiKeyBean apiKeyBean;
		apiKeyBean = new ApiKeyBean();
		
		// API 키 획득 시도
		try
		{
			HashMap<String, String> map = Util.getProperties(platform);
			
			apiKeyBean.setApi(map.get("api"));
			apiKeyBean.setSecret(map.get("secret"));
			apiKeyBean.setCallback(map.get("callback"));
		}
		
		// 예외
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return apiKeyBean;
	}
}