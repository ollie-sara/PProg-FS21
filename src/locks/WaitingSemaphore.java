package locks;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WaitingSemaphore {
	
	AtomicInteger S;
	ConcurrentLinkedQueue<Thread> waitlist;
	
	public WaitingSemaphore(int i) {
		this.S = new AtomicInteger(i);
		this.waitlist = new ConcurrentLinkedQueue<>();
	}
	
	public synchronized void acquire() {
		while(S.get() <= 0) {
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		S.decrementAndGet();
	}
	
	public synchronized void release() {
		S.incrementAndGet();
		notify();
	}
}
