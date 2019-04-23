package com.fsn.nmg.monster.datamodel;

public class AdGroup extends AbstractModel<Campaign, Creative> {
	private static final String ATTRIBUTE_KEY_STATUS = "status";
	private static final String ATTRIBUTE_KEY_PERIOD_FROM = "from";
	private static final String ATTRIBUTE_KEY_PERIOD_TILL = "till";
	
	private static final String[] ATTRIBUTE_KEYS = new String[] {
		ATTRIBUTE_KEY_STATUS,
		ATTRIBUTE_KEY_PERIOD_FROM,
		ATTRIBUTE_KEY_PERIOD_TILL,
	};
	
	protected AdGroup(String id) {
		super(id);
	}
	
	/**
	 * public alias of get parent
	 * @return
	 */
	public Campaign getCampaign() {
		return getParent();
	}
	
	
	/**
	 * public alias of add child
	 * @param id
	 * @return
	 */
	public Creative creative(String id) {
		return addChild(id);
	}
	


	@Override
	protected String[] initiateAttributes() {
		return ATTRIBUTE_KEYS;
	}




	@Override
	protected Creative createChild(String id) {
		return new Creative(id);
	}

}
