package oauth.naver.module;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import oauth.account.bean.ApiKeyBean;
import oauth.account.module.AuthModule;

/**
 * Naver 인증 모듈 클래스
 *
 * @author RWB
 * @since 2021.09.29 Wed 23:45:49
 */
public class NaverAuthModule extends AuthModule
{
	private static final String API_KEY;
	private static final String SECRET_KEY;
	private static final String CALLBACK_URL;
	
	static
	{
		ApiKeyBean apiKeyBean = getApiKeyBean("naver");
		
		API_KEY = apiKeyBean.getApi();
		SECRET_KEY = apiKeyBean.getSecret();
		CALLBACK_URL = apiKeyBean.getCallback();
	}
	
	private static final ServiceBuilderOAuth20 SERVICE_BUILDER = new ServiceBuilder(API_KEY).apiSecret(SECRET_KEY).callback(CALLBACK_URL);
	
	private static final NaverAuthModule INSTANCE = new NaverAuthModule(SERVICE_BUILDER);
	
	/**
	 * 생성자 메서드
	 *
	 * @param serviceBuilder: [ServiceBuilderOAuth20] API 서비스 빌더
	 */
	private NaverAuthModule(ServiceBuilderOAuth20 serviceBuilder)
	{
		super(serviceBuilder);
	}
	
	/**
	 * 인스턴스 반환 메서드
	 *
	 * @return [NaverAuthModule] 인스턴스
	 */
	public static NaverAuthModule getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * 접근 토큰 요청 URL 반환 메서드
	 *
	 * @return [String] 접근 토큰 요청 URL
	 */
	@Override
	public String getAccessTokenEndpoint()
	{
		return "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code";
	}
	
	/**
	 * 인증 API 요청 URL 반환 메서드
	 *
	 * @return [String] 인증 API 요청 URL
	 */
	@Override
	protected String getAuthorizationBaseUrl()
	{
		return "https://nid.naver.com/oauth2.0/authorize";
	}
	
	/**
	 * 사용자 정보 요청 URL 반환 메서드
	 *
	 * @return [String] 사용자 정보 요청 URL
	 */
	@Override
	protected String getUserInfoEndPoint()
	{
		return "https://openapi.naver.com/v1/nid/me";
	}
}