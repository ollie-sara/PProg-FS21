package algorithms;

public class ThreadBattle {
	public static void main(String[] args) {
		Thread t1 = new Thread(new Writer());
		Thread t2 = new Thread(new Deleter());
		t1.start();
		t2.start();
	}
}

class Writer implements Runnable {
	@Override
	public void run() {
		while(true) {
			System.out.print("*");
			System.out.flush();
			try {
				Thread.sleep((int) Math.random()*100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Deleter implements Runnable {
	@Override
	public void run() {
		while(true) {
			System.out.print("\b");
			System.out.flush();
			try {
				Thread.sleep((int) Math.random()*100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
