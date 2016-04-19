package com.starterkit.bartoszzychal.restConnection.mapper.json;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.starterkit.bartoszzychal.restConnection.mapper.Mapper;

public class JSONMapper<T> implements Mapper<T> {
	private final static ObjectMapper mapper = new ObjectMapper();

	@Override
	public List<T> map2List(String objectsString, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(objectsString, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
	}

	@Override
	public String map2String(List<T> objects) throws JsonProcessingException  {
		return mapper.writeValueAsString(objects);
	}

	@Override
	public T map(String objectString, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(objectString, clazz);
	}

	@Override
	public String map(T object) throws JsonProcessingException  {
		return mapper.writeValueAsString(object);
	}
	
}
