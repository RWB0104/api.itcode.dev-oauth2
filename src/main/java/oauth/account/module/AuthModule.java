package oauth.account.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.OAuth20Service;
import global.module.Util;
import oauth.account.bean.ApiKeyBean;
import oauth.account.bean.UserInfoBean;

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
 * 인증 모듈 추상 클래스
 *
 * @author RWB
 * @since 2021.09.29 Wed 23:30:47
 */
abstract public class AuthModule extends DefaultApi20
{
	protected OAuth20Service service;
	
	/**
	 * 생성자 메서드
	 *
	 * @param serviceBuilder: [ServiceBuilderOAuth20] API 서비스 빌더
	 */
	protected AuthModule(ServiceBuilderOAuth20 serviceBuilder)
	{
		service = serviceBuilder.build(this);
	}
	
	abstract protected String getUserInfoEndPoint();
	
	abstract public UserInfoBean getUserInfoBean(String body) throws JsonProcessingException;
	
	abstract public boolean deleteInfo(String access) throws IOException, ExecutionException, InterruptedException;
	
	abstract public String getUpdateAuthorizationUrl(String state);
	
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
	 * 접근 토큰 반환 메서드
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
	 * 접근 토큰 반환 메서드
	 *
	 * @param params: [AccessTokenRequestParams] AccessTokenRequestParams 객체
	 *
	 * @return [OAuth2AccessToken] 접근 토큰
	 *
	 * @throws IOException 데이터 입출력 예외
	 * @throws ExecutionException 실행 예외
	 * @throws InterruptedException 인터럽트 예외
	 */
	public OAuth2AccessToken getAccessToken(AccessTokenRequestParams params) throws IOException, ExecutionException, InterruptedException
	{
		return service.getAccessToken(params);
	}
	
	/**
	 * 접근 토큰 갱신 및 반환 메서드
	 *
	 * @param refresh: [String] 리프레쉬 토큰
	 *
	 * @return [OAuth2AccessToken] 접근 토큰
	 *
	 * @throws IOException 데이터 입출력 예외
	 */
	public OAuth2AccessToken getRefreshAccessToken(String refresh) throws IOException
	{
		HashMap<String, String> params = new HashMap<>();
		params.put("client_id", service.getApiKey());
		params.put("client_secret", service.getApiSecret());
		params.put("refresh_token", refresh);
		
		StringBuilder builder = new StringBuilder();
		
		for (Map.Entry<String, String> param : params.entrySet())
		{
			builder.append("&").append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
		}
		
		byte[] paramBytes = builder.toString().getBytes(StandardCharsets.UTF_8);
		
		URL url = new URL(getRefreshTokenEndpoint());
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
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
		
		String access_token = node.get("access_token").textValue();
		String token_type = node.get("token_type").textValue();
		int expires_in = node.get("expires_in").intValue();
		
		return new OAuth2AccessToken(access_token, token_type, expires_in, refresh, null, responseBuilder.toString());
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
	public Response getUserInfo(String access) throws IOException, ExecutionException, InterruptedException
	{
		OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, getUserInfoEndPoint());
		service.signRequest(access, oAuthRequest);
		
		return service.execute(oAuthRequest);
	}
	
	/**
	 * 접근 토큰 재발급 요청 URL 반환 메서드
	 *
	 * @return [String] 접근 토큰 재발급 요청 URL
	 */
	@Override
	public String getRefreshTokenEndpoint()
	{
		return Util.builder(getAccessTokenEndpoint(), "?grant_type=refresh_token");
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
		ApiKeyBean apiKeyBean = new ApiKeyBean();
		
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