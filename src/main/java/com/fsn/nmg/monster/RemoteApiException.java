package com.fsn.nmg.monster;

public class RemoteApiException extends Exception {
	private static final long serialVersionUID = -1322557747694739436L;
	
	public final ExType exType;
	public RemoteApiException(ExType type) {
		this.exType = type;
	}
	
	public enum ExType {
		API_LIMIT_EXCEEDS,
	}

}
