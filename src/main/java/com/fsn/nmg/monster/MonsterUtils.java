package com.fsn.nmg.monster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * Monster functional utilities
 * @author yg.song@nextmediagroup.co.kr
 */
public class MonsterUtils {
	private static JsonParser _jsonParser = new JsonParser();
	public static String executeCommand(String command) throws IOException,InterruptedException {
		final Runtime runs = Runtime.getRuntime();
		final Process proc = runs.exec(command);
		final InputStreamReader reader = new InputStreamReader(proc.getInputStream());
		final BufferedReader bf = new BufferedReader(reader);
		final StringBuffer buffer = new StringBuffer();
		
		// wait for process to be completed
		while(proc.isAlive()) {
			Thread.sleep(0);
			if(bf.ready())
				buffer.append(bf.readLine());
		}
		// close subprocess
		proc.destroy();
			
		if(bf.ready())
			buffer.append(bf.readLine());
		
		// closing
		bf.close();
		reader.close();
		
		
		return buffer.toString();
	}
	
	public static String executeCURL(String url, int timeout) {
		try {
			final String command = String.format("curl -g -m %d \"%s\"", timeout, url);
			return executeCommand(command);
		} catch(IOException ex) {
			return null;
		} catch(InterruptedException ex) {
			return null;
		}
	}
	
	public static String executeCURLPost(String url, String[] values, int timeout) {
		try {
			final StringBuffer params = new StringBuffer();
			for(String val : values) {
				params.append(String.format(" -F '%s'", val));
			}
			final String command = String.format("curl -m %d %s \"%s\"", timeout, params.toString(), url);
			System.out.println(command);
			return executeCommand(command);
		} catch(IOException ex) {
			return null;
		} catch(InterruptedException ex) {
			return null;
		}
	}
	
	public static JsonElement parseJson(String json) {
		try {
			return _jsonParser.parse(json);
		} catch(JsonParseException ex) {
			return null;
		} 
	}
	
	public static String jsonString(JsonObject obj, String key) {
		return jsonString(obj, key, null);
	}
	
	public static String jsonString(JsonObject obj, String key, String defValue) {
		try {
			return obj.get(key).getAsString();
		} catch(RuntimeException ex) {
			return defValue;
		}
	}
	
	public static Boolean jsonBoolean(JsonObject obj, String key) {
		return jsonBoolean(obj, key, null);
	}
	
	public static Boolean jsonBoolean(JsonObject obj, String key, Boolean defValue) {
		try {
			return obj.get(key).getAsBoolean();
		} catch(RuntimeException ex) {
			return defValue;
		}
	}
	
	public static Integer jsonInteger(JsonObject obj, String key) {
		return jsonInteger(obj, key, null);
	}
	
	public static Integer jsonInteger(JsonObject obj, String key, Integer defValue) {
		try {
			return obj.get(key).getAsInt();
		} catch(RuntimeException ex) {
			return defValue;
		}
	}
	
	public static Double jsonFloat(JsonObject obj, String key) {
		return jsonFloat(obj, key, null);
	}
	public static Double jsonFloat(JsonObject obj, String key, Double defValue) {
		try {
			return obj.get(key).getAsDouble();
		} catch(RuntimeException ex) {
			return defValue;
		}
	}
	
	public static JsonElement executeCURLtoJSON(String url, int timeout) {
		final String resp = executeCURL(url, timeout);
		return parseJson(resp);
		
	}
	

}
