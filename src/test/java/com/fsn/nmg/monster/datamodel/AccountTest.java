package com.fsn.nmg.monster.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountTest {
	final static String singleAccountID = "";
	final static String singleCampaignId = "the_campaign";
	final static String singleGroupId = "the_sample_group";
	final static String singleCreativeId = "this-is-the-sample-creative";
	

	@Test
	public void test01CreateFromTotal() {
		assertNotNull(Account.get(singleAccountID));
		
	}
	
	@Test 
	public void test02RetrieveFromTotal() {
		final Account acc = Account.get(singleAccountID);
		assertNotNull(acc);
		assertEquals(singleAccountID, acc.getId());
		
		assertTrue(acc == Account.get(singleAccountID));
	}
	
	
	@Test
	public void test03ReferalAccountCampaign() {
		final Account acc = Account.get(singleAccountID);
		final Campaign cmp = acc.campaign(singleCampaignId);
		final AdGroup grp = cmp.adgroup(singleGroupId);
		final Creative cc = grp.creative(singleCreativeId);
		assertNotNull(cc);
		
		assertEquals(singleCampaignId, cmp.getId());
		assertEquals(singleGroupId, grp.getId());
		assertEquals(singleCreativeId, cc.getId());

		assertEquals(cc.getGroup().getCampaign().getAccount(), Account.get(singleAccountID));
		
		
		assertNotNull(acc.campaignIds());
	}

}
