package com.fsn.nmg.monster;

/**
 * @author yg.song@nextmediagroup.co.kr
 *
 */
public class RemoteApiException extends Exception {
	private static final long serialVersionUID = -1322557747694739436L;
	
	public final ExType exType;
	
	
	/**
	 * @param type
	 */
	public RemoteApiException(ExType type) {
		this.exType = type;
	}
	
	/**
	 * @author NMG-ygsong
	 *
	 */
	public enum ExType {
		API_LIMIT_EXCEEDS,
		API_ERROR,
		
		NOT_SPECIFIED,
	}

}
