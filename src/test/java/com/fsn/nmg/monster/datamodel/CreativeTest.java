package com.fsn.nmg.monster.datamodel;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;

import org.junit.Test;

public class CreativeTest {

	@Test
	public void testCreateCreative() {
		final String tick = String.format("%x", System.currentTimeMillis());
		final Account account = Account.get(tick);
		final CampaignCreative cc = new CampaignCreative(account, tick, tick, tick);
		
		assertNotNull(cc);
		assertEquals(tick, cc.getId());
		
		assertFalse(cc.hasSet());
	}
	
	@Test
	public void testCreativeSet() {
		final String tick = String.format("%x", System.currentTimeMillis());
		final Account account = Account.get(tick);
		final CampaignCreative cc = new CampaignCreative(account, tick, tick, tick);
		
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
	public void testPutValues() {
		final String tick = String.format("%x", System.currentTimeMillis());
		final Account account = Account.get(tick);
		final CampaignCreative cc = new CampaignCreative(account, tick, tick, tick);
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
		
		for(String c: columnNames) {
			assertEquals(values.get(c), cc.getValue(today, c));
		}
	}
	

}
