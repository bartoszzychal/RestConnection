package com.starterkit.bartoszzychal.restConnection;

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

import com.starterkit.bartoszzychal.restConnection.mapper.Mapper;

public class RestConnection<T> {

	private final Logger LOG = Logger.getLogger(getClass().getSimpleName());
    private final Class<T> clazz;

    public RestConnection(Class<T> clazz){
    	this.clazz = clazz;
    }
    
	public T postOne(URL url,Charset charset,T object) throws IOException {
		return Mapper.map(post(url,charset, Mapper.map(object)),clazz);
	}
	
	public List<T> postList(URL url,Charset charset, List<T> objects) throws IOException {
		return Mapper.map2List(post(url,charset, Mapper.map2String(objects)), clazz);
	}
	
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
	
	public void post(String json,Charset charset, HttpURLConnection connection) throws IOException {
		try(OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream(), charset)){
			output.write(json);			
		}
	}
	
	public T getOne(URL url,Charset charset) throws IOException{
		return Mapper.map(get(url , charset), clazz);
	}
	
	public List<T> getList(URL url, Charset charset) throws IOException{
		return Mapper.map2List(get(url , charset), clazz);
	}
	
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
