package dev.itcode.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OAuth2 프로젝트 클래스
 *
 * @author RWB
 * @since 2025.09.07 Sun 17:54:26
 */
@SpringBootApplication
public class Oauth2Application
{
	/**
	 * 메인 메서드
	 *
	 * @param args (String[]) 파라미터
	 */
	public static void main(String[] args)
	{
		SpringApplication.run(Oauth2Application.class, args);
	}
}
