package com.starterkit.bartoszzychal.restConnection.mapper;

import java.util.List;

public interface Mapper<T> {
	public List<T> map2List(String objectsString, Class<T> clazz) throws Exception;

	public String map2String(List<T> objects) throws Exception;

	public T map(String objectsString, Class<T> clazz)throws Exception;

	public String map(T object) throws Exception;
}
