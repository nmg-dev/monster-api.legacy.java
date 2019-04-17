package com.fsn.nmg.monster.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fsn.nmg.monster.datamodel.AccessAccount;

public class BaseServiceTest {

	@Test
	public void testToBuildAbstractService() {
		final String tick = String.format("%x", System.currentTimeMillis());
		final AccessAccount user = new AccessAccount(tick, tick);
		
		final AbstractService service = ApiService.SampleTest.serviceContext(user);
		assertNotNull(service);
		assertTrue(service instanceof ServiceTest);
	}
	
	
}
