package locks;

import java.util.concurrent.atomic.AtomicBoolean;

public class TASLock {
	
	AtomicBoolean flag;
	
	public TASLock() {
		this.flag = new AtomicBoolean();
		this.flag.set(false);
	}
	
	public void acquire() {
		while(flag.getAndSet(true));
	}
	
	public void release() {
		flag.set(false);
	}
	
}
