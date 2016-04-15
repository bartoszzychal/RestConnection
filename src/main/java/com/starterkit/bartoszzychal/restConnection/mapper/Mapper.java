package com.starterkit.bartoszzychal.restConnection.mapper;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper {
	private final static ObjectMapper mapper = new ObjectMapper();

	public final static <T> List<T> map2List(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
	}
	
	public final static <T> String map2String(List<T> object) throws JsonProcessingException{
		return mapper.writeValueAsString(object);
	}
	
	public final static <T> T map(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, clazz);
	}
		
	public final static <T> String map(T object) throws JsonProcessingException{
		return mapper.writeValueAsString(object);
	}
	
}
