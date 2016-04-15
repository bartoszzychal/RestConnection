package com.starterkit.bartoszzychal.restConnection.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.starterkit.bartoszzychal.restConnection.RestConnection;
import com.starterkit.bartoszzychal.restConnection.mapper.Mapper;
import com.starterkit.bartoszzychal.restConnection.mapper.json.JSONMapper;

public class JSONRestConnection<T> implements RestConnection<T>{

	private final Logger LOG = Logger.getLogger(getClass().getSimpleName());
	private final Mapper<T> mapper = new JSONMapper<>();
	
    private final Class<T> clazz;

    public JSONRestConnection(Class<T> clazz){
    	this.clazz = clazz;
    }
    
    @Override
	public T postOne(URL url,Charset charset,T object) throws Exception  {
		return mapper.map(post(url,charset, mapper.map(object)),clazz);
	}
    
    @Override
	public List<T> postList(URL url,Charset charset, List<T> objects) throws Exception {
		return mapper.map2List(post(url,charset, mapper.map2String(objects)), clazz);
	}
	
    @Override
	public String post(URL url,Charset charset, String json) {
		HttpURLConnection connection = null;
		String gotJson = null;
		try {
			connection = prepareConnection(url, "POST");
			post(json,charset,connection);
			LOG.info("POST JSON: "+ json);
			if(checkResponse(connection)){
				gotJson = get(connection, charset);
				LOG.info("GET JSON:" + gotJson);
			}else{
				LOG.warning("OBJECT NOT RESPONSE ");
			}
		} catch (IOException e) {
			LOG.warning(e.getMessage());
		} finally {
			connection.disconnect();
		}
		return gotJson;
	}
    
    @Override
	public void post(String json,Charset charset, HttpURLConnection connection) throws IOException  {
		try(OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream(), charset)){
			output.write(json);			
		}
	}
	
    @Override
	public T getOne(URL url,Charset charset) throws Exception {
		return mapper.map(get(url , charset), clazz);
	}
	
    @Override
	public List<T> getList(URL url, Charset charset) throws Exception {
		return mapper.map2List(get(url , charset), clazz);
	}
	
    @Override
	public String get(URL url, Charset charset){
		HttpURLConnection connection = null;
		String json = null;
		try {
			connection = prepareConnection(url, "GET");
			json = get(connection, charset);
			LOG.info("GET JSON: " + json);
		} catch (IOException e) {
			LOG.warning(e.getMessage());
		} finally {
			connection.disconnect();
		}
		return json;
	}
	
    @Override
	public String get(HttpURLConnection connection, Charset charset) throws UnsupportedEncodingException, IOException {
		String response = null;
		try(InputStreamReader input = new InputStreamReader(connection.getInputStream(),charset)){
			response = getResponseMassage(input);			
		}
		return response;
	}
	
	private String getResponseMassage(InputStreamReader input){
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(input)){
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			LOG.warning(e.getMessage());
		}
		return sb.toString();
	}

	private HttpURLConnection prepareConnection(URL url, String requestMethod) throws IOException, ProtocolException {
		HttpURLConnection connection;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestMethod(requestMethod);
		if("POST".equals(requestMethod)){
			connection.setDoOutput(true);			
		}else if("GET".equals(requestMethod)){
			connection.setDoInput(true);			
		}
		return connection;
	}
	
	private Boolean checkResponse(HttpURLConnection connection) throws IOException, UnsupportedEncodingException {
		int HttpResult = connection.getResponseCode();
		boolean response = false;
		if (HttpResult == HttpURLConnection.HTTP_OK) {
			response = true;
			LOG.info("HTTP RESPONSE" + HttpURLConnection.HTTP_OK);
		} else {
			response = false;
			LOG.warning("HTTP RESPONSE" + HttpURLConnection.HTTP_NO_CONTENT);
		}
		return response;
	}
}
