package cn.hexing;

import java.util.concurrent.atomic.AtomicBoolean;

public class SpinLock {
	
	
	private AtomicBoolean state = new AtomicBoolean(false);
	public void lock(){

		while(state.getAndSet(true)){}
		
	}
	
	public void unlock(){

	       state.set(false);
	}
	
}
