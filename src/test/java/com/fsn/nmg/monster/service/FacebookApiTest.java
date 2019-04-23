package com.fsn.nmg.monster.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fsn.nmg.monster.AppConfigure;
import com.fsn.nmg.monster.datamodel.AccessAccount;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FacebookApiTest {
	
	/**
	 * test access key should be updated from <a href="https://developers.facebook.com/tools/explorer/">https://developers.facebook.com/tools/explorer/</a>
	 * at /.env file
	 */
	private static final String ENVKEY_TEST_USERID = "FB_TEST_USER_ID";
	private static final String ENVKEY_TEST_ACCESS = "FB_TEST_ACCESS";

	@Test
	public void test01Configure() {
		assertNotNull(AppConfigure.get().getProperty("FB_CLIENT_ID"));
		assertNotNull(AppConfigure.get().getProperty("FB_CLIENT_SECRET"));
		assertNotNull(AppConfigure.get().getProperty(ENVKEY_TEST_ACCESS));
		assertNotNull(AppConfigure.get().getProperty(ENVKEY_TEST_USERID));
	}
	
	@Test
	public void test02MyAuthString() {
		final String idString = String.format("%x", System.currentTimeMillis());
//		final AccessAccount nullAccount = new AccessAccount("", null);
//		final AccessAccount idAccount = new AccessAccount("", idString);
		final ServiceFacebook nullFb = new ServiceFacebook(new AccessAccount(null, null));
		final ServiceFacebook idFb = new ServiceFacebook(new AccessAccount("", idString));
		
		assertEquals("me", nullFb.myAuthIdString());
		assertEquals(idString, idFb.myAuthIdString());
	}
	
	@Test
	public void test03APIWithErrorToken() {
		final Map<String,String> tokenInfo = ServiceFacebook.retrieveLongtermToken("");
		assertFalse(tokenInfo.containsKey("access_token"));
		assertTrue(tokenInfo.containsKey("error"));
	}
	
	
	@Test
	public void test04AccountRetrieval() {
		final String access = AppConfigure.get().getProperty(ENVKEY_TEST_ACCESS);
		final String userId = AppConfigure.get().getProperty(ENVKEY_TEST_USERID);
		final AccessAccount user = new AccessAccount(access, null);
		
		final ServiceFacebook fb = new ServiceFacebook(user);
		
		fb.retrieveUserInfo();
		assertEquals(userId, user.getId());
		assertTrue(0<fb._accounts.size());
	}
	
	@Test
	public void test05LongtermTokenExchange() {
		final String access = AppConfigure.get().getProperty(ENVKEY_TEST_ACCESS);
		final Map<String,String> tokenInfo = ServiceFacebook.retrieveLongtermToken(access);
		assertNotNull(tokenInfo);
		System.out.println(tokenInfo.toString());
		assertTrue(tokenInfo.containsKey("access_token"));
	}	
	
	@Test
	public void test06LongterTokenExchangedErrors() {
		final String invalidAccess = "test-access";
		final Map<String,String> tokenInfo = ServiceFacebook.retrieveLongtermToken(invalidAccess);
		assertFalse(tokenInfo.containsKey("access_token"));
	}
	
	
	@Test
	public void test07CreativeDataRequest() {
		final String access = AppConfigure.get().getProperty(ENVKEY_TEST_ACCESS);
		final AccessAccount user = new AccessAccount(access, null);
		final ServiceFacebook fb = new ServiceFacebook(user);
		// get user info first
		fb.retrieveUserInfo();
		
		// then creatives
		fb.retrieveCreativeData();
		assertTrue(true);
		
//		assertTrue(user.)
	}

}
