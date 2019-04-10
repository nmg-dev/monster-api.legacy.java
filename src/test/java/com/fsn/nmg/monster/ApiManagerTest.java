package com.fsn.nmg.monster;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.TestCase;

public class ApiManagerTest extends TestCase {
	static final int maxThread = 3;
	static final int maxInterval = 5*1000;
	static final int maxUsage = 5;
	
	ApiManager manager = new ApiManager("test", 3, 5, 1000);
	
	protected void sampleLoad(int iter) {
		for(int i=0; i<iter; i++) {
			final String testload = String.format("testLoad%02d", i);
			manager.load(testload, testload);
		}
	}

	@Test
	public void testSetupManager() {
//		fail("Not yet implemented");
		assertNotNull("manager instance", manager);
	}
	
	@Test(timeout=20000)
	public void testCompleteThreads() {
		manager.clear();
		sampleLoad(maxThread);			
		try {
			for(int i=0; i<maxThread; i++) {
				manager.proceed(1);
			}
		} catch(Exception ex) {
			fail("task load failed");
		}

//		assertTrue(true);
		while(0<manager.countRunnings()) {
			try {
				Thread.sleep(100);
			} catch(InterruptedException ex) {
				break;
			}
		}
		
	}
	
	@Test(timeout=20000)
	public void testExceedingThreads() {
		manager.clear();
		final int exceeds = 5*maxThread+1;
		sampleLoad(exceeds);
		try {
			manager.proceed(1);
		} catch (RemoteApiException e) {
			e.printStackTrace();
		}
		
		
		while(0<manager.countRunnings()) {
			try {
				System.out.println(String.format("run countings %d", manager.countRunnings()));
				Thread.sleep(10);
				manager.proceed(2);
			}
			catch(RemoteApiException ex) {
				assertEquals(ex.exType, RemoteApiException.ExType.API_LIMIT_EXCEEDS);
			}
			catch(InterruptedException ex) {
				break;
			}
		}
	}

	

}
