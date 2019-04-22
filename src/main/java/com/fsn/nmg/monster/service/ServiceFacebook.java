package com.fsn.nmg.monster.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;

import com.fsn.nmg.monster.MonsterUtils;
import com.fsn.nmg.monster.datamodel.AccessAccount;
import com.fsn.nmg.monster.datamodel.Account;
import com.fsn.nmg.monster.datamodel.AdGroup;
import com.fsn.nmg.monster.datamodel.Campaign;
import com.fsn.nmg.monster.datamodel.Creative;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author yg.song@nextmediagroup.co.kr
 *
 */
class ServiceFacebook extends AbstractService {
	
	private static final int API_TIMEOUT_SECONDS = 20;
	private static final int BATCH_LIMIT = 50;
	private static final boolean DEBUGGINGS = getConfig("FB_API_TESTING")!="true";
	private static final String ENVKEY_FB_API_HOST = "FB_API_HOST";
	private static final String ENVKEY_FB_CLIENT_ID = "FB_CLIENT_ID";
	private static final String ENVKEY_FB_CLIENT_SECRET = "FB_CLIENT_SECRET";
	private static final String ENVKEY_FB_APP_ACCESS = "FB_APP_ACCESS";
	private static final String ENVKEY_FB_INSIGHT_DATE_PRESET = "FB_INSIGHT_DATE_PRESET";
	
	private static final String ENVKEY_FB_USERINFO_FIELDS = "FB_BATCH_USER_FIELDS";
	private static final String ENVKEY_FB_ADACCOUNT_FIELDS = "FB_BATCH_ADACCOUNT_FIELDS";
	private static final String ENVKEY_FB_CAMPAIGN_FIELDS = "FB_BATCH_CAMPAIGN_FIELDS";
	private static final String ENVKEY_FB_ADGROUP_FIELDS = "FB_BATCH_ADGROUP_FIELDS";
	private static final String ENVKEY_FB_CREATIVE_FIELDS = "FB_BATCH_CREATIVE_FIELDS";
	private static final String ENVKEY_FB_ADINSHGIT_FIELDS = "FB_BATCH_INSIGHT_FIELDS";
	
	private static final String APIKEY_GRANT_TYPE = "grant_type";
	private static final String APIKEY_CLIENT_ID = "client_id";
	private static final String APIKEY_CLIENT_SECRET = "client_secret";
	private static final String APIKEY_EXCHANGE_TOKEN = "fb_exchange_token";
	private static final String APIKEY_ACCESS_TOKEN = "access_token";
	
	private static final String APIKEY_ID = "id";
	private static final String APIKEY_FILEDS = "fields";
	private static final String APIKEY_LIMITS = "limit";
	private static final String APIKEY_ADACCOUNTS = "adaccounts";
	private static final String APIKEY_ADACCOUNT_ID = "account_id";
	private static final String APIKEY_CAMPAIGNS = "campaigns";
	private static final String APIKEY_CAMPAIGN_ID = "campaign_id";
	private static final String APIKEY_ADSETS = "adsets";
	private static final String APIKEY_ADSET_ID = "adset_id";
	private static final String APIKEY_ADS = "ads";
	private static final String APIKEY_DATA = "data";
	private static final String APIKEY_ERROR = "error";
	private static final String APIKEY_ERROR_MESSAGE = "message";
	
	private static final String APIKEY_LEVEL = "level";
	private static final String APIKEY_TIME_INC = "time_increment";
	private static final String APIKEY_DATES = "date_preset";
	
	private static final String APIKEY_PAGING = "paging";
	private static final String APIKEY_PAGING_NEXT = "next";
	private static final String APIKEY_PAGING_CURSOR = "cursor";
	private static final String APIKEY_PAGING_NEXT_CURSOR = "after";
	
	private static final String APIKEY_BATCH_METHOD = "method";
	private static final String APIKEY_BATCH_PATH = "relative_url";
	
	private static final String APIKEY_STATUS = "effective_status";
	private static final String APIKEY_PERIOD_FROM = "start_time";
	private static final String APIKEY_PERIOD_TILL = "end_time";
	
	private static final String APIKEY_ADCREATIVES = "adcreatives";
	
	private static final String APIVAL_STATUS_ACTIVE = "ACTIVE";
	
	
	/**
	 * default api paramters for long-term token exchange
	 */
	@SuppressWarnings("serial")
	private static final HashMap<String,String> TOKEN_EXCHANGE_PARAMS = new HashMap<String, String>() {{
		put(APIKEY_GRANT_TYPE, APIKEY_EXCHANGE_TOKEN);
		put(APIKEY_CLIENT_ID, getConfig(ENVKEY_FB_CLIENT_ID));
		put(APIKEY_CLIENT_SECRET, getConfig(ENVKEY_FB_CLIENT_SECRET));
	}};
	
