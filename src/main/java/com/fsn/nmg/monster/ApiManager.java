package com.fsn.nmg.monster;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class ApiManager {
	
	protected final TreeMap<String, String> stack;
	protected final ArrayList<Thread> running;
	protected String groupName;
	protected int maxThreads;
	protected int limitWindow;
	protected long limitInterval;
	protected long lastIntervalChecked;
	protected int trafficUsage;
	
	public ApiManager(String group, int threads, int limits, long interval) {
		this.groupName = group;
		this.maxThreads = threads;
		this.limitWindow = limits;
		this.limitInterval = interval;
				
		this.stack = new TreeMap<String, String>();
		this.running = new ArrayList<Thread>();
		
		this.clear();
	}
	
	protected int countTraffic(int estimatedUsage) {
		final long now = System.currentTimeMillis();
		if(lastIntervalChecked + limitInterval < now) {
			this.lastIntervalChecked = now;
			this.trafficUsage = this.limitWindow;
		}
		return this.limitWindow - estimatedUsage;
	}
	
	protected int countRunnings() {
		final ArrayList<Thread> dels = new ArrayList<Thread>();
		
		for(Thread th : this.running) {
			if(!th.isAlive()) {
				dels.add(th);
			} 
		}
		
		if(0<dels.size()) {
			this.running.removeAll(dels);
		}
		
		return this.running.size();
	}
	
	public int runners() {
		return running.size();
	}
	
	public void clear() {
		this.lastIntervalChecked = 0;
		this.trafficUsage = 0;
		this.stack.clear();
		this.running.clear();
	}
	
	public void proceed(int estUsage) throws RemoteApiException {
		// return if stack empty
		if(stack.size()<=0) return;
		
		// check api window interval
		if(estUsage <= 0 || this.countTraffic(estUsage) < 0) {
			throw new RemoteApiException(RemoteApiException.ExType.API_LIMIT_EXCEEDS);
		}
				
		// check if max thread limit
		if(countRunnings() < maxThreads) {
			// pop stack and run
			final Entry<String, String> entry = stack.firstEntry();
			stack.remove(entry.getKey());
			
			// start thread
			final ApiWorker worker = new SampleWorker(entry.getValue());
			final Thread apiThread = new Thread(worker);
			
			running.add(apiThread);
			apiThread.start();
			this.trafficUsage -= estUsage;
			
		}
	}
	
	public boolean load(String key, String val) {
		if(stack.containsKey(key)) return false;
		
		stack.put(key, val);
		return true;
	}
	
	public boolean load(Entry<String, String> ent) {
		return this.load(ent.getKey(), ent.getValue());
	}
	
	public int loads(Map<String, String> vals) {
		int cnt = 0;
		for(Entry<String, String> e : vals.entrySet()) {
			if(this.load(e)) cnt += 1;
		}
		
		return cnt;
	}
}
