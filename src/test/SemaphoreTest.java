package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import locks.FairSemaphore;
import locks.SpinningSemaphore;
import locks.WaitingSemaphore;

class SemaphoreTest {
	
	int numCPU, maxCS;
	AtomicInteger inCS;
	
	@Test
	void fairSemaphoreTest() throws InterruptedException {
		for(int r = 0; r < 1000; r++) {
			numCPU = 10;
			inCS = new AtomicInteger(0);
			maxCS = 5;
			FairSemaphore sem = new FairSemaphore(maxCS);
			
			Thread[] t = new Thread[numCPU];
			for(int th = 0; th < numCPU; th++) {
				t[th] = new Thread(new Runnable() {
					public void run() {
						for(int i = 0; i < 1000; i++) {
							sem.acquire();
							int o = inCS.incrementAndGet();
							assertFalse((o > maxCS));
							inCS.decrementAndGet();
							sem.release();
						}
					}
				});
				t[th].start();
			}
			
			for(int th = 0; th < numCPU; th++) {
				t[th].join();
			}
		}
	}
	
	@Test
	void waitingSemaphoreTest() throws InterruptedException {
		for(int r = 0; r < 100; r++) {
			numCPU = 10;
			inCS = new AtomicInteger(0);
			maxCS = 5;
			WaitingSemaphore sem = new WaitingSemaphore(maxCS);
			
			Thread[] t = new Thread[numCPU];
			for(int th = 0; th < numCPU; th++) {
				t[th] = new Thread(new Runnable() {
					public void run() {
						for(int i = 0; i < 1000; i++) {
							sem.acquire();
							int o = inCS.incrementAndGet();
							assertFalse((o > maxCS));
							inCS.decrementAndGet();
							sem.release();
						}
					}
				});
				t[th].start();
			}
			
			for(int th = 0; th < numCPU; th++) {
				t[th].join();
			}
		}
	}
	
	@Test
	void spinningSemaphoreTest() throws InterruptedException {
		for(int r = 0; r < 100; r++) {
			numCPU = 10;
			inCS = new AtomicInteger(0);
			maxCS = 5;
			SpinningSemaphore sem = new SpinningSemaphore(maxCS);
			
			Thread[] t = new Thread[numCPU];
			for(int th = 0; th < numCPU; th++) {
				t[th] = new Thread(new Runnable() {
					public void run() {
						for(int i = 0; i < 1000; i++) {
							sem.acquire();
							int o = inCS.incrementAndGet();
							assertFalse((o > maxCS));
							inCS.decrementAndGet();
							sem.release();
						}
					}
				});
				t[th].start();
			}
			
			for(int th = 0; th < numCPU; th++) {
				t[th].join();
			}
		}
	}

}