	private static final HashMap<String,String> INSIGHT_FIELD_MAPS = new HashMap<String,String>() {{
		put("impressions", null);
		put("reach", null);
		put("spend", "cost");
		put("clicks", null);
//		put("account_currency", "currency");
	}};
	
	
	
	
	/**
	 * build api parameter map for long-term token exchange
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static HashMap<String,String> buildExchangeTokenParams(String token) {
		final HashMap<String,String> params = (HashMap<String, String>) TOKEN_EXCHANGE_PARAMS.clone();
		params.put(APIKEY_EXCHANGE_TOKEN, token);
		return params;
	}
	
	/**
	 * check if error presented at facebook api response
	 * @param resp
	 * @return error message; null if no error.
	 */
	private static final String getAPIResponseHasError(JsonObject resp) {
		try {
			return resp.get(APIKEY_ERROR).getAsJsonObject()
				.get(APIKEY_ERROR_MESSAGE).getAsString();
		}
		catch(NullPointerException ex) {
			return null;
		}
		catch(IllegalStateException ex) {
			return null;			
		} 
	}
	
	private static final String getAPIResponseNextPaged(JsonObject resp) { 
		return getAPIResponseNextPaged(resp, false);
	}
	/**
	 * check if next page cursor presented at facebook api response.
	 * @param resp
	 * @param fullURL
	 * @return next page cursor (after); null if ends.
	 */
	private static final String getAPIResponseNextPaged(JsonObject resp, boolean fullURL) {
		try {
			final String nextPath = resp.get(APIKEY_PAGING).getAsJsonObject()
				.get(APIKEY_PAGING_NEXT).getAsString();
			if(nextPath==null || nextPath.length()<=0)
				return null;
			
			if(fullURL) {
				return nextPath;
			} else {
				final String nextCursor = resp.get(APIKEY_PAGING).getAsJsonObject()
					.get(APIKEY_PAGING_CURSOR).getAsJsonObject()
					.get(APIKEY_PAGING_NEXT_CURSOR).getAsString();
				return nextCursor;
			}
		}
		catch(NullPointerException ex) {
			return null;
		}
		catch(IllegalStateException ex) {
			return null;
		}
	}
	
	
	
	private final HashMap<String,String> _apiParams = new HashMap<String, String>();
	private final ArrayList<String> _batches = new ArrayList<String>();
//	private final TreeMap<String,Boolean> _campaignStats = new TreeMap<String, Boolean>();
//	private final TreeMap<String,Boolean> _adgroupStats = new TreeMap<String, Boolean>();
//	private final TreeMap<String,CampaignCreative> _creativeMap = new TreeMap<String,CampaignCreative>();
//	private final ArrayList<JsonObject> _drilldowns = new ArrayList<JsonObject>();
	
	private static String buildAPIPath(String endpoint, Map<String,String> params) {
		String path = String.format("%s%s", 
				endpoint.contains("://") || endpoint.startsWith("/") ? "" : "/", 
				endpoint);
		
		// TODO: remove host if it contains

		if(params!=null && 0<params.size()) {
			for(Entry<String,String> p : params.entrySet()) {
				path += path.contains("?") ? "&" : "?";
				path += String.format("%s=%s", p.getKey(), p.getValue());
			}
		}
		return path;
	}
	
	/**
	 * static facebook api with curl (curl command required)
	 * @param endpoint - api path to send. must begin with "/"
	 * @param params - key=value map
	 * @return responsed json object
	 */
	private static JsonObject getAPI(String endpoint, Map<String,String> params) {
		final String url = String.format("%s%s",
				endpoint.contains("://") ? "" : getConfig(ENVKEY_FB_API_HOST), 
				buildAPIPath(endpoint, params));
		
		try {
			if(DEBUGGINGS) {
				System.out.println(url);
				
				final String respTxt = MonsterUtils.executeCURL(url,API_TIMEOUT_SECONDS);
				System.out.println("--------");
				System.out.println(respTxt);
				System.out.println("");
				return MonsterUtils.parseJson(respTxt).getAsJsonObject();
			} else {
				return MonsterUtils.executeCURLtoJSON(url, API_TIMEOUT_SECONDS)
						.getAsJsonObject();			
			}			
		} catch(IllegalStateException ex) {
			return null;
		}
	}
	
	
	/**
	 * @param endpoint
	 * @return
	 */
	private JsonObject getAccessedAPI(String endpoint) {
		// append access token
		_apiParams.put(APIKEY_ACCESS_TOKEN, _auth.getAccessToken());
		return getAPI(endpoint, _apiParams);
	}
	 
