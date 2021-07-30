package test;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import datastructures.ConcurrentSortedList;

class ConcurrentSortedListTest {

	ConcurrentSortedList<Integer> list = new ConcurrentSortedList<>();
	boolean done = false;
	boolean[] doners;
	int actions = 100;
	int threads = 3;
	
	@Test
	void test() throws InterruptedException {
		Thread[] t = new Thread[threads];
		Thread reader = new Thread(new Reader());
		doners = new boolean[threads];
		for(int i = 0; i < t.length; i++) {
			t[i] = new Thread(new Lister("Lister-"+i, i));
			t[i].start();
		}
		reader.start();
		
		for(int i = 0; i < t.length; i++) t[i].join();
		reader.join();
	}
	
	class Reader implements Runnable {
		@Override
		public void run() {
			while(!done) {
				System.out.println(list.toString());
				try {
					Thread.sleep((int) (Math.random()*10)+10);
				} catch(InterruptedException e) {}
				
				done = true;
				for(boolean b : doners) done = done && b;
			}
		}
	}
	
	class Lister implements Runnable {
		HashSet<Integer> added = new HashSet<>();
		String name;
		int ID;
		
		public Lister(String n, int i) {
			this.name = n;
			this.ID = i;
		}
		
		@Override
		public void run() {
			for(int i = 0; i < actions; i++) {
				double action = Math.random();
				if(action > 0.5 || added.size() == 0) { // ADD
					int toAdd = (int) (Math.random()*10);
					System.out.println(name + " adding " + toAdd);
					list.add(toAdd);
					added.add(toAdd);
				} else {								// REMOVE
					int toRem = added.iterator().next();
					System.out.println(name + " removing " + toRem);
					added.remove(toRem);
					list.remove(toRem);
				}
				try {
					Thread.sleep((int) (Math.random()*10)+10);
				} catch(Exception e) {}
			}
			doners[ID] = true;
		}
	}

}
