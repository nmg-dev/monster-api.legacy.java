package com.fsn.nmg.monster.datamodel;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class AccountTest {
	final static String singleAccountID = "";
	final static String singleCampaignId = "the_campaign";
	final static String singleGroupId = "the_sample_group";
	final static String singleCreativeId = "this-is-the-sample-creative";
	

	@Test
	public void testCreateFromTotal() {
		assertNotNull(Account.get(singleAccountID));
		
	}
	
	@Test 
	public void testRetrieveFromTotal() {
		final Account acc = Account.get(singleAccountID);
		assertNotNull(acc);
		assertEquals(singleAccountID, acc.getId());
		
		assertTrue(acc == Account.get(singleAccountID));
	}
	
	
	@Test
	public void testReferalAccountCampaign() {
		final Account acc = Account.get(singleAccountID);
		final CampaignCreative cc = acc.creative(singleCampaignId, singleGroupId, singleCreativeId);
		assertNotNull(cc);
		
		assertEquals(singleCampaignId, cc._campaignId);
		assertEquals(singleGroupId, cc._adgroupId);
		assertEquals(singleCreativeId, cc._id);

		assertEquals(cc._account, Account.get(singleAccountID));
		
		
		assertNotNull(acc.campaignIds());
	}
	
	@Test
	public void testAppendingCampaignGroupMap() {
		final String tick = String.format("%x-x", System.currentTimeMillis());
		final Account acc = Account.get(tick);
		
		// not at all
		assertEquals(0, acc.campaignIds().size());
		
		// single id added
		acc.appendCampaignGroupMapping(singleCampaignId, singleCampaignId);
		
		assertEquals(1, acc.campaignIds().size());
		assertEquals(1, acc.campaignGroups(singleCampaignId).size());
		assertTrue(acc.campaignGroups(singleCampaignId).contains(singleCampaignId));
		assertFalse(acc.campaignGroups(singleCampaignId).contains(singleGroupId));
		
		// one more
		acc.appendCampaignGroupMapping(singleCampaignId, singleGroupId);
		assertEquals(2, acc.campaignGroups(singleCampaignId).size());
		assertTrue(acc.campaignGroups(singleCampaignId).contains(singleGroupId));
	}
	
	@Test
	public void testAppendingAdGroupMap() {
		final String tick = String.format("%x-y", System.currentTimeMillis());
		final ArrayList<String> samples = new ArrayList<String>();
		
		final Account acc = Account.get(tick);
		final int max = 1000;
		
		//
		acc.appendCampaignGroupMapping(singleCampaignId, singleGroupId);
		
		for(int i=0; i<max; i++) {
			final String ctick = String.format("%d", System.currentTimeMillis());
			if(samples.contains(ctick)) {
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				samples.add(ctick);
				acc.appendAdgroupCreativeMapping(singleGroupId, ctick);
			}
		}
		
		assertEquals(1, acc.campaignGroups(singleCampaignId).size());
		assertTrue(acc.campaignGroups(singleCampaignId).contains(singleGroupId));
		assertTrue(acc.campaignCreatives(singleCampaignId).length<max);
		
	}
	
	@Test
	public void testCampaignCreatives() {
		final String tick = String.format("%x-z", System.currentTimeMillis());
		final ArrayList<String> groupIds = new ArrayList<String>();
		final HashMap<String, Integer> groupCnts = new HashMap<String, Integer>();
		final Account acc = Account.get(tick);
		
		final String[] cids = new String[50];
		for(int i=0; i<cids.length; i++) {
			final String group = String.format("grp-%x", System.currentTimeMillis());
			if(!groupIds.contains(group)) {
				groupIds.add(group);
				groupCnts.put(group, 1);
			} else {
				groupCnts.put(group, groupCnts.get(group)+1);
			}
			final String cid = String.format("%s-%03d", tick, (System.currentTimeMillis()+i)%999);
			cids[i] = cid; 
			acc.creative(tick, group, cid);
			
			// random interrupt to increase tick in motion 
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		assertTrue(acc.campaignIds().contains(tick));
		assertEquals(groupIds.size(), acc.campaignGroups(tick).size());
		
		assertEquals(cids.length, acc.campaignCreatives(tick).length);
		
		for(String gid : groupIds) {
			final CampaignCreative[] ccs = acc.groupCreatives(gid);
			assertTrue(groupCnts.get(gid) == ccs.length);
		}
		
	}
	
	

}
