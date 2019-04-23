package com.fsn.nmg.monster;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.function.Function;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MonsterUtilTest {
	
	@Test
	public void test01ExecuteCommand(){
		String resp;
		try {
			resp = MonsterUtils.executeCommand("curl -m 1 https://graph.facebook.com");
			assertNotNull(resp);
			assertTrue(0<resp.length());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	@Test(timeout=1500)
	public void test02ExecuteCommandCurl() {
		final String resp = MonsterUtils.executeCURL("https://graph.facebook.com", 10);
		assertNotNull(resp);
		assertTrue(resp.contains("\"error\""));
	}
	
	
	@Test(timeout=1500)
	public void test03ExecuteCommandCurlJson() {
		final JsonObject resp = MonsterUtils.executeCURLtoJSON("https://graph.facebook.com", 10).getAsJsonObject();
		
		assertNotNull(resp.get("error"));
		assertNotNull(resp.get("error").getAsJsonObject().get("message"));
	}
	
	@Test
	public void test04ExecuteCommandCurlErrors() {
		final String illegalUrlResp = MonsterUtils.executeCURL("ls -al", 10);
		assertTrue(illegalUrlResp.length()<=0);
		
		final JsonElement illegalJsonResp = MonsterUtils.executeCURLtoJSON("https://www.naver.com", 10);
		assertNull(illegalJsonResp);
	}

	@Test
	public void test05ParseJson() {
		final String exactJson = "{\"name\":\"hello\", \"values\": [1,true,\"false\",3.2]}";
		final String missingJson = "{asdf";
		
		final JsonElement parsed =MonsterUtils.parseJson(exactJson);
		assertNull(MonsterUtils.parseJson(missingJson));
		assertNotNull(parsed);
		assertNotNull(parsed.getAsJsonObject());
		final JsonObject obj = parsed.getAsJsonObject();
		assertTrue(obj.has("name"));
		assertTrue(obj.has("values"));
		
		final JsonArray values = obj.get("values").getAsJsonArray();
		assertEquals(1, values.get(0).getAsInt());
		assertEquals(true, values.get(1).getAsBoolean());
		assertEquals("false", values.get(2).getAsString());
		assertEquals(new Float(3.2), new Float(values.get(3).getAsFloat()));
	}
	
	@Test
	public void test06JsonTests() {
		final String exactJson = "{\"string\":\"value\", \"boolean\": true, \"int\": 1, \"float\": 2.5, \"array\": [1,2,3], \"object\": {\"id\": 0, \"name\": \"sample\", \"is_active\": false}}";
		final JsonObject obj = MonsterUtils.parseJson(exactJson).getAsJsonObject();
		
//		assertFalse(MonsterUtils.jsonBoolean(obj, "string"));
		assertNull(MonsterUtils.jsonInteger(obj, "string"));
		assertNull(MonsterUtils.jsonFloat(obj, "string"));
		assertNotNull(MonsterUtils.jsonString(obj, "string"));
		assertEquals("value", MonsterUtils.jsonString(obj, "string"));
		
		assertNotNull(MonsterUtils.jsonBoolean(obj, "boolean"));
		assertNull(MonsterUtils.jsonInteger(obj, "boolean"));
		assertNull(MonsterUtils.jsonFloat(obj, "boolean"));
//		assertNull(MonsterUtils.jsonString(obj, "boolean"));
		assertTrue(MonsterUtils.jsonBoolean(obj, "boolean"));
		
//		assertNull(MonsterUtils.jsonBoolean(obj, "int"));
		assertNotNull(MonsterUtils.jsonInteger(obj, "int"));
//		assertNull(MonsterUtils.jsonFloat(obj, "int"));
//		assertNull(MonsterUtils.jsonString(obj, "int"));
		assertTrue(1 == MonsterUtils.jsonInteger(obj, "int"));
		
//		assertNull(MonsterUtils.jsonBoolean(obj, "float"));
//		assertNull(MonsterUtils.jsonInteger(obj, "float"));
		assertNotNull(MonsterUtils.jsonFloat(obj, "float"));
//		assertNull(MonsterUtils.jsonString(obj, "float"));
		assertTrue(2.5 == MonsterUtils.jsonFloat(obj, "float"));
		
//		assertNull(MonsterUtils.jsonBoolean(obj, "float"));
//		assertNull(MonsterUtils.jsonInteger(obj, "float"));
		assertNotNull(MonsterUtils.jsonFloat(obj, "float"));
//		assertNull(MonsterUtils.jsonString(obj, "float"));
		assertTrue(2.5 == MonsterUtils.jsonFloat(obj, "float"));
	}

}
