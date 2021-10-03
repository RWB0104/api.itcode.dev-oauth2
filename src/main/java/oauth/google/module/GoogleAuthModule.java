package oauth.google.module;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import oauth.account.bean.ApiKeyBean;
import oauth.account.module.AuthModule;

/**
 * Google 인증 모듈 클래스
 *
 * @author RWB
 * @since 2021.09.29 Wed 23:45:27
 */
public class GoogleAuthModule extends AuthModule
{
	private static final String API_KEY;
	private static final String SECRET_KEY;
	private static final String CALLBACK_URL;
	
	static
	{
		ApiKeyBean apiKeyBean = getApiKeyBean("google");
		
		API_KEY = apiKeyBean.getApi();
		SECRET_KEY = apiKeyBean.getSecret();
		CALLBACK_URL = apiKeyBean.getCallback();
	}
	
	private static final ServiceBuilderOAuth20 SERVICE_BUILDER = new ServiceBuilder(API_KEY).apiSecret(SECRET_KEY).callback(CALLBACK_URL).defaultScope("https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile");
	
	private static final GoogleAuthModule INSTANCE = new GoogleAuthModule(SERVICE_BUILDER);
	
	/**
	 * 생성자 메서드
	 *
	 * @param serviceBuilder: [ServiceBuilderOAuth20] API 서비스 빌더
	 */
	private GoogleAuthModule(ServiceBuilderOAuth20 serviceBuilder)
	{
		super(serviceBuilder);
	}
	
	/**
	 * 인스턴스 반환 메서드
	 *
	 * @return [GoogleAuthModule] 인스턴스
	 */
	public static GoogleAuthModule getInstance()
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
		return "https://accounts.google.com/o/oauth2/token";
	}
	
	/**
	 * 인증 API 요청 URL 반환 메서드
	 *
	 * @return [String] 인증 API 요청 URL
	 */
	@Override
	protected String getAuthorizationBaseUrl()
	{
		return "https://accounts.google.com/o/oauth2/auth";
	}
	
	/**
	 * 사용자 정보 요청 URL 반환 메서드
	 *
	 * @return [String] 사용자 정보 요청 URL
	 */
	@Override
	protected String getUserInfoEndPoint()
	{
		return "https://www.googleapis.com/oauth2/v2/userinfo";
	}
}