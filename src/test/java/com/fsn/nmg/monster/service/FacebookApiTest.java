package com.fsn.nmg.monster.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fsn.nmg.monster.AppConfigure;
import com.fsn.nmg.monster.datamodel.AccessAccount;

public class FacebookApiTest {

	@Test
	public void testConfigure() {
		assertNotNull(AppConfigure.get().getProperty("FB_CLIENT_ID"));
		assertNotNull(AppConfigure.get().getProperty("FB_CLIENT_SECRET"));
		assertNotNull(AppConfigure.get().getProperty("FB_TEST_ACCESS"));
		assertNotNull(AppConfigure.get().getProperty("FB_TEST_ACCOUNT_ID"));
	}
	
	@Test
	public void testAccessContext() {
		final String access = AppConfigure.get().getProperty("FB_TEST_ACCESS");
		final AccessAccount user = new AccessAccount(access, null);
		
		final ServiceFacebook fb = new ServiceFacebook(user);
		
		assertNotNull(fb);
		assertNotNull(fb._context);
	}
	
	

}
