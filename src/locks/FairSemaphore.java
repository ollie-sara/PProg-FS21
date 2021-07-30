package locks;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairSemaphore {
	
	AtomicInteger S;
	LinkedList<Condition> waitlist;
	Lock lock;
	
	public FairSemaphore(int i) {
		this.S = new AtomicInteger(i);
		this.lock = new ReentrantLock();
		this.waitlist = new LinkedList<>();
	}
	
	public void acquire() {
		lock.lock();
		while(S.get() <= 0) {
			try {
				Condition cond = lock.newCondition();
				waitlist.add(cond);
				cond.await();
			} catch(InterruptedException e) {}
		}
		S.decrementAndGet();
		lock.unlock();
	}
	
	public void release() {
		lock.lock();
		S.incrementAndGet();
		Condition cond = waitlist.poll();
		if(cond != null) cond.signal();
		lock.unlock();
	}
}
