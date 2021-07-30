package locks;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class BakeryLock {
	
	volatile int N;
	AtomicIntegerArray tickets;
	AtomicIntegerArray flag;
	
	public BakeryLock(int N) {
		this.N = N;
		this.tickets = new AtomicIntegerArray(N);
		this.flag = new AtomicIntegerArray(N);
	}
	
	public void acquire(int me) {
		flag.set(me, 1);
		tickets.set(me, getMax());
		while(hasSmaller(me));
	}
	
	public void release(int me) {
		flag.set(me, 0);
	}
	
	private int getMax() {
		int max = 0;
		for(int i = 0; i < N; i++) {
			max = Math.max(max, tickets.get(i));
		}
		return max+1;
	}
	
	private boolean hasSmaller(int me) {
		for(int i = 0; i < N; i++) {
			if(i != me && flag.get(i) == 1 && smallerThan(me, i)) return true;
		}
		return false;
	}
	
	private boolean smallerThan(int me, int oth) {
		if(tickets.get(me) > tickets.get(oth)) {
			return true;
		} else if(tickets.get(me) == tickets.get(oth) && me > oth) {
			return true;
		} else {
			return false;
		}
	}

}
