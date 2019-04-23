package com.fsn.nmg.monster;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import junit.framework.TestCase;

/**
 * testing for AppConfigure class
 * @author yg.song@nextmediagroup.co.kr
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppConfigTest extends TestCase {

	/**
	 * testing App configure loaded and values
	 */
	@Test
	public void test01ConfigLoaded() {
		final AppConfigure config = AppConfigure.get();
		assertNotNull(config);
		assertTrue(config.hasLoaded());
	}
	
	/**
	 * testing configure activated
	 */
	@Test
	public void test02ConfigActivated() {
		final AppConfigure config = AppConfigure.get();
		assertEquals("monster-api", config.getProperty("APP_TITLE"));
	}
	
}
