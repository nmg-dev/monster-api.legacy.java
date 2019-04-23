package com.fsn.nmg.monster.datamodel;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fsn.nmg.monster.AppConfigure;

/**
 * testing for AccessAccount class
 * @author yg.song@nextmediagroup.co.kr
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccessTest {

	/**
	 * test building AccessAccount
	 */
	@Test
	public void test01Create() {
		assertNotNull(new AccessAccount(null, null));
	}
	
	@Test
	public void test02InitIdSet() {
		final AccessAccount acc = new AccessAccount(null, null);
		assertNull(acc.getId());
		final String testId ="test-id";
		final String testIdDiff = testId + "-diff";
		
		acc.setId(testId);
		
		assertEquals(testId, acc.getId());
		
		acc.setId(testIdDiff);
		
		assertEquals(testId, acc.getId());
	}
	
	@Test
	public void test03GetAccess() {
		final String sampleAccess = AppConfigure.get().getProperty("FB_APP_ACCESS", "test");
		assertNotNull(sampleAccess);
		
		final AccessAccount ac1 = new AccessAccount(null, "1");
		final AccessAccount ac2 = new AccessAccount(sampleAccess, null);
		
		assertNull(ac1.getAccessToken());
		assertEquals(sampleAccess, ac2.getAccessToken());
	}

}
