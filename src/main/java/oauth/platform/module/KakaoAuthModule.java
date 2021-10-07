package oauth.platform.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import oauth.account.bean.ApiKeyBean;
import oauth.account.bean.UserInfoBean;
import oauth.account.module.AuthModule;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * 카카오 인증 모듈 클래스
 *
 * @author RWB
 * @since 2021.10.04 Mon 21:30:49
 */
public class KakaoAuthModule extends AuthModule
{
	private static final String MODULE_NAME = "kakao";
	
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
	
	private static final ServiceBuilderOAuth20 SERVICE_BUILDER = new ServiceBuilder(API_KEY).apiSecret(SECRET_KEY).callback(CALLBACK_URL).debug();
	
	private static final KakaoAuthModule INSTANCE = new KakaoAuthModule(SERVICE_BUILDER, MODULE_NAME);
	
	/**
	 * 생성자 메서드
	 *
	 * @param serviceBuilder: [ServiceBuilderOAuth20] API 서비스 빌더
	 */
	private KakaoAuthModule(ServiceBuilderOAuth20 serviceBuilder, String unique)
	{
		super(serviceBuilder, unique);
	}
	
	/**
	 * 인스턴스 반환 메서드
	 *
	 * @return [KakaoAuthModule] 인스턴스
	 */
	public static KakaoAuthModule getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * 접근 토큰 반환 메서드
	 *
	 * @param code: [String] 인증 코드
	 *
	 * @return [OAuth2AccessToken] 접근 토큰
	 *
	 * @throws IOException 데이터 입출력 예외
	 */
	@Override
	public OAuth2AccessToken getAccessToken(String code) throws IOException, ExecutionException, InterruptedException
	{
		AccessTokenRequestParams params = new AccessTokenRequestParams(code);
		params.addExtraParameter("client_id", API_KEY);
		params.addExtraParameter("client_secret", SECRET_KEY);
		
		return super.getAccessToken(params);
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
		
		String email = node.get("kakao_account").get("email") == null ? "미동의" : node.get("kakao_account").get("email").textValue();
		String name = node.get("kakao_account").get("profile").get("nickname") == null ? "미동의" : node.get("kakao_account").get("profile").get("nickname").textValue();
		String picture = node.get("kakao_account").get("profile").get("profile_image_url") == null ? "미동의" : node.get("kakao_account").get("profile").get("profile_image_url").textValue();
		
		return new UserInfoBean(email, name, picture, MODULE_NAME);
	}
	
	/**
	 * 접근 토큰 요청 URL 반환 메서드
	 *
	 * @return [String] 접근 토큰 요청 URL
	 */
	@Override
	public String getAccessTokenEndpoint()
	{
		return "https://kauth.kakao.com/oauth/token";
	}
	
	/**
	 * 인증 API 요청 URL 반환 메서드
	 *
	 * @return [String] 인증 API 요청 URL
	 */
	@Override
	protected String getAuthorizationBaseUrl()
	{
		return "https://kauth.kakao.com/oauth/authorize";
	}
	
	/**
	 * 사용자 정보 요청 URL 반환 메서드
	 *
	 * @return [String] 사용자 정보 요청 URL
	 */
	@Override
	protected String getUserInfoEndPoint()
	{
		return "https://kapi.kakao.com/v2/user/me";
	}
}