package oauth.platform.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import global.module.Util;
import oauth.account.bean.ApiKeyBean;
import oauth.account.bean.UserInfoBean;
import oauth.account.module.AuthModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
		
		String id = node.get("response").get("id").textValue();
		String email = node.get("response").get("email").textValue();
		String name = node.get("response").get("name").textValue();
		String profile_image = node.get("response").get("profile_image").textValue();
		
		return new UserInfoBean(id, email, name, profile_image, MODULE_NAME);
	}
	
	/**
	 * 재발급한 접근 토큰 반환 메서드
	 *
	 * @param refresh: [String] 리프레쉬 코드
	 *
	 * @return [OAuth2AccessToken] 접근 토큰
	 *
	 * @throws IOException 데이터 입출력 예외
	 */
	@Override
	public OAuth2AccessToken getRefreshAccessToken(String refresh) throws IOException
	{
		HashMap<String, String> params = new HashMap<>();
		params.put("client_id", API_KEY);
		params.put("client_secret", SECRET_KEY);
		params.put("refresh_token", refresh);
		
		StringBuilder builder = new StringBuilder();
		
		for (Map.Entry<String, String> param : params.entrySet())
		{
			builder.append("&").append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
		}
		
		URL url = new URL(Util.builder(getRefreshTokenEndpoint(), builder.toString()));
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
		
		StringBuilder responseBuilder = new StringBuilder();
		String temp;
		
		while ((temp = reader.readLine()) != null)
		{
			responseBuilder.append(temp);
		}
		
		reader.close();
		
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode node = mapper.readTree(responseBuilder.toString());
		
		String access_token = node.get("access_token").textValue();
		String token_type = node.get("token_type").textValue();
		int expires_in = node.get("expires_in").intValue();
		
		return new OAuth2AccessToken(access_token, token_type, expires_in, null, null, responseBuilder.toString());
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
	 * 접근 토큰 재발급 요청 URL 반환 메서드
	 *
	 * @return [String] 접근 토큰 재발급 요청 URL
	 */
	@Override
	public String getRefreshTokenEndpoint()
	{
		return "https://nid.naver.com/oauth2.0/token?grant_type=refresh_token";
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