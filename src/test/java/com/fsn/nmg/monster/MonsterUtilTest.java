package com.fsn.nmg.monster;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MonsterUtilTest {
	
	@Test
	public void testExecuteCommand(){
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
	public void testExecuteCommandCurl() {
		final String resp = MonsterUtils.executeCURL("https://graph.facebook.com", 10);
		assertNotNull(resp);
		assertTrue(resp.contains("\"error\""));
	}
	
	
	@Test(timeout=1500)
	public void testExecuteCommandCurlJson() {
		final JsonObject resp = MonsterUtils.executeCURLtoJSON("https://graph.facebook.com", 10).getAsJsonObject();
		
		assertNotNull(resp.get("error"));
		assertNotNull(resp.get("error").getAsJsonObject().get("message"));
	}
	
	@Test
	public void testExecuteCommandCurlErrors() {
		final String illegalUrlResp = MonsterUtils.executeCURL("ls -al", 10);
		assertTrue(illegalUrlResp.length()<=0);
		
		final JsonElement illegalJsonResp = MonsterUtils.executeCURLtoJSON("https://www.naver.com", 10);
		assertNull(illegalJsonResp);
	}

	

}
