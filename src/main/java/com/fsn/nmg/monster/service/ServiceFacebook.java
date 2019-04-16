package com.fsn.nmg.monster.service;

import java.io.IOException;
import java.util.ArrayList;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINode;
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
	protected ArrayList<AdAccount> _accounts;
	
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
	
	public static String retrieveLongtermToken(String token) throws IOException {
		final String command = String.format("curl -m %d -G %s%s -d "
				+"grant_type=%s,client_id=%s,client_secret=%s,fb_exchange_token=%s", 
				// timeout in seconds
				10,
				// fb api host
				"https://graph.facebook.com",
				// fb api endpoint
				"/oauth/access_token",
				// grant type
				"fb_exchage_token",
				// client id
				getConfig(ENVKEY_FB_CLIENT_ID),
				// client secret
				getConfig(ENVKEY_FB_CLIENT_SECRET),
				// token
				token);
		final Runtime runs = Runtime.getRuntime();
		final Process proc = runs.exec(command);
		// TODO: check CURL works
		return null;
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
	
	private class AccessToken extends APINode {
		
	}
	
}
