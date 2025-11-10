package dev.itcode.oauth2.core.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Env 환경변수 DTO 클래스
 *
 * @author RWB
 * @since 2025.10.12 Sun 14:04:45
 */
@Component
@ConfigurationProperties("env")
@Setter
@Getter
public class EnvDto
{
	/**
	 * 프론트엔드 URL
	 */
	private String frontendUrl;
	
	/**
	 * CORS 허용 도메인
	 */
	private List<String> corsOrigins = new ArrayList<>();
	
	/**
	 * 프론트엔드 Origin 반환 메서드
	 *
	 * @return [String] 프론트엔드 Origin
	 */
	public String getFrontendOrigin()
	{
		// 프론트엔드 URL이 없을 경우 그냥 반환
		if (frontendUrl == null)
		{
			return null;
		}
		
		URI uri = URI.create(frontendUrl);
		
		String schema = uri.getScheme();
		String host = uri.getAuthority();
		
		return schema + "://" + host;
	}
}
