package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.junit.jupiter.api.Test;

import datastructures.ConcurrentStack;

class ConcurrentStackTest {
	
	ConcurrentStack<String> stack = new ConcurrentStack<>();
	ConcurrentLinkedDeque<String> stackReal = new ConcurrentLinkedDeque<>();
	
	@Test
	void test() throws InterruptedException {
		int threadCount = 1000;
		
		Thread[] t = new Thread[threadCount];
		for(int i = 0; i < threadCount; i++) {
			t[i] = new Thread(new Stacker());
			t[i].setName(i + "");
			t[i].start();
		}
		
		for(int i = 0; i < threadCount; i++) {
			t[i].join();
		}
	}
	
	class Stacker implements Runnable {
		
		@Override
		public void run() {
			String name = Thread.currentThread().getName();
			double action = Math.random();
			for(int i = 0; i < Math.random()*10000; i++) {
				if(action > 0.5) { // push
					stack.push(name);
					stackReal.push(name);
					System.out.println("Pushing: " + name);
				} else { // pop
					String pop = stack.pop();
					String popReal = null;
					try {
						popReal = stackReal.pop();
					} catch(NoSuchElementException e) {}
					assertEquals(popReal, pop);
					System.out.println(name + " popped: " + pop + " | " + popReal);
				}
			}
		}
		
	}

}
