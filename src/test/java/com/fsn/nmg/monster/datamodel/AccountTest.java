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
		final Campaign cmp = acc.addCampaign(singleCampaignId);
		final AdGroup grp = cmp.addGroup(singleGroupId);
		final Creative cc = grp.addCreative(singleCreativeId);
		assertNotNull(cc);
		
		assertEquals(singleCampaignId, cmp.getId());
		assertEquals(singleGroupId, grp.getId());
		assertEquals(singleCreativeId, cc.getId());

		assertEquals(cc.getGroup().getCampaign().getAccount(), Account.get(singleAccountID));
		
		
		assertNotNull(acc.campaignIds());
	}

}
