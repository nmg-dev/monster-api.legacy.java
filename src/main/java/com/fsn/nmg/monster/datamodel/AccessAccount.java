package com.fsn.nmg.monster.datamodel;

import java.util.TreeMap;

/**
 * @author yg.song@nextmediagroup.co.kr
 *
 */
public class AccessAccount {
	protected final String _access;
	protected String _id;
	protected TreeMap<String, Account> _accounts;
	
	/**
	 * @param access
	 * @param id
	 */
	public AccessAccount(String access, String id) {
		this._access = access;
		this._id = id;
		
		this._accounts = new TreeMap<String, Account>();
	}
	
	/**
	 * @param id
	 */
	public void setId(String id) {
		if(this._id == null)
			this._id = id;
	}
	
	/**
	 * @return
	 */
	public String getId() {
		return this._id;
	}
	
	/**
	 * @return
	 */
	public String getAccessToken() {
		return this._access;
	}

	
}
