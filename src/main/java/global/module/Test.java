package global.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class Test
{
	public static void main(String[] args) throws JsonProcessingException
	{
		HashMap<String, String> map = new HashMap<>();
		map.put("key", "asdfklasjdfl");
		map.put("aa", "Asdfkj3");
		
		ObjectMapper mapper = new ObjectMapper();
		
		System.out.println(mapper.writeValueAsString(map));
	}
}