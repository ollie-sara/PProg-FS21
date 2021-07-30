package locks;

import java.util.concurrent.atomic.AtomicBoolean;

public class BackoffLock {
	
	AtomicBoolean flag = new AtomicBoolean();
	
	public void acquire() {
		Backoff backoff = null;
		while(true) {
			while(flag.get());
			if(!flag.getAndSet(true)) {
				return;
			} else {
				if(backoff == null) {
					backoff = new Backoff(1, 100);
				}
				try {
					backoff.backoff();
				} catch (InterruptedException e) {
					continue;
				}
			}
		}
	}
	
	public void release() {
		flag.set(false);
	}
	
	class Backoff {
		int limit;
		int MIN_DELAY, MAX_DELAY;
		
		public Backoff(int MIN, int MAX) {
			this.MIN_DELAY = MIN;
			this.MAX_DELAY = MAX;
			this.limit = MIN;
		}
		
		public void backoff() throws InterruptedException {
			int interval = (int) Math.random()*limit;
			if(limit*2 < MAX_DELAY) limit *= 2;
			Thread.sleep(interval);
		}
	}
	
}
