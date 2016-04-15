package com.starterkit.bartoszzychal.restConnection;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public interface RestConnection<T> {
	T postOne(URL url,Charset charset,T object) throws Exception;
	List<T> postList(URL url,Charset charset, List<T> objects) throws Exception;
	String post(URL url,Charset charset, String json);
	void post(String objectsString,Charset charset, HttpURLConnection connection) throws Exception;
	
	T getOne(URL url,Charset charset) throws Exception;
	List<T> getList(URL url, Charset charset) throws Exception;
	String get(URL url, Charset charset);
	String get(HttpURLConnection connection, Charset charset) throws Exception;
}
