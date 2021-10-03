package oauth.account.bean;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 정보 객체 함수
 *
 * @author RWB
 * @since 2021.10.02 Sat 00:36:55
 */
@Getter
@Setter
public class UserInfoBean
{
	// 아이디
	private String id;
	
	// 이메일
	private String email;
	
	// 이름
	private String name;
	
	// 프로필 이미지 URL
	private String profile;
	
	/**
	 * 객체 JsonObject 반환 메서드
	 *
	 * @return [JsonObject] JsonObject 객체
	 */
	public JsonObject toJson()
	{
		JsonObject object = new JsonObject();
		object.addProperty("id", id);
		object.addProperty("email", email);
		object.addProperty("name", name);
		object.addProperty("profile", profile);
		
		return object;
	}
}