package com.fsn.nmg.monster.datamodel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CampaignCreative {
	protected final Account _account;
	protected String _campaignId;
	protected String _adgroupId;
	protected String _id;
	
	protected final ArrayList<String> _columns;
	protected final HashMap<String, ArrayList<String>> _values;
	
	protected final HashMap<LocalDate, Integer> dateIndexMap;
	
	protected boolean _active;
	
	
	protected CampaignCreative(Account account, String campaign, String adgroup, String id) {
		this._account = account;
		this._campaignId = campaign;
		this._adgroupId = adgroup;
		this._id = id;
		
		this._columns = new ArrayList<String>();
		this._values = new HashMap<String, ArrayList<String>>();
		this.dateIndexMap = new HashMap<LocalDate, Integer>();
	}
	
	public String getId() {
		return this._id;
	}
	
	public void setPeriod(LocalDate from, LocalDate till) {
		LocalDate cs = from;
		while(cs.compareTo(till) <=0) {
			if(!dateIndexMap.containsKey(cs)) {
				dateIndexing(cs);
			}
			cs = cs.plus(1, ChronoUnit.DAYS);
		}
	}
	
	public boolean isActive() {
		return this._active;
	}
	
	public boolean hasSet() {
		return !this._values.isEmpty(); 
	}
	
	public void setActive(boolean active) {
		this._active = active;
	}
	
	protected int dateIndexing(LocalDate date) {
		if(!dateIndexMap.containsKey(date))
			dateIndexMap.put(date, dateIndexMap.size());
		return dateIndexMap.get(date);
	}
	
	protected ArrayList<String> columnIndexing(String key) {
		if(!_values.containsKey(key)) {
			_columns.add(key);
			_values.put(key, new ArrayList<String>());
		}
		return _values.get(key);
	}
	
	public void putValue(LocalDate date, String key, String value) {
		// create date index if not exists
		final int dateIdx = dateIndexing(date);
		// create columnName if not exists
		final ArrayList<String> vals = columnIndexing(key);
		// set values
		for(int i=vals.size(); i<=dateIdx; i++ )
			vals.add(null);

		vals.set(dateIdx, value);
	}
	
	public void putValuesAt(LocalDate date, Map<String,String> values) {
		final int dateIdx = dateIndexing(date);
		for(Entry<String,String> e : values.entrySet()) {
			// filling
			final ArrayList<String> columns = columnIndexing(e.getKey());
			for(int i=columns.size(); i<=dateIdx; i++)
				columns.add(null);
			columns.set(dateIdx, e.getValue());
		}
	}
	
	
	public String getValue(LocalDate date, String key) {
		if(dateIndexMap.containsKey(date) && _values.containsKey(key)) {
			return _values.get(key).get(dateIndexMap.get(date));
		} else {
			return null;
		}
	}
	
	public String[] getValuesAt(LocalDate date) {
		if(dateIndexMap.containsKey(date)) {
			final int dateIndex = dateIndexMap.get(date); 
			final String[] rets = new String[_columns.size()];
			for(int i=0; i<rets.length; i++) {
				final String col = _columns.get(i);
				rets[i] =  _values.get(col).get(dateIndex);
			}
			return rets;
		} else {
			return null;
		}
	}
	
	public List<String> columns() {
		return this._columns;
	}
	
}
