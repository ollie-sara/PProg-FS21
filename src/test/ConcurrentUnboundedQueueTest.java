package test;

import org.junit.jupiter.api.Test;

import datastructures.ConcurrentUnboundedQueue;

class ConcurrentUnboundedQueueTest {
	
	ConcurrentUnboundedQueue<Integer> queue;
	int actors = 5;
	int actions = 10;

	@Test
	void test() throws InterruptedException {
		queue = new ConcurrentUnboundedQueue<>();
		Thread[] t = new Thread[actors];
		for(int i = 0; i < actors; i++) {
			t[i] = new Thread(new Actor());
			t[i].start();
		}
		
		for(int i = 0; i < actors; i++) t[i].join();
	}
	
	private class Actor implements Runnable {

		@Override
		public void run() {
			for(int i = 0; i < actions; i++) {
				double action = Math.random();
				if(action > 0.5) {	// ENQUEUE
					int rand = (int) (Math.random()*20);
					queue.enqueue(rand);
				} else {	// DEQUEUE
					Integer out = queue.dequeue();
					if(out != null) System.out.println("Dequeued " + out.intValue());
				}
			}
		}
		
	}

}
