package locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RWLock {
	int readers, writers;
	int readersWaiting;
	int writersWaiting;
	int readersQueued;
	Lock lock;
	Condition mayRead;
	Condition mayWrite;
	public boolean printDebug = false;
	
	public RWLock() {
		this.readers = 0;
		this.writers = 0;
		this.readersQueued = 0;
		this.readersWaiting = 0;
		this.writersWaiting = 0;
		this.lock = new ReentrantLock();
		this.mayRead = lock.newCondition();
		this.mayWrite = lock.newCondition();
	}
	
	public void acquire_read() {
		lock.lock();
		readersWaiting++;
		while(writers > 0 || (writersWaiting > 0 && readersQueued <= 0)) {
			if(printDebug) System.out.println(Thread.currentThread().getName() + " waiting");
			try { mayRead.await(); } catch(InterruptedException e) {}
			if(printDebug) System.out.println(Thread.currentThread().getName() + " awaking");
		}
		readersWaiting--;
		readersQueued--;
		readers++;
		lock.unlock();
	}
	
	public void release_read() {
		lock.lock();
		readers--;
		if(!(readers > 0 || readersQueued > 0 || writers > 0)) mayWrite.signalAll();
		if(!(writers > 0 || (writersWaiting > 0 && readersQueued <= 0))) mayRead.signalAll();
		lock.unlock();
	}
	
	public void acquire_write() {
		lock.lock();
		writersWaiting++;
		while(readers > 0 || readersQueued > 0 || writers > 0) {
			if(printDebug) System.out.println(Thread.currentThread().getName() + " waiting");
			try { mayWrite.await(); } catch(InterruptedException e) {}
			if(printDebug) System.out.println(Thread.currentThread().getName() + " awaking");
		}
		writersWaiting--;
		writers++;
		lock.unlock();
	}
	
	public void release_write() {
		lock.lock();
		writers--;
		readersQueued = readersWaiting;
		if(printDebug) System.out.println("At least " + readersQueued + " readers will follow");
		if(!(readers > 0 || readersQueued > 0 || writers > 0)) mayWrite.signalAll();
		if(!(writers > 0 || (writersWaiting > 0 && readersQueued <= 0))) mayRead.signalAll();
		lock.unlock();
	}
}
