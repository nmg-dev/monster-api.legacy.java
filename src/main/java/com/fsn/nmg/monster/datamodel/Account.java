package com.fsn.nmg.monster.datamodel;

import java.util.Collection;
import java.util.TreeMap;

/**
 * @author yg.song@nextmediagroup.co.kr
 *
 */
public class Account extends AbstractModel<AccessAccount, Campaign> {
	protected final static TreeMap<String, Account> TotalAccounts = new TreeMap<String, Account>();
	/**
	 * @param adAccountId
	 * @return
	 */
	public static Account get(String adAccountId) {
		if(!TotalAccounts.containsKey(adAccountId)) {
			final Account acc = new Account(adAccountId);
			TotalAccounts.put(adAccountId, acc);
		}
		return TotalAccounts.get(adAccountId);
	}
	
	
	
	
	/**
	 * @param id
	 */
	protected Account(String id) {
		super(id);
	}
	
	public Campaign campaign(String campaignId) {
		return addChild(campaignId);
	}
	
	/**
	 * @return
	 */
	public Collection<String> campaignIds() {
		return childIds();
	}
	
	
	public boolean hasSet() {
		return _children!=null;
	}
	
	
	/**
	 * @param campaignId
	 * @param groupId
	 * @param id
	 * @return
	 */
//	public CampaignCreative creative(String campaignId, String groupId, String id) {
//		appendCampaignGroupMapping(campaignId, groupId);
//		appendAdgroupCreativeMapping(groupId, id);
//		if(!creativeMap.containsKey(id)) {
//			final CampaignCreative cc = new CampaignCreative(this, campaignId, groupId, id);
//			creativeMap.put(id, cc);
//		}
//		return creativeMap.get(id);
//	}
	
	/**
	 * @param campaignId
	 * @return
	 */
//	public CampaignCreative[] campaignCreatives(String campaignId) {
//		if(!campaignMap.containsKey(campaignId))
//			return null;
//		// clear id stacks
//		_idStacks.clear();
//		// append campaign - group - creative ids
//		for(String adgroupId : campaignMap.get(campaignId)) {
//			if(adgroupMap.containsKey(adgroupId))
//				_idStacks.addAll(adgroupMap.get(adgroupId));
//		}
//		
//		return _getCreatives();
//	}
	
	/**
	 * @param groupId
	 * @return
	 */
//	public CampaignCreative[] groupCreatives(String groupId) {
//		if(!adgroupMap.containsKey(groupId))
//			return null;
//		
//		_idStacks.clear();
//		// append group - creative ids
//		_idStacks.addAll(adgroupMap.get(groupId));
//		
//		return _getCreatives();
//		
//	}
	
	
	/**
	 * @param campaignId
	 * @param groupId
	 */
//	protected void appendCampaignGroupMapping(String campaignId, String groupId) {
//		if(!campaignMap.containsKey(campaignId))
//			campaignMap.put(campaignId, new TreeSet<String>());
//		if(!campaignMap.get(campaignId).contains(groupId))
//			campaignMap.get(campaignId).add(groupId);
//	}
	
	/**
	 * @param groupId
	 * @param id
	 */
//	protected void appendAdgroupCreativeMapping(String groupId, String id) {
//		if(!adgroupMap.containsKey(groupId))
//			adgroupMap.put(groupId, new TreeSet<String>());
//		if(!adgroupMap.get(groupId).contains(id))
//			adgroupMap.get(groupId).add(id);
//	}


	/**
	 * @return
	 */
//	protected CampaignCreative[] _getCreatives() {
//		final CampaignCreative[] ccs = new CampaignCreative[_idStacks.size()];
//		for(int i=0; i<ccs.length; i++) {
//			ccs[i] = creativeMap.get(_idStacks.get(i));
//		}
//		
//		_idStacks.clear();
//		return ccs;
//	}
	
	private static final String[] ATTRIBUTE_KEYS = new String[] {};

	@Override
	protected String[] initiateAttributes() {
		return ATTRIBUTE_KEYS;
	}

	@Override
	protected Campaign createChild(String id) {
		return new Campaign(id);
	} 
	
}
