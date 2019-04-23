package com.fsn.nmg.monster.datamodel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author yg.song@nextmediagroup.co.kr
 *
 */
@SuppressWarnings("rawtypes")
public class Creative extends AbstractModel<AdGroup,AbstractModel> {
	
	private static final String[] ATTRIBUTE_KEYS = new String[] {
			
	};
	
	protected final ArrayList<String> _columns;
	protected final HashMap<String, ArrayList<String>> _values;
	
	protected final HashMap<LocalDate, Integer> dateIndexMap;
	
	protected boolean _campaignActive;
	protected boolean _groupActive;
	protected boolean _active;
	
	
	/**
	 * @param account
	 * @param campaign
	 * @param adgroup
	 * @param id
	 */
	protected Creative(String id) {
		super(id);
		
		this._columns = new ArrayList<String>();
		this._values = new HashMap<String, ArrayList<String>>();
		this.dateIndexMap = new HashMap<LocalDate, Integer>();
	}
	
	/**
	 * public alias of get parent
	 * @return
	 */
	public AdGroup getGroup() {
		return getParent();
	}

	
	
	/**
	 * @param from
	 * @param till
	 */
	public void setPeriod(LocalDate from, LocalDate till) {
		LocalDate cs = from;
		while(cs.compareTo(till) <=0) {
			if(!dateIndexMap.containsKey(cs)) {
				dateIndexing(cs);
			}
			cs = cs.plus(1, ChronoUnit.DAYS);
		}
	}
	
	public boolean isCampaignActive() {
		return this._campaignActive;
	}
	
	public boolean isGroupActive() {
		return this._campaignActive && this._groupActive;
	}
	/**
	 * @return
	 */
	public boolean isActive() {
		return this._campaignActive && this._groupActive && this._active;
	}
	
	/**
	 * @return
	 */
	public boolean hasSet() {
		return !this._values.isEmpty(); 
	}
	
	/**
	 * @param active
	 */
	public void setActive(boolean active) {
		this._active = active;
	}
	
	public void setGroupActive(boolean active) {
		this._groupActive = active;
	}
	
	public void setCampaignActive(boolean active) {
		this._campaignActive = active;
	}
	
	/**
	 * @param date
	 * @return
	 */
	protected int dateIndexing(LocalDate date) {
		if(!dateIndexMap.containsKey(date))
			dateIndexMap.put(date, dateIndexMap.size());
		return dateIndexMap.get(date);
	}
	
	/**
	 * @param key
	 * @return
	 */
	protected ArrayList<String> columnIndexing(String key) {
		if(!_values.containsKey(key)) {
			_columns.add(key);
			_values.put(key, new ArrayList<String>());
		}
		return _values.get(key);
	}
	
	/**
	 * @param date
	 * @param key
	 * @param value
	 */
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
	
	/**
	 * @param date
	 * @param values
	 */
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
	
	
	/**
	 * @param date
	 * @param key
	 * @return
	 */
	public String getValue(LocalDate date, String key) {
		if(dateIndexMap.containsKey(date) && _values.containsKey(key)) {
			return _values.get(key).get(dateIndexMap.get(date));
		} else {
			return null;
		}
	}
	
	/**
	 * @param date
	 * @return
	 */
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
	
	/**
	 * @return
	 */
	public List<String> columns() {
		return this._columns;
	}

	@Override
	protected String[] initiateAttributes() {
		return ATTRIBUTE_KEYS;
	}

	@Override
	protected AbstractModel createChild(String id) {
		return null;
	}
	
}
