package com.fsn.nmg.monster;

public abstract class ApiWorker implements Runnable {
	protected String targetId;
	
	protected ApiWorker(String target) {
		this.targetId = target;
	}
	
	abstract void willRun();
	abstract void proceed() throws Exception;
	abstract void onSuccess();
	abstract void onError(Exception ex);
	
	
	public void run() {
		//
		try {
			this.willRun();
		
			this.proceed();			
		} catch(Exception ex) {
			this.onError(ex);
		}
		
		this.onSuccess();
		
	}
}
