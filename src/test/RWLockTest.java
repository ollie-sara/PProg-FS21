package test;

import org.junit.jupiter.api.Test;

import locks.RWLock;

class RWLockTest {
	
	RWLock lock;
	String toRead;
	int actions;
	int R, W;
	boolean printDebug;

	@Test
	void test() {
		lock = new RWLock();
		toRead = "start";
		actions = 100;
		R = 20;
		W = 5;
		printDebug = true;
		lock.printDebug = printDebug;
		
		Thread[] readers = createReaders(R);
		Thread[] writers = createWriters(W);
		
		for(Thread r : readers) r.start();
		for(Thread w : writers) w.start();
		
		try {
			for(Thread r : readers) r.join();
			for(Thread w : writers) w.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private Thread[] createReaders(int i) {
		Thread[] out = new Thread[i];
		for(int o = 0; o < i; o++) {
			out[o] = new Thread(new Reader());
			out[o].setName("Reader-" + o);
		}
		return out;
	}
	
	private Thread[] createWriters(int i) {
		Thread[] out = new Thread[i];
		for(int o = 0; o < i; o++) {
			out[o] = new Thread(new Writer());
			out[o].setName("Writer-" + o);
		}
		return out;
	}
	
	class Writer implements Runnable {
		@Override
		public void run() {
			lock.acquire_write();
			toRead = Thread.currentThread().getName();
			if(printDebug) System.out.println(Thread.currentThread().getName() + " writing: " + toRead);
			lock.release_write();
		}
	}
	
	class Reader implements Runnable {
		@Override
		public void run() {
			for(int i = 0; i < actions; i++) {
				lock.acquire_read();
				if(printDebug) System.out.println(Thread.currentThread().getName() + " reading: " + toRead);
				lock.release_read();
			}
		}
	}
}
