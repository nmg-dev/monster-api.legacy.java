package com.fsn.nmg.monster.datamodel;

import java.util.TreeMap;

public class AccessAccount {
	protected final String _access;
	protected String _id;
	protected TreeMap<String, Account> _accounts;
	
	public AccessAccount(String access, String id) {
		this._access = access;
		this._id = id;
		
		this._accounts = new TreeMap<String, Account>();
	}
	
	public void setId(String id) {
		if(this._id == null)
			this._id = id;
	}
	
	public String getId() {
		return this._id;
	}
	
	public String getAccessToken() {
		return this._access;
	}

	
}
