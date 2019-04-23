package com.fsn.nmg.monster.service;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fsn.nmg.monster.datamodel.AccessAccount;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseServiceTest {

	@Test
	public void test01ToBuildAbstractService() {
		final String tick = String.format("%x", System.currentTimeMillis());
		final AccessAccount user = new AccessAccount(tick, tick);
		
		final AbstractService service = ApiService.SampleTest.serviceContext(user);
		assertNotNull(service);
		assertTrue(service instanceof ServiceTest);
	}
	
	
}
