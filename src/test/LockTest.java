package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import locks.BackoffLock;
import locks.BakeryLock;
import locks.DeckersLock;
import locks.FilterLock;
import locks.PetersonLock;
import locks.TASLock;
import locks.TATASLock;

class LockTest {
	
	int numCPU;
	int counter;
	int countTo;
	
	@Test
	@Timeout(10)
	void backoffLockTest() throws InterruptedException {
		for(int r = 0; r < 1000; r++) {
			counter = 0;
			countTo = 10000;
			numCPU = 10;
			BackoffLock lock = new BackoffLock();
			
			Thread[] t = new Thread[numCPU];
			for(int th = 0; th < numCPU; th++) {
				t[th] = new Thread(new Runnable() {
					public void run() {
						for(int i = 0; i < countTo/numCPU; i++) {
							lock.acquire();
							counter++;
							lock.release();
						}
					}
				});
				t[th].start();
			}
			
			for(int th = 0; th < numCPU; th++) {
				t[th].join();
			}
			
			assertEquals(countTo, counter);
		}
	}
	
	@Test
	@Timeout(10)
	void TATASLockTest() throws InterruptedException {
		for(int r = 0; r < 1000; r++) {
			counter = 0;
			countTo = 10000;
			numCPU = 10;
			TATASLock lock = new TATASLock();
			
			Thread[] t = new Thread[numCPU];
			for(int th = 0; th < numCPU; th++) {
				t[th] = new Thread(new Runnable() {
					public void run() {
						for(int i = 0; i < countTo/numCPU; i++) {
							lock.acquire();
							counter++;
							lock.release();
						}
					}
				});
				t[th].start();
			}
			
			for(int th = 0; th < numCPU; th++) {
				t[th].join();
			}
			
			assertEquals(countTo, counter);
		}
	}
	
	@Test
	@Timeout(10)
	void TASLockTest() throws InterruptedException {
		for(int r = 0; r < 1000; r++) {
			counter = 0;
			countTo = 10000;
			numCPU = 10;
			TASLock lock = new TASLock();
			
			Thread[] t = new Thread[numCPU];
			for(int th = 0; th < numCPU; th++) {
				t[th] = new Thread(new Runnable() {
					public void run() {
						for(int i = 0; i < countTo/numCPU; i++) {
							lock.acquire();
							counter++;
							lock.release();
						}
					}
				});
				t[th].start();
			}
			
			for(int th = 0; th < numCPU; th++) {
				t[th].join();
			}
			
			assertEquals(countTo, counter);
		}
	}
	
	@Test
	@Timeout(10)
	void petersonLockTest() {
		for(int r = 0; r < 1000; r++) {
			counter = 0;
			countTo = 10000;
			PetersonLock lock = new PetersonLock();
			
			Thread t1 = new Thread(new Runnable() {
				boolean isThread2 = false;
				public void run() {
					for(int i = 0; i < countTo/2; i++) {
						lock.acquire(isThread2);
						counter++;
						lock.release(isThread2);
					}
				}
			});
			
			Thread t2 = new Thread(new Runnable() {
				boolean isThread2 = true;
				public void run() {
					for(int i = 0; i < countTo/2; i++) {
						lock.acquire(isThread2);
						counter++;
						lock.release(isThread2);
					}
				}
			});
			
			t1.start();
			t2.start();
			
			try {
				t1.join();
				t2.join();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			assertEquals(countTo, counter);
		}
	}
	
	@Test
	@Timeout(10)
	void deckersLockTest() {
		for(int r = 0; r < 1000; r++) {
			counter = 0;
			countTo = 10000;
			DeckersLock lock = new DeckersLock();
			
			Thread t1 = new Thread(new Runnable() {
				boolean isThread2 = false;
				public void run() {
					for(int i = 0; i < countTo/2; i++) {
						lock.acquire(isThread2);
						counter++;
						lock.release(isThread2);
					}
				}
			});
			
			Thread t2 = new Thread(new Runnable() {
				boolean isThread2 = true;
				public void run() {
					for(int i = 0; i < countTo/2; i++) {
						lock.acquire(isThread2);
						counter++;
						lock.release(isThread2);
					}
				}
			});
			
			t1.start();
			t2.start();
			
			try {
				t1.join();
				t2.join();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			assertEquals(countTo, counter);
		}
	}
	
	@Test
	@Timeout(10)
	void filterLockTest() throws InterruptedException {
		for(int run = 0; run < 100; run++) {
			numCPU = 10;
			counter = 0;
			countTo = 100000;
			FilterLock lock = new FilterLock(numCPU);
			
			Thread[] t = new Thread[numCPU];
			for(int th = 0; th < numCPU; th++) {
				final int numID = th;
				t[th] = new Thread(new Runnable() {
					int ID = numID;
					public void run() {
						for(int i = 0; i < countTo/numCPU; i++) {
							lock.acquire(ID);
							counter++;
							lock.release(ID);
						}
					}
				});
				t[th].start();
			}
			
			for(int th = 0; th < numCPU; th++) {
				t[th].join();
			}
			
			assertEquals(countTo, counter);
		}
	}
	
	@Test
	@Timeout(10)
	void bakeryLockTest() throws InterruptedException {
		for(int run = 0; run < 10; run++) {
			numCPU = 10;
			counter = 0;
			countTo = 1000;
			BakeryLock lock = new BakeryLock(numCPU);
			
			Thread[] t = new Thread[numCPU];
			for(int th = 0; th < numCPU; th++) {
				final int numID = th;
				t[th] = new Thread(new Runnable() {
					int ID = numID;
					public void run() {
						for(int i = 0; i < countTo/numCPU; i++) {
							lock.acquire(ID);
							counter++;
							lock.release(ID);
						}
					}
				});
				t[th].start();
			}
			
			for(int th = 0; th < numCPU; th++) {
				t[th].join();
			}
			
			assertEquals(countTo, counter);
		}
	}

}
