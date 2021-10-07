package oauth.platform.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
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
import java.util.concurrent.ExecutionException;

/**
 * GitHub 인증 모듈 클래스
 *
 * @author RWB
 * @since 2021.10.05 Tue 00:22:10
 */
public class GithubAuthModule extends AuthModule
{
	private static final String MODULE_NAME = "github";
	
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
	
	private static final ServiceBuilderOAuth20 SERVICE_BUILDER = new ServiceBuilder(API_KEY).apiSecret(SECRET_KEY).callback(CALLBACK_URL).defaultScope("https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile");
	
	private static final GithubAuthModule INSTANCE = new GithubAuthModule(SERVICE_BUILDER, MODULE_NAME);
	
	/**
	 * 생성자 메서드
	 *
	 * @param serviceBuilder: [ServiceBuilderOAuth20] API 서비스 빌더
	 * @param unique: [String] 유니크 값
	 */
	private GithubAuthModule(ServiceBuilderOAuth20 serviceBuilder, String unique)
	{
		super(serviceBuilder, unique);
	}
	
	/**
	 * 인스턴스 반환 메서드
	 *
	 * @return [GithubAuthModule] 인스턴스
	 */
	public static GithubAuthModule getInstance()
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
	public OAuth2AccessToken getAccessToken(String code) throws IOException
	{
		HashMap<String, String> params = new HashMap<>();
		params.put("client_id", API_KEY);
		params.put("client_secret", SECRET_KEY);
		params.put("redirect_uri", CALLBACK_URL);
		params.put("code", code);
		
		StringBuilder builder = new StringBuilder();
		
		for (Map.Entry<String, String> param : params.entrySet())
		{
			String pre = builder.length() == 0 ? "" : "&";
			
			builder.append(pre).append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
		}
		
		byte[] paramBytes = builder.toString().getBytes(StandardCharsets.UTF_8);
		
		URL url = new URL(getAccessTokenEndpoint());
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept", "application/json");
		connection.setDoOutput(true);
		connection.getOutputStream().write(paramBytes);
		
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
		
		String access_token = node.get("access_token") == null ? "미동의" : node.get("access_token").textValue();
		String token_type = node.get("token_type") == null ? "미동의" : node.get("token_type").textValue();
		String scope = node.get("scope") == null ? "미동의" : node.get("scope").textValue();
		
		return new OAuth2AccessToken(access_token, token_type, 0, null, scope, responseBuilder.toString());
	}
	
	/**
	 * 사용자 정보 응답 반환 메서드
	 *
	 * @param access: [String] 접근 토큰
	 *
	 * @return [Response] 사용자 정보 응답
	 *
	 * @throws IOException 데이터 입출력 예외
	 * @throws ExecutionException 실행 예외
	 * @throws InterruptedException 인터럽트 예외
	 */
	@Override
	public Response getUserInfo(String access) throws IOException, ExecutionException, InterruptedException
	{
		OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, getUserInfoEndPoint());
		oAuthRequest.addHeader("Authorization", Util.builder("token ", access));
		
		service.signRequest(access, oAuthRequest);
		
		return service.execute(oAuthRequest);
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
		
		String email = node.get("email").textValue();
		String name = node.get("name").textValue();
		String picture = node.get("avatar_url").textValue();
		
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
		return "https://github.com/login/oauth/access_token";
	}
	
	/**
	 * 인증 API 요청 URL 반환 메서드
	 *
	 * @return [String] 인증 API 요청 URL
	 */
	@Override
	protected String getAuthorizationBaseUrl()
	{
		return "https://github.com/login/oauth/authorize";
	}
	
	/**
	 * 사용자 정보 요청 URL 반환 메서드
	 *
	 * @return [String] 사용자 정보 요청 URL
	 */
	@Override
	protected String getUserInfoEndPoint()
	{
		return "https://api.github.com/users/RWB0104";
	}
}