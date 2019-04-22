package com.fsn.nmg.monster.datamodel;

import java.util.TreeMap;

/**
 * @author yg.song@nextmediagroup.co.kr
 *
 */
public class AccessAccount extends AbstractModel<AbstractModel,Account> {
	private static final String ATTRIBUTE_ACCESS = "access";
	private static final String[] ATTRIBUTE_KEYS = new String[] {
			ATTRIBUTE_ACCESS,
	};
//	protected final String _access;
	protected String _id;
	protected TreeMap<String, Account> _accounts;
	
	/**
	 * @param access
	 * @param id
	 */
	public AccessAccount(String access, String id) {
		super(id);
		
		this._id = id;
		setAttribute(ATTRIBUTE_ACCESS, access);
		
		this._accounts = new TreeMap<String, Account>();
	}
	
	@Override
	protected String[] initiateAttributes() {
		return ATTRIBUTE_KEYS;
	}
	
	@Override
	protected Account createChild(String id) {
		return Account.get(id);
	}
	
	/**
	 * @return
	 */
	public String getAccessToken() {
		return getAttribute(ATTRIBUTE_ACCESS);
	}

	

	
}
