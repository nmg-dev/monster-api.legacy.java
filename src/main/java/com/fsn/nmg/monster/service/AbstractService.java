package com.fsn.nmg.monster.service;

import java.util.TreeMap;

import com.fsn.nmg.monster.AppConfigure;
import com.fsn.nmg.monster.datamodel.AccessAccount;
import com.fsn.nmg.monster.datamodel.Account;

abstract class AbstractService {
	protected AccessAccount _auth;
	protected TreeMap<String,Account> _accounts;
	
	protected AbstractService(AccessAccount auth) {
		this._auth = auth;
	}
	
	protected abstract void retrieveAccounts();
	protected abstract void retrieveAccountData();
	
	protected static AppConfigure _config = AppConfigure.get();
	
	protected static String getConfig(String key) {
		return _config.getProperty(key);
	}
}
