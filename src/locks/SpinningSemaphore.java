package locks;

import java.util.concurrent.atomic.AtomicInteger;

public class SpinningSemaphore {
	
	AtomicInteger S;
	
	public SpinningSemaphore(int i) {
		this.S = new AtomicInteger(i);
	}
	
	public void acquire() {
		while(true) {
			int o = S.get();
			while(o <= 0) o = S.get();
			if(S.compareAndSet(o, o-1)) return;
		}
	}
	
	public void release() {
		S.incrementAndGet();
	}
	
	public int getS() {
		return S.get();
	}
	
}
