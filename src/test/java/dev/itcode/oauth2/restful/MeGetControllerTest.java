package dev.itcode.oauth2.restful;

import dev.itcode.oauth2.api.me.controller.MeGetController;
import dev.itcode.oauth2.api.me.dto.MeDto;
import dev.itcode.oauth2.common.RestdocsTest;
import dev.itcode.oauth2.core.oauth2.Platform;
import dev.itcode.oauth2.mock.EnvDtoMockConfig;
import dev.itcode.oauth2.mock.MeGetServiceMockConfig;
import dev.itcode.oauth2.mock.TokenProviderMockConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

/**
 * 내 정보 GET 컨트롤러 테스트
 *
 * @author RWB
 * @since 2025.10.27 Mon 01:23:25
 */
@WebFluxTest(controllers = MeGetController.class)
@Import({ MeGetServiceMockConfig.class, TokenProviderMockConfig.class, EnvDtoMockConfig.class })
class MeGetControllerTest extends RestdocsTest
{
	/**
	 * 생성자 메서드
	 */
	public MeGetControllerTest()
	{
		super("/me");
	}
	
	/**
	 * 내 정보 조회 API 테스트 메서드
	 */
	@Test
	@WithMockUser
	@DisplayName("내 정보 조회 API 테스트")
	void getMeApiTest()
	{
		webTestClient
				.get()
				.uri(basePath)
				.header(HttpHeaders.AUTHORIZATION, "Bearer token")
				.header(HttpHeaders.REFERER, referer)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(document("get-me-api",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(builder()
								.summary("내 정보 조회")
								.description("현재 로그인한 사용자의 프로필 정보를 반환합니다.")
								.tag("MeController")
								.responseSchema(getSchema(MeDto.class))
								.responseFields(
										apiFieldWithPaths(
												JsonFieldType.OBJECT,
												fieldWithPath("body.platform").type("enum").description("플랫폼")
														.attributes(key("enumValues").value(Platform.values())),
												fieldWithPath("body.name").type(JsonFieldType.STRING).description("이름"),
												fieldWithPath("body.email").type(JsonFieldType.STRING).description("이메일"),
												fieldWithPath("body.picture").type(JsonFieldType.STRING).description("이미지")
										)
								)
								.requestHeaders(
										header
								)
								.build()
						)
				));
	}
	
	/**
	 * 유효성 결과 조회 API 테스트 메서드
	 */
	@Test
	@WithMockUser
	@DisplayName("유효성 결과 조회 API 테스트")
	void getMeValidateApiTest()
	{
		webTestClient
				.get()
				.uri(getPath("/validate"))
				.header(HttpHeaders.AUTHORIZATION, "Bearer token")
				.header(HttpHeaders.REFERER, referer)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(document("get-me-validate-api",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(builder()
								.summary("유효성 결과 조회")
								.description("토큰의 유효성 검증 결과를 반환합니다.")
								.tag("MeController")
								.responseSchema(getSchema(Boolean.class))
								.responseFields(
										apiFieldWithPaths(JsonFieldType.BOOLEAN)
								)
								.requestHeaders(
										header
								)
								.build()
						)
				));
	}
}