package dev.itcode.oauth2.restful;

import com.epages.restdocs.apispec.Schema;
import dev.itcode.oauth2.api.base.dto.ApiResponseDto;
import dev.itcode.oauth2.api.me.controller.MeGetController;
import dev.itcode.oauth2.api.me.dto.MeDto;
import dev.itcode.oauth2.api.me.service.MeGetService;
import dev.itcode.oauth2.core.env.EnvDto;
import dev.itcode.oauth2.core.oauth2.Platform;
import dev.itcode.oauth2.core.token.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest(controllers = MeGetController.class)
@ExtendWith({ SpringExtension.class, RestDocumentationExtension.class })
@AutoConfigureRestDocs
@AutoConfigureWebTestClient
class MeGetControllerTest
{
	@Autowired
	private WebTestClient webTestClient;
	
	@MockitoBean
	private MeGetService meGetService;
	
	@MockitoBean
	private TokenProvider tokenProvider;
	
	@MockitoBean
	private EnvDto envDto;
	
	@BeforeEach
	void setup()
	{
		SecretKey key = Keys.hmacShaKeyFor(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
		
		HashMap<String, String> map = new HashMap<>();
		map.put("platform", Platform.GOOGLE.getPlatform());
		map.put("name", "tester");
		map.put("email", "test@example.com");
		map.put("picture", "http://");
		
		String token = Jwts.builder()
				.subject("test")
				.issuer("api.itcode.dev-oauth2-")
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 3600_000))
				.claims(map)
				.signWith(key)
				.compact();
		
		Jws<Claims> mockJws = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
		
		given(tokenProvider.decrypt(anyString())).willReturn(mockJws);
		given(envDto.getCorsOrigins()).willReturn(List.of("http://localhost:3000"));
	}
	
	@Test
	@DisplayName("내 정보 조회 API 문서화")
	@WithMockUser
	void getMeApi()
	{
		MeDto meDto = new MeDto(Platform.GOOGLE, "홍길동", "user@example.com", "https://s");
		ApiResponseDto<MeDto> response = ApiResponseDto.<MeDto>builder()
				.uuid(UUID.randomUUID().toString())
				.body(meDto)
				.path("/api/me")
				.build();
		
		when(meGetService.getMe(anyString())).thenReturn(Mono.just(response));
		
		webTestClient
				.get()
				.uri("/api/me")
				.header(HttpHeaders.AUTHORIZATION, "Bearer token")
				.header(HttpHeaders.REFERER, "http://localhost:3000")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(document("get-me",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(builder()
								.summary("내 정보 조회")
								.description("현재 로그인한 사용자의 프로필 정보를 반환합니다.")
								.responseSchema(Schema.schema(MeDto.class.getSimpleName()))
								.responseFields(
										fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID"),
										fieldWithPath("body.platform").type("enum").description("플랫폼")
												.attributes(key("enumValues").value(Platform.values())),
										fieldWithPath("body.name").type(JsonFieldType.STRING).description("이름"),
										fieldWithPath("body.email").type(JsonFieldType.STRING).description("이메일"),
										fieldWithPath("body.picture").type(JsonFieldType.STRING).description("이미지"),
										fieldWithPath("path").type(JsonFieldType.STRING).description("경로"),
										fieldWithPath("timestamp").type(JsonFieldType.NUMBER).description("타임스탬프"),
										fieldWithPath("title").type(JsonFieldType.STRING).optional().description("제목"),
										fieldWithPath("description").type(JsonFieldType.STRING).optional().description("부제목")
								)
								.requestHeaders(
										headerWithName("Authorization").description("인증 토큰 (Bearer)")
								)
								.build()
						)
				));
	}
}