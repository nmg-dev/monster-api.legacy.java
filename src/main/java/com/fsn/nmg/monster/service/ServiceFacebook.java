package com.fsn.nmg.monster.service;

import java.util.ArrayList;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.User;
import com.fsn.nmg.monster.datamodel.AccessAccount;
import com.fsn.nmg.monster.datamodel.Account;

class ServiceFacebook extends AbstractService {
	
	private static final String ENVKEY_FB_CLIENT_ID = "FB_CLIENT_ID";
	private static final String ENVKEY_FB_CLIENT_SECRET = "FB_CLIENT_SECRET";
	private static final String ENVKEY_FB_APP_ACCESS = "FB_APP_ACCESS";
	
	protected APIContext _context;
	
	private final String _clientID;
	private final String _clientSecret;
	private final String _appAccessToken;
	
	protected ServiceFacebook(AccessAccount user) {
		super(user);
		
		this._clientID = getConfig(ENVKEY_FB_CLIENT_ID);
		this._clientSecret = getConfig(ENVKEY_FB_CLIENT_SECRET);
		this._appAccessToken = getConfig(ENVKEY_FB_APP_ACCESS);
		
		try {
			this._context = new APIContext(user.getAccessToken(), this._clientSecret);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	

	@Override
	protected void retrieveAccounts() {
		final ArrayList<Account> rets = new ArrayList<Account>();
		try {
			// init access account
			final User uinfo = User.fetchById("me", this._context);
			if(_auth.getId()==null) _auth.setId(uinfo.getFieldId());
			APINodeList<AdAccount> accounts = uinfo.getAdAccounts().execute();
			for(AdAccount acc : accounts) {
				final String accountId = acc.getFieldAccountId();
				rets.add(Account.get(accountId));
			}
			
		} catch(APIException ex) {
		}
	}

	@Override
	protected void retrieveAccountData() {
		// TODO Auto-generated method stub
		
	}
	
}
