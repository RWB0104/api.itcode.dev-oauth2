package oauth.account.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import oauth.account.bean.ApiKeyBean;
import oauth.account.bean.UserInfoBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

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
		String profile_image = node.get("response").get("profile_image") == null ? "/oauth2/assets/images/logo.png" : node.get("response").get("profile_image").textValue();
		
		return new UserInfoBean(email, name, profile_image, MODULE_NAME);
	}
	
	/**
	 * 연동 해제 결과 반환 메서드
	 *
	 * @param access: [String] 접근 토큰
	 *
	 * @return [boolean] 연동 해제 결과
	 *
	 * @throws IOException 데이터 입출력 예외
	 * @throws ExecutionException 실행 예외
	 * @throws InterruptedException 인터럽트 예외
	 */
	@Override
	public boolean deleteInfo(String access) throws IOException, ExecutionException, InterruptedException
	{
		OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, getAccessTokenEndpoint());
		oAuthRequest.addQuerystringParameter("client_id", API_KEY);
		oAuthRequest.addQuerystringParameter("client_secret", SECRET_KEY);
		oAuthRequest.addQuerystringParameter("access_token", access);
		oAuthRequest.addQuerystringParameter("grant_type", "delete");
		oAuthRequest.addQuerystringParameter("service_provider", "NAVER");
		
		service.signRequest(access, oAuthRequest);
		
		Response response = service.execute(oAuthRequest);
		
		return response.isSuccessful();
	}
	
	/**
	 * 정보 제공 동의 갱신 URL 반환 메서드
	 *
	 * @param state: [String] 고유 상태값
	 *
	 * @return [String] 정보 제공 동의 갱신 URL
	 */
	@Override
	public String getUpdateAuthorizationUrl(String state)
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