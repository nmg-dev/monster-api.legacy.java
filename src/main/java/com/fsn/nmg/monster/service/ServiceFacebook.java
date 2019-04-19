package com.fsn.nmg.monster.service;

import java.io.IOException;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author yg.song@nextmediagroup.co.kr
 *
 */
class ServiceFacebook extends AbstractService {
	
	private static final int API_TIMEOUT_SECONDS = 5;
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
	
	private static final HashMap<String,String> AD_ACCOUNT_INFO_PARAMS = new HashMap<String,String>() {{
		put(APIKEY_FILEDS, getConfig(ENVKEY_FB_ADACCOUNT_FIELDS));		
	}};
	
	private static final HashMap<String,String> AD_CAMPAIGN_INFO_PARAMS = new HashMap<String,String>() {{
		put(APIKEY_FILEDS, getConfig(ENVKEY_FB_CAMPAIGN_FIELDS));
	}};
	
	private static final HashMap<String,String> AD_ADGROUP_INFO_PARAMS = new HashMap<String,String>() {{
		put(APIKEY_FILEDS, getConfig(ENVKEY_FB_ADGROUP_FIELDS));
	}};
	
	private static final HashMap<String,String> AD_CREATIVE_INFO_PARAMS = new HashMap<String,String>() {{
		put(APIKEY_FILEDS, getConfig(ENVKEY_FB_CREATIVE_FIELDS));
	}};
	
