package com.fsn.nmg.monster.service;

import java.util.function.Function;

import com.fsn.nmg.monster.datamodel.AccessAccount;

public enum ApiService {
	Facebook((AccessAccount user) -> new ServiceFacebook(user)),
//	Google,
//	Naver,
//	Kakao,
	
	SampleTest((AccessAccount user) -> new ServiceTest()),
	;
	private final Function<AccessAccount,? extends AbstractService> _serviceBuilder;
	
	private ApiService(Function<AccessAccount,? extends AbstractService> builder) {
		this._serviceBuilder = builder;
	}
	
	public AbstractService serviceContext(AccessAccount user) {
		return this._serviceBuilder.apply(user);
	}

}