	/**
	 * @param user
	 */
	protected ServiceFacebook(AccessAccount user) {
		super(user);
	}
	
	@Override
	protected Account addAccount(String accountId) {
		if(accountId.startsWith("act_"))
			accountId = accountId.substring(4);
		return super.addAccount(accountId);
	}
	
	
	/**
	 * server-side exchage of short-term login token to long-term(2w) token, with curl
	 * @param token
	 * @return Map<String,String> token information; i.e. {"access_token": ..., "expires_in"=unix_timestamp(int), "token_type"="bearer"}
	 * @throws IOException
	 */
	public static Map<String,String> retrieveLongtermToken(String token) {
		// setup params
		final HashMap<String,String> params = buildExchangeTokenParams(token);
		final JsonObject resp = getAPI("/oauth/access_token", params)
				.getAsJsonObject();
		final String error = getAPIResponseHasError(resp);
		final TreeMap<String,String> ret = new TreeMap<String, String>();
		if(error!=null) {
			ret.put("error", error);
		} else {
			for(Entry<String,JsonElement> e : resp.entrySet()) {
				ret.put(e.getKey(), e.getValue().getAsString());
			}
		}
			
		return ret;
	}
	
	
	/**
	 * @return
	 */
	protected String myAuthIdString() {
		if(_auth.getId()!=null)
			return _auth.getId();
		else
			return "me";
	}
	
	/**
	 * @param afters
	 */
	private void _retrieveUserInfoBuildingParams(String afters) {
		// build parameters ?fields=id,adaccounts
		_apiParams.clear();
		_apiParams.put(APIKEY_FILEDS, getConfig(ENVKEY_FB_USERINFO_FIELDS));
		_apiParams.put(APIKEY_LIMITS, "50");
		if(afters!=null) {
			_apiParams.put(APIKEY_PAGING_NEXT_CURSOR, afters);
		}
	}

	
	/**
	 * @param resp
	 * @return
	 */
	private String _retrieveUserInfoParseAndNext(JsonObject resp) {
		try {
			
			if(_auth.getId()==null)
				_auth.setId(resp.get(APIKEY_ID).getAsString());
			
			final JsonArray accounts = resp.get(APIKEY_ADACCOUNTS).getAsJsonObject()
				.get(APIKEY_DATA).getAsJsonArray();
			for(JsonElement acc : accounts) {
				final String accountId = acc.getAsJsonObject()
						.get(APIKEY_ID).getAsString();
				// appending accounts
				addAccount(accountId);
			}
			
			return getAPIResponseNextPaged(resp);
			
		} catch(Exception ex) {
			//
			ex.printStackTrace();
			return null;
		}
	}


	/**
	 * retrieve user info: facebook.
	 * endpoints to /me?fields=id,adaccounts
	 */
	@Override
	protected void retrieveUserInfo() {
		if(_auth.getId()!=null)
			return;
		
		final String path = buildAPIPath(myAuthIdString(), null);
		String afters = null;
		do {
			_retrieveUserInfoBuildingParams(afters);
			JsonObject resp = getAccessedAPI(path);
			afters = _retrieveUserInfoParseAndNext(resp);
			
			final String err = getAPIResponseHasError(resp);
			if(err!=null)
				break;		
		} while(afters!=null);
	}
	
	protected boolean _retrieveCreativeDataDrillDownParseStack(ArrayList<JsonObject> stack, JsonObject obj, String apikey) {
		if(obj.has(apikey)) {
			final JsonArray arr = obj.get(apikey).getAsJsonObject()
					.getAsJsonArray(APIKEY_DATA);
			for(JsonElement e : arr) {
				final JsonObject rs = e.getAsJsonObject();
				stack.add(rs);
			}
			return true;
		} else {
			return false;
		}
	}
	

