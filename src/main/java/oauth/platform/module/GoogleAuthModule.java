package oauth.platform.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import oauth.account.bean.ApiKeyBean;
import oauth.account.bean.UserInfoBean;
import oauth.account.module.AuthModule;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Google 인증 모듈 클래스
 *
 * @author RWB
 * @since 2021.09.29 Wed 23:45:27
 */
public class GoogleAuthModule extends AuthModule
{
	private static final String MODULE_NAME = "google";
	
	private static final String API_KEY;
	private static final String SECRET_KEY;
	private static final String CALLBACK_URL;
	
	static
	{
		ApiKeyBean apiKeyBean = getApiKeyBean(MODULE_NAME);
		
		API_KEY = apiKeyBean.getApi();
		SECRET_KEY = apiKeyBean.getSecret();
		CALLBACK_URL = apiKeyBean.getCallback();
	}
	
	private static final ServiceBuilderOAuth20 SERVICE_BUILDER = new ServiceBuilder(API_KEY).apiSecret(SECRET_KEY).callback(CALLBACK_URL).defaultScope("https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile").debug();
	
	private static final GoogleAuthModule INSTANCE = new GoogleAuthModule(SERVICE_BUILDER, MODULE_NAME);
	
	/**
	 * 생성자 메서드
	 *
	 * @param serviceBuilder: [ServiceBuilderOAuth20] API 서비스 빌더
	 * @param unique: [String] 유니크 값
	 */
	private GoogleAuthModule(ServiceBuilderOAuth20 serviceBuilder, String unique)
	{
		super(serviceBuilder, unique);
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
	 * 유저 정보 객체 반환 메서드
	 *
	 * @param body: [String] OAuth 응답 내용
	 *
	 * @return [UserInfoBean] 유저 정보 객체
	 *
	 * @throws JsonProcessingException JSON 파싱 예외
	 */
	@Override
	public UserInfoBean getUserInfoBean(String body) throws JsonProcessingException
	{
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode node = mapper.readTree(body);
		
		String email = node.get("email") == null ? "미동의" : node.get("email").textValue();
		String name = node.get("name") == null ? "미동의" : node.get("name").textValue();
		String picture = node.get("picture") == null ? "/oauth2/assets/images/logo.png" : node.get("picture").textValue();
		
		return new UserInfoBean(email, name, picture, MODULE_NAME);
	}
	
	@Override
	public boolean deleteInfo(String access) throws IOException, ExecutionException, InterruptedException
	{
		OAuthRequest oAuthRequest = new OAuthRequest(Verb.POST, "https://oauth2.googleapis.com/revoke");
		oAuthRequest.addBodyParameter("token", access);
		
		service.signRequest(access, oAuthRequest);
		
		return service.execute(oAuthRequest).isSuccessful();
	}
	
	@Override
	public String getReAuthorizationUrl(String state)
	{
		return null;
	}
	
	/**
	 * 접근 토큰 요청 URL 반환 메서드
	 *
	 * @return [String] 접근 토큰 요청 URL
	 */
	@Override
	public String getAccessTokenEndpoint()
	{
		return "https://oauth2.googleapis.com/token";
	}
	
	/**
	 * 인증 API 요청 URL 반환 메서드
	 *
	 * @return [String] 인증 API 요청 URL
	 */
	@Override
	protected String getAuthorizationBaseUrl()
	{
		return "https://accounts.google.com/o/oauth2/v2/auth?access_type=offline&prompt=consent";
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