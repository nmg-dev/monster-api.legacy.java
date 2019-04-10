package com.fsn.nmg.monster;

import java.util.Map.Entry;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
	private static String APP_TITLE_FOR_TEST = "monster-api";
	private static String ENVKEY_APP_TITLE = "APP_TITLE";
	
	
    @org.junit.Test
    public void testEnvironment() {
    	for(Entry<String, String> env : System.getenv().entrySet()) {
    		System.out.println(String.format("%s = %s", env.getKey(), env.getValue()));
    	}
    	
    	assertEquals(APP_TITLE_FOR_TEST, System.getenv(ENVKEY_APP_TITLE));
    }
    
}
