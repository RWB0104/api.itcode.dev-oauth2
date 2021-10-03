package global.bean;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 응답 객체 클래스
 *
 * @param <T> 제네릭 클래스
 *
 * @author RWB
 * @since 2021.09.30 Thu 01:24:00
 */
@Getter
@Setter
public class ResponseBean<T>
{
	// 결과
	private boolean flag;
	
	// 제목
	private String title;
	
	// 메세지
	private String message;
	
	// 내용
	private T body;
	
	/**
	 * 객체 JsonObject 반환 메서드
	 *
	 * @return [JsonObject] JsonObject 객체
	 */
	public JsonObject toJson()
	{
		JsonObject object = new JsonObject();
		
		// JsonElement 객체일 경우
		if (body instanceof JsonElement)
		{
			object.addProperty("flag", flag);
			object.addProperty("title", title);
			object.addProperty("message", message);
			object.add("body", (JsonElement) body);
		}
		
		// Boolean 객체일 경우
		else if (body instanceof Boolean)
		{
			object.addProperty("flag", flag);
			object.addProperty("title", title);
			object.addProperty("message", message);
			object.addProperty("body", (Boolean) body);
		}
		
		// 문자 객체일 경우
		else if (body instanceof Character)
		{
			object.addProperty("flag", flag);
			object.addProperty("title", title);
			object.addProperty("message", message);
			object.addProperty("body", (Character) body);
		}
		
		// 숫자 객체일 경우
		else if (body instanceof Number)
		{
			object.addProperty("flag", flag);
			object.addProperty("title", title);
			object.addProperty("message", message);
			object.addProperty("body", (Number) body);
		}
		
		// 문자열 객체일 경우
		else if (body instanceof String)
		{
			object.addProperty("flag", flag);
			object.addProperty("title", title);
			object.addProperty("message", message);
			object.addProperty("body", (String) body);
		}
		
		// 어느 타입에도 해당되지 않을 경우
		else
		{
			object.addProperty("flag", flag);
			object.addProperty("title", title);
			object.addProperty("message", message);
			object.addProperty("body", body.toString());
		}
		
		return object;
	}
	
	/**
	 * 객체 문자열 반환 메서드
	 *
	 * @return [String] 객체 문자열
	 */
	@Override
	public String toString()
	{
		return new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create().toJson(toJson());
	}
}