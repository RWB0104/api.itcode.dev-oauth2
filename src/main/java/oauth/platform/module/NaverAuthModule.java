package oauth.platform.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import oauth.account.bean.ApiKeyBean;
import oauth.account.bean.UserInfoBean;
import oauth.account.module.AuthModule;

import java.util.HashMap;

/**
 * Naver 인증 모듈 클래스
 *
 * @author RWB
 * @since 2021.09.29 Wed 23:45:49
 */
public class NaverAuthModule extends AuthModule
{
	private static final String MODULE_NAME = "naver";
	
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
	
	private static final ServiceBuilderOAuth20 SERVICE_BUILDER = new ServiceBuilder(API_KEY).apiSecret(SECRET_KEY).debug().callback(CALLBACK_URL);
	
	private static final NaverAuthModule INSTANCE = new NaverAuthModule(SERVICE_BUILDER, MODULE_NAME);
	
	/**
	 * 생성자 메서드
	 *
	 * @param serviceBuilder: [ServiceBuilderOAuth20] API 서비스 빌더
	 * @param unique: [String] 유니크 값
	 */
	private NaverAuthModule(ServiceBuilderOAuth20 serviceBuilder, String unique)
	{
		super(serviceBuilder, unique);
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
	 * 모듈 이름 반환 메서드
	 *
	 * @return [String] 모듈 이름
	 */
	public static String getModuleName()
	{
		return MODULE_NAME;
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
		
		String email = node.get("response").get("email") == null ? "미동의" : node.get("response").get("email").textValue();
		String name = node.get("response").get("name") == null ? "미동의" : node.get("response").get("name").textValue();
		String profile_image = node.get("response").get("profile_image") == null ? "미동의" : node.get("response").get("profile_image").textValue();
		
		return new UserInfoBean(email, name, profile_image, MODULE_NAME);
	}
	
	@Override
	public boolean deleteInfo(String access)
	{
		return false;
	}
	
	@Override
	public String getReAuthorizationUrl(String state)
	{
		HashMap<String, String> params = new HashMap<>();
		params.put("state", state);
		params.put("auth_type", "reprompt");
		
		return service.getAuthorizationUrl(params);
	}
	
	/**
	 * 접근 토큰 요청 URL 반환 메서드
	 *
	 * @return [String] 접근 토큰 요청 URL
	 */
	@Override
	public String getAccessTokenEndpoint()
	{
		return "https://nid.naver.com/oauth2.0/token";
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