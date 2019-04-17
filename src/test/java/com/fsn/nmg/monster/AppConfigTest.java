package com.fsn.nmg.monster;


import org.junit.Test;

import junit.framework.TestCase;

/**
 * testing for AppConfigure class
 * @author yg.song@nextmediagroup.co.kr
 *
 */
public class AppConfigTest extends TestCase {

	/**
	 * testing App configure loaded and values
	 */
	@Test
	public void testConfigLoaded() {
		final AppConfigure config = AppConfigure.get();
		assertNotNull(config);
		assertTrue(config.hasLoaded());
	}
	
	/**
	 * testing configure activated
	 */
	@Test
	public void testConfigActivated() {
		final AppConfigure config = AppConfigure.get();
		assertEquals("monster-api", config.getProperty("APP_TITLE"));
	}
	
}
