package com.fsn.nmg.monster;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class AppConfigure extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2745337268832677797L;
	
	private static final String envPath = "./.env";
//	private static String ENVKEY_APP_TITLE = "APP_TITLE";
	private static String ENVKEY_CONFIG_LOADED = "APP_CONFIG_LOADED_@";
	
	private static AppConfigure _instance;
	
	public static AppConfigure get(String path) {
		if(_instance == null) _instance = new AppConfigure(path);
		return _instance;
	}
	
	public static AppConfigure get() {
		return get(envPath);
	}
	
	protected AppConfigure(String path) {
		initialize(path);
	}
	
	public boolean hasLoaded() {
		return containsKey(ENVKEY_CONFIG_LOADED);
	}
	
	protected void initialize(String path) {
		int retries = 5;
		while(0<--retries && !hasLoaded()) {			
			try {
				final FileReader reader = new FileReader(path);
				load(reader);
				put(ENVKEY_CONFIG_LOADED, System.currentTimeMillis());
			} catch(IOException ex) {
				System.out.println(ex);
				continue;
			}
		}
		System.out.println(String.format("%d %s", retries, this.toString()));
	}
	
	
}
