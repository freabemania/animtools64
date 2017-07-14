package se.oxidev.animtools64;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonObjectMapper {

	private static ObjectMapper mapper;
	
	static {
		mapper = new ObjectMapper();
		
		// Instead of @JsonIgnoreProperties(ignoreUnknown = true)
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static ObjectMapper getObjectMapper() {
		return mapper;
	}
	
}
