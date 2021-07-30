package locks;

import java.util.concurrent.atomic.AtomicBoolean;

public class TATASLock {
	
	AtomicBoolean flag;
	
	public TATASLock() {
		this.flag = new AtomicBoolean();
	}
	
	public void acquire() {
		do {
			while(flag.get());
		} while(flag.getAndSet(true));
	}
	
	public void release() {
		flag.set(false);
	}
}
