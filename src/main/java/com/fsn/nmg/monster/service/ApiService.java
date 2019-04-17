package com.fsn.nmg.monster.service;

import java.util.function.Function;

import com.fsn.nmg.monster.datamodel.AccessAccount;

/**
 * @author yg.song@nextmediagroup.co.kr
 *
 */
public enum ApiService {
	Facebook((AccessAccount user) -> new ServiceFacebook(user)),
//	Google,
//	Naver,
//	Kakao,
	
	SampleTest((AccessAccount user) -> new ServiceTest()),
	;
	private final Function<AccessAccount,? extends AbstractService> _serviceBuilder;
	
	/**
	 * @param builder
	 */
	private ApiService(Function<AccessAccount,? extends AbstractService> builder) {
		this._serviceBuilder = builder;
	}
	
	/**
	 * @param user
	 * @return
	 */
	public AbstractService serviceContext(AccessAccount user) {
		return this._serviceBuilder.apply(user);
	}

}
