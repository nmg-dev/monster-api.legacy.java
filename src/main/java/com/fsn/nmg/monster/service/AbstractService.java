package com.fsn.nmg.monster.service;

import java.util.Set;
import java.util.TreeMap;

import com.fsn.nmg.monster.AppConfigure;
import com.fsn.nmg.monster.datamodel.AccessAccount;
import com.fsn.nmg.monster.datamodel.Account;


/**
 * @author yg.song@nextmediagroup.co.kr
 *
 */
abstract class AbstractService {
	protected AccessAccount _auth;
	protected TreeMap<String,Account> _accounts;
	
	/**
	 * @param auth
	 */
	protected AbstractService(AccessAccount auth) {
		this._auth = auth;
	}
	
	public Set<String> accountIds() {
		return _accounts.keySet();
	}
	
	public Account getAccount(String id) {
		if(_accounts.containsKey(id))
			return _accounts.get(id);
		else
			return null;
	}
	
	
	/**
	 * abstract method for getting user info + ad accounts
	 * load user info (uid at the service) AND affiliated ad-accounts.
	 * may contains 2 API calls to 
	 *  1) get auth user info, 
	 *  2) list linked ad-account  
	 */
	protected abstract void retrieveUserInfo();
	
	/**
	 * abstract method for getting ad creatives per an ad-account.
	 * may contains 4 types of API calls to:
	 *  1) get campaigns in the ad-account
	 *  2) get ad groups (adset or campaign group) for each campaigns
	 *  3) get ad creatives for each ad groups
	 *  4) get the creative performances for each ads.
	 *  
	 * @param account - target adaccount
	 */
	protected abstract void retrieveCreativeData();
	
	protected static AppConfigure _config = AppConfigure.get();
	
	/**
	 * @param key
	 * @return
	 */
	protected static String getConfig(String key) {
		return _config.getProperty(key);
	}
	
	protected Account addAccount(String accountId) {
		if(_accounts==null)
			_accounts = new TreeMap<String, Account>();
		if(!_accounts.containsKey(accountId))
			_accounts.put(accountId, Account.get(accountId));
		return _accounts.get(accountId);
	}
	
	
}
