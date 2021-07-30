package algorithms;

public class ParallelCounter {
	public static void main(String[] args) {
		Thread[] threads = new Thread[12];
		for(int i = 0; i < threads.length; i++) {
			System.out.println("Started calling thread...");
			threads[i] = new Thread(new CounterTask(0,10,1));
			System.out.println("...successfully called " + threads[i].getName());
		}
		for(Thread t : threads) {
			t.start();
		}
		for(Thread t : threads)
			try {
				System.out.println("Joining " + t.getName() + "...");
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				System.out.println(t.getName() + " is done :)");
			}
	}
}

class CounterTask implements Runnable {
	
	int start, end, step;
	boolean negative;
	
	CounterTask(int str, int e, int stp) {
		this.start = str;
		this.end = e;
		this.step = stp;
		this.negative = (stp < 0);
	}
	
	@Override
	public void run() {
		if(!this.negative) {
			for(int i = this.start; i < this.end; i += this.step) {
				System.out.println(Thread.currentThread().getName() + "/" + Thread.currentThread().getId() + ": " + i);
			}
		} else {
			for(int i = this.start; i > this.end; i += this.step) {
				System.out.println(Thread.currentThread().getName() + "/" + Thread.currentThread().getId() + ": " + i);
			}
		}
	}
	
}
