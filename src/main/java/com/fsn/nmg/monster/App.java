package com.fsn.nmg.monster;

import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
	protected static final HashMap<String, ApiManager> managers = new HashMap<String, ApiManager>(); 

	
    public static void main( String[] args )
    {
    	managers.clear();
    	
    	managers.put("sample", new ApiManager("tester", 3, 100, 10000));
    	
    	
    }
}
