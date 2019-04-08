package com.fsn.nmg.monster;

public class SampleWorker extends ApiWorker {
	
	public SampleWorker(String target) {
		super(target);
	}

	protected static final int interval = 1000;

	@Override
	void willRun() {
		System.out.println(String.format("Will run %s", this.targetId));

	}

	@Override
	void proceed() throws InterruptedException {
		Thread.sleep(interval);

	}

	@Override
	void onSuccess() {
		System.out.println(String.format("Completed %s", this.targetId));
		
	}

	@Override
	void onError(Exception ex) {
		System.err.println(String.format("Error %s : %s", this.targetId, ex.getMessage()));
	}

}
