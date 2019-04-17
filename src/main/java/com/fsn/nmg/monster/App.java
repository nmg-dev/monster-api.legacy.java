package com.fsn.nmg.monster;

import java.util.HashMap;

/**
 * @author yg.song@nextmediagroup.co.kr
 *
 */
public class App 
{
	private static final String envPath = "./.env";
	protected static final HashMap<String, ApiManager> managers = new HashMap<String, ApiManager>();
//	protected static final AppConfigure appConfig = new AppConfigure(); 

	
    public static void main( String[] args )
    {
    	managers.clear();
    	
    	managers.put("sample", new ApiManager("tester", 3, 100, 10000));
    	
    	
    }
}
