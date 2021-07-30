package algorithms;

import locks.BackoffLock;
import locks.SpinningSemaphore;

public class BarrierSandbox {
	
	static int numCPU = 10;
	
	public static void main(String[] args) throws InterruptedException {
		Barrier b = new Barrier();
		Thread[] t = new Thread[numCPU];
		for(int i = 0; i < numCPU; i++) {
			t[i] = new Thread(b.new Threaddy(i));
			t[i].start();
		}
		
		for(int i = 0; i < numCPU; i++) t[i].join();
	}
}

class Barrier {
	SpinningSemaphore barrier0 = new SpinningSemaphore(1);
	SpinningSemaphore barrier1 = new SpinningSemaphore(0);
	BackoffLock mutex = new BackoffLock();
	static int count = 0;
	static int numCPU = 10;
	static int numRuns = 5;
	
	class Threaddy implements Runnable {

		int ID;
		
		public Threaddy(int i) {
			this.ID = i;
		}
		
		@Override
		public void run() {
			for(int run = 0; run < numRuns; run++) {
				System.out.println(ID + ": before barrier  | barrier0: " + barrier0.getS() + ", barrier1: " + barrier1.getS());
				
				mutex.acquire();
				count++;
				if(count == numCPU) {
					barrier0.acquire();
					barrier1.release();
					System.out.println("---------------------------------------------");
				}
				mutex.release();
				
				barrier1.acquire();
				barrier1.release();
				
				System.out.println(ID + ": after barrier   | barrier0: " + barrier0.getS() + ", barrier1: " + barrier1.getS());
				
				mutex.acquire();
				count--;
				if(count == 0) {
					barrier0.release();
					barrier1.acquire();
					System.out.println("---------------------------------------------");
				}
				mutex.release();
				
				barrier0.acquire();
				barrier0.release();
			}
		}
		
	}
}