	protected void _retrieveCreativeDataDrillDownBatch(ArrayList<JsonObject> stack) {
		_apiParams.clear();
		// pop single
		final String reqpath = _batches.remove(0);
		final JsonObject rs = getAccessedAPI(reqpath);
		
		// get account - campaign - adset - ad info
		stack.clear();
		stack.add(rs);
		
		final String[] apikeysbyLevel = new String[] {
			APIKEY_CAMPAIGNS, APIKEY_ADSETS, APIKEY_ADS,
		};
		boolean isLeaf = true;

		while(!stack.isEmpty()) {
			final JsonObject resp = stack.remove(0);
			if(!resp.has(APIKEY_DATA)) continue;
			final JsonArray values = resp.get(APIKEY_DATA).getAsJsonArray();
			final String nextAPI = getAPIResponseNextPaged(resp, true);
			if(nextAPI != null)
				_batches.add(nextAPI);
			
			
			for(JsonElement _v : values) {
				final JsonObject val = _v.getAsJsonObject();
				
				isLeaf = true;
				for(String apikey : apikeysbyLevel) {
					if(_retrieveCreativeDataDrillDownParseStack(stack, val, apikey)) {
						isLeaf = false;
						break;
					}
				}
				
				// appending account, campaign, ads when the term is leaf (ad)
				if(isLeaf) {
					final String accId = val.get(APIKEY_ADACCOUNT_ID).getAsString();
					if(!val.has(APIKEY_CAMPAIGN_ID)) continue;
					final String cmpId = val.get(APIKEY_CAMPAIGN_ID).getAsString();
					if(!val.has(APIKEY_ADSET_ID)) continue;
					final String grpId = val.get(APIKEY_ADSET_ID).getAsString();
					if(!val.has(APIKEY_ID)) continue;
					final String adId = val.get(APIKEY_ID).getAsString();
					
//					final Account account = Account.get(accId);
					if(!_accounts.containsKey(accId))
						addAccount(accId);
					final Account account = Account.get(accId);
					final Campaign campaign = account.addCampaign(cmpId);
					final AdGroup group = campaign.addGroup(grpId);
					
					// TEST
					System.out.println(String.format("%s - %s - %s - %s", accId, cmpId, grpId, adId));
					
					// init creatives
					group.addCreative(adId);
				}
			}
		}
	}

	
	protected void _retrieveCreativeDataParse(JsonObject data) {
		if(!(data.has(APIKEY_ADACCOUNT_ID) 
		&& data.has(APIKEY_CAMPAIGN_ID)
		&& data.has(APIKEY_ADSET_ID))) return;
		final String accId = data.get(APIKEY_ADACCOUNT_ID).getAsString();
		final String cmpId = data.get(APIKEY_CAMPAIGN_ID).getAsString();
		final String grpId = data.get(APIKEY_ADSET_ID).getAsString();
		final String adId = data.get("ad_id").getAsString();
		final String _date = data.get("date_start").getAsString();
		
		final Account account = _accounts.get(accId);
		final Campaign campaign = account.addCampaign(cmpId);
		final AdGroup group = campaign.addGroup(grpId);
		final Creative ad = group.addCreative(adId);
		
		final LocalDate date = LocalDate.parse(_date);
		
		// setup values
		_apiParams.clear();
		for(Entry<String,JsonElement> ev : data.entrySet()) {
			final String ek = ev.getKey();
			if(!INSIGHT_FIELD_MAPS.containsKey(ek)) continue;
			final String attr = INSIGHT_FIELD_MAPS.get(ek) != null ? INSIGHT_FIELD_MAPS.get(ek) : ek;
			_apiParams.put(attr, ev.getValue().getAsString());
		}
		
		if(data.has("actions")) {
			for(Entry<String,JsonElement> e : data.get("actions").getAsJsonObject().entrySet()) {
				final JsonObject v = e.getValue().getAsJsonObject();
				final String ek = v.get("action_type").getAsString();
				final String ev = v.get("value").getAsString();
				_apiParams.put(ek, ev);
			}
		}
		ad.putValuesAt(date, _apiParams);
		
	}
	
	protected void _batchAccountString(Function<String, String> urlFn) {		
		for(Account account : _accounts.values()) {
			if(account.hasSet()) continue;
			final String path = urlFn.apply(account.getId());
			_batches.add(path);
		}
	}
	/**
	 *
	 */
	@Override
	protected void retrieveCreativeData() {
		_batches.clear();
		final ArrayList<JsonObject> stack = new ArrayList<JsonObject>();
		
		// append account ids
//		_batchAccountString((accId) -> String.format("/me/adaccounts?fields=%s", getConfig(ENVKEY_FB_ADACCOUNT_FIELDS)));
		final String seedPath = String.format("/me/adaccounts?fields=%s", getConfig(ENVKEY_FB_ADACCOUNT_FIELDS));
		_batches.add(seedPath);
		
		while(!_batches.isEmpty()) {
			// read drilldown stack
			_retrieveCreativeDataDrillDownBatch(stack);
		}
		
		// read insights
		_batches.clear();
		_batchAccountString((accId) -> 
			String.format("/act_%s/insights?level=ad&time_increment=1&date_preset=%s&field=%s",
					accId,
					getConfig(ENVKEY_FB_INSIGHT_DATE_PRESET),
					getConfig(ENVKEY_FB_ADINSHGIT_FIELDS)));
		while(!_batches.isEmpty()) {
			final String path = _batches.remove(0);
			final JsonObject resp = getAccessedAPI(path);
			_retrieveCreativeDataParse(resp);
			
			final String nextPath = getAPIResponseNextPaged(resp, true);
			if(nextPath!=null)
				_batches.add(nextPath);
			
		}
	}

	
}
