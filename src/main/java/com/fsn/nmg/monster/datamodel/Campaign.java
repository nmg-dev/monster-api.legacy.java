package com.fsn.nmg.monster.datamodel;

public class Campaign extends AbstractModel<Account, AdGroup> {
	private static final String ATTRIBUTE_KEY_STATUS = "status";
	private static final String[] ATTRIBUTE_KEYS = new String[] {
		ATTRIBUTE_KEY_STATUS,
	};

	protected Campaign(String id) {
		super(id);
	}


	@Override
	protected String[] initiateAttributes() {
		return ATTRIBUTE_KEYS;
	}


	@Override
	protected AdGroup createChild(String id) {
		return new AdGroup(id);
	}
	
	/**
	 * public alias of get parent
	 * @return
	 */
	public Account getAccount() {
		return getParent();
	}
	
	
	/**
	 * public alias of add child
	 * @param groupId
	 * @return
	 */
	public AdGroup adgroup(String groupId) {
		return addChild(groupId);
	}

}