	private static final HashMap<String,String> AD_ACCOUNT_INSIGHT_PARAMS = new HashMap<String,String>() {{
		put(APIKEY_FILEDS, getConfig(ENVKEY_FB_ADINSHGIT_FIELDS));
		put(APIKEY_DATES, getConfig(ENVKEY_FB_INSIGHT_DATE_PRESET));
		put(APIKEY_LEVEL, "ad");
		put(APIKEY_TIME_INC, "1");
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
		String path = String.format("%s%s", endpoint.startsWith("/") ? "" : "/", endpoint);
		
		// TODO: remove host if contains

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
		String url = String.format("%s%s",
				getConfig(ENVKEY_FB_API_HOST), buildAPIPath(endpoint, params));
		
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
	 * build batch request
	 * @param path
	 * @return
	 */
	protected static JsonObject _batchAPIRequest(String path) {
		final JsonObject batch = new JsonObject();
		batch.addProperty(APIKEY_BATCH_METHOD, "GET");
		batch.addProperty(APIKEY_BATCH_PATH, path);
		return batch;
	}
	
	/**
	 * batch processing apis
	 * @param access
	 * @param paths
	 * @return
	 */
	protected static JsonArray batchAPI(String access, ArrayList<String> paths) {
		final JsonArray batches = new JsonArray();
		for(String path : paths)
			batches.add(_batchAPIRequest(path));
		final String[] values = new String[] {
			String.format("%s=%s", APIKEY_ACCESS_TOKEN, access),
			String.format("batch=%s", batches.toString()),
		};
		
		final String resp = MonsterUtils.executeCURLPost(getConfig(ENVKEY_FB_API_HOST), values, 30);
		return MonsterUtils.parseJson(resp).getAsJsonArray();
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

	private void _retrieveCreativeInsightBuildingParams(String afters) {
		_apiParams.clear();
		_apiParams.put(APIKEY_FILEDS, getConfig(ENVKEY_FB_ADINSHGIT_FIELDS));
		_apiParams.put(APIKEY_DATES, getConfig(ENVKEY_FB_INSIGHT_DATE_PRESET));
		_apiParams.put(APIKEY_LEVEL, "ad");
		_apiParams.put(APIKEY_TIME_INC, "1");
		_apiParams.put(APIKEY_LIMITS, "50");
		if(afters!=null) {
			_apiParams.put(APIKEY_PAGING_NEXT_CURSOR, afters);
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
	
	protected void buildPathBatches(
			ArrayList<String> batches, 
			Collection<String> accountIds, 
			Function<String,Boolean> validateFn,
			Function<String,String> buildFn) {
		batches.clear();
		
		for(String accId : accountIds) {
			if(!validateFn.apply(accId))
				continue;
			
			final String apiPath = buildFn.apply(accId);
			batches.add(apiPath);
		}
	}
	
	protected void _retrieveCreativeDataParseDrilldowns(JsonObject obj, TreeMap<String,Boolean> statMap, String nextKey) {
		final JsonArray downs = obj.get(nextKey).getAsJsonObject()
				.get(APIKEY_DATA).getAsJsonArray();
		for(JsonElement _down : downs) {
			final JsonObject down = _down.getAsJsonObject();
			final String id = down.get(APIKEY_ID).getAsString();
			if(!statMap.containsKey(id)) {
				final String stat = down.get(APIKEY_STATUS).getAsString();
				statMap.put(id, APIVAL_STATUS_ACTIVE.equalsIgnoreCase(stat));
			}
			
//			_drilldowns.add(down);
		}
	}
	
	
	protected void _retrieveCreativeDataParse(JsonElement respElement, ArrayList<String> batches) {
		final JsonObject respObj = respElement.getAsJsonObject();
		final String respBody = respObj.get("body").getAsString();
		final JsonObject resp = MonsterUtils.parseJson(respBody).getAsJsonObject();
		final String next = getAPIResponseNextPaged(resp, true);
		final String error = getAPIResponseHasError(resp);
		if(error!=null) {
			// TODO: exception
		}
		if(next!=null) {
			batches.add(next);
		}
		
		// drill down into ads
//		_drilldowns.clear();
//		_drilldowns.add(resp);
//		while(!_drilldowns.isEmpty()) {
//			// pop one
//			final JsonObject obj = _drilldowns.remove(0);
//			// is account info
//			if(obj.has(APIKEY_CAMPAIGNS)) {
//				_retrieveCreativeDataParseDrilldowns(resp, _campaignStats, APIKEY_CAMPAIGNS);
//			} else if(obj.has(APIKEY_ADSETS)) {
//				_retrieveCreativeDataParseDrilldowns(resp, _adgroupStats, APIKEY_ADSETS);
//				final String _since = obj.get(APIKEY_PERIOD_FROM).getAsString();
//				final String _until = obj.get(APIKEY_PERIOD_TILL).getAsString();
//			} else if(obj.has(APIKEY_CAMPAIGN_ID) && obj.has(APIKEY_ADSET_ID)) {
//				final String accountId = obj.get(APIKEY_ADACCOUNT_ID).getAsString();
//				final String campaignId = obj.get(APIKEY_CAMPAIGN_ID).getAsString();
//				final String adsetId = obj.get(APIKEY_ADSET_ID).getAsString();
//				final String creativeId = obj.get(APIKEY_ID).getAsString();
//				
//				// block running if creative presented
//				if(_creativeMap.containsKey(creativeId)) continue;
//				
//				final JsonObject _creative = obj.get(APIKEY_ADCREATIVES).getAsJsonObject();
//				
//				final Account account = _accounts.get(accountId);
//				final CampaignCreative cc = account.creative(campaignId, adsetId, creativeId);
//				_creativeMap.put(creativeId, cc);
//			}
//		}
		
		
	}

	/**
	 *
	 */
	@Override
	protected void retrieveCreativeData() {
		// build seed batches
		buildPathBatches(_batches, _accounts.keySet(),
				(String id) -> !_accounts.containsKey(id),
				(String id) -> buildAPIPath(id, AD_ACCOUNT_INFO_PARAMS));
		
		while(!_batches.isEmpty()) {
			// trim to size
			final List<String> trimmed = _batches.subList(0, BATCH_LIMIT);
						
			// responses
			while(!_batches.isEmpty()) {
				final JsonArray resps = batchAPI(_auth.getAccessToken(), _batches);
								
				for(JsonElement _resp : resps) {
					final JsonObject resp = _resp.getAsJsonObject();
					final String next = getAPIResponseNextPaged(resp);
					final String error = getAPIResponseHasError(resp);
					if(error!=null) continue;
					if(next!=null) {
//						_batches.add(buildAPIPath(endpoint, params));
					}
				}
			}
			
			_batches.removeAll(trimmed);
			
		}
		
		
	}

	
}
