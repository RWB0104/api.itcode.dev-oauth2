package dev.itcode.oauth2.common;

import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

/**
 * RESTDocs 테스트 클래스
 *
 * @author RWB
 * @since 2025.10.26 Sun 13:21:43
 */
@ExtendWith({ SpringExtension.class, RestDocumentationExtension.class })
@AutoConfigureRestDocs
@AutoConfigureWebTestClient
public class RestdocsTest
{
	/**
	 * 기본 경로
	 */
	protected final String basePath;
	
	/**
	 * 헤더 FieldDescriptor
	 */
	protected final HeaderDescriptor header = headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰 (Bearer)");
	
	/**
	 * 기본 경로 prefix
	 */
	private final String prefix = "/api";
	
	/**
	 * API용 FieldDescriptor 배열
	 */
	private final FieldDescriptor[] apiDescriptor = new FieldDescriptor[] {
			fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID"),
			fieldWithPath("path").type(JsonFieldType.STRING).description("경로"),
			fieldWithPath("timestamp").type(JsonFieldType.NUMBER).description("타임스탬프"),
			fieldWithPath("title").type(JsonFieldType.STRING).optional().description("제목"),
			fieldWithPath("description").type(JsonFieldType.STRING).optional().description("부제목")
	};
	
	@Autowired
	protected WebTestClient webTestClient;
	
	/**
	 * 생성자 메서드
	 */
	protected RestdocsTest()
	{
		basePath = prefix;
	}
	
	/**
	 * 생성자 메서드
	 *
	 * @param basePath (String) 기본 경로
	 */
	protected RestdocsTest(String basePath)
	{
		this.basePath = prefix + basePath;
	}
	
	/**
	 * 전체 경로 반환 메서드
	 *
	 * @param path (String) 경로
	 *
	 * @return (String) 전체 경로
	 */
	protected String getPath(String path)
	{
		return basePath + path;
	}
	
	/**
	 * API용 FieldDescriptor 배열 반환 메서드
	 *
	 * @param additional (FieldDescriptor...) 추가 FieldDescriptor
	 *
	 * @return (FieldDescriptor[]) FieldDescriptor 배열
	 */
	protected FieldDescriptor[] apiFieldWithPaths(FieldDescriptor... additional)
	{
		return Stream.concat(Arrays.stream(apiDescriptor), Arrays.stream(additional)).toArray(FieldDescriptor[]::new);
	}
	
	/**
	 * Schema 반환 메서드
	 *
	 * @param cls (Class) 클래스
	 *
	 * @return (Schema) Schema 객체
	 */
	protected Schema getSchema(Class<?> cls)
	{
		return Schema.schema(cls.getSimpleName());
	}
}
