package com.fsn.nmg.monster;


import org.junit.Test;

import junit.framework.TestCase;

public class AppConfigTest extends TestCase {

	@Test
	public void testConfigLoaded() {
		final AppConfigure config = AppConfigure.get();
		assertNotNull(config);
		assertTrue(config.hasLoaded());
	}
	
	@Test
	public void testConfigActivated() {
		final AppConfigure config = AppConfigure.get();
		assertEquals("monster-api", config.getProperty("APP_TITLE"));
	}
	
}
