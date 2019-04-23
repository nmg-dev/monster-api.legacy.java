package com.fsn.nmg.monster.datamodel;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreativeTest {
	
	private Creative buildCreative(String id) {
		return buildCreative(id, id, id, id);
	}
	
	private Creative buildCreative(String id, String groupId, String campaignId, String accountId) {
		final Account account = Account.get(accountId);
		final Campaign cmp = account.campaign(campaignId);
		final AdGroup grp = cmp.adgroup(groupId);
		return grp.creative(id);
	}

	@Test
	public void test01CreateCreative() {
		final String tick = String.format("%x", System.currentTimeMillis());
		final Creative cc = buildCreative(tick);
		
		assertNotNull(cc);
		assertEquals(tick, cc.getId());
		
		System.out.println(">>?" + cc.hasSet());
		assertFalse(cc.hasSet());
	}
	
	@Test
	public void test02CreativeSet() {
		final String tick = String.format("%x", System.currentTimeMillis());
		final Creative cc = buildCreative(tick);
		
		final LocalDate today = LocalDate.now();
		final String columnName = "col1";
		final String sampleValue = String.format("%06d", System.currentTimeMillis()%1000000);
		
		cc.putValue(today, columnName, sampleValue);
		
		assertTrue(cc.hasSet());
		assertEquals(1, cc.columns().size());
		assertTrue(cc._columns.contains(columnName));
		
		assertEquals(sampleValue, cc.getValue(today, columnName));
	}
	
	@Test
	public void test03PutValues() {
		final String tick = String.format("%x", System.currentTimeMillis());
		final Creative cc = buildCreative(tick);
		final String[] columnNames = new String[] {
				"impressions",
				"reach",
				"clicks",
				"views",
				"conversions",
		};
		
		final LocalDate today = LocalDate.now();
		final HashMap<String, String> values = new HashMap<String, String>();
		
		for(String c : columnNames) {
			values.put(c, String.format("%06d", System.currentTimeMillis()%1000000));
		}
		cc.putValuesAt(today, values);
		
		final String[] puts = cc.getValuesAt(today);
		final List<String> columns = cc.columns();
		for(int i=0; i<columns.size(); i++) {
			final String c = columns.get(i);
			assertEquals(values.get(c), puts[i]);
		}
	}
	
	@Test
	public void test04Activated() {
		final String tick = String.format("%x", System.currentTimeMillis());
		final Creative cc = buildCreative(tick);
		
		assertFalse(cc.isActive());
		assertFalse(cc.isGroupActive());
		assertFalse(cc.isCampaignActive());
		
		// set creative activate only (all false)
		cc.setActive(true);
		assertFalse(cc.isActive());
		assertFalse(cc.isGroupActive());
		assertFalse(cc.isCampaignActive());
		
		// set group activate
		cc.setGroupActive(true);
		assertFalse(cc.isActive());
		assertFalse(cc.isGroupActive());
		assertFalse(cc.isCampaignActive());
		
		// set campaign activate
		cc.setCampaignActive(true);
		assertTrue(cc.isActive());
		assertTrue(cc.isGroupActive());
		assertTrue(cc.isCampaignActive());
		
		// deactivate creative again
		cc.setActive(false);
		assertFalse(cc.isActive());
		assertTrue(cc.isGroupActive());
		assertTrue(cc.isCampaignActive());
		
		// deactivate group
		cc.setGroupActive(false);
		assertFalse(cc.isActive());
		assertFalse(cc.isGroupActive());
		assertTrue(cc.isCampaignActive());
		
		// deactivate campaign to complete
		cc.setCampaignActive(false);
		assertFalse(cc.isActive());
		assertFalse(cc.isGroupActive());
		assertFalse(cc.isCampaignActive());
	}
	
}
