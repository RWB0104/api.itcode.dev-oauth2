package dev.itcode.oauth2.core.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
	private List<String> corsOrigins;
}
