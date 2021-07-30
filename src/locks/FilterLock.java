package locks;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class FilterLock {
	int N;
	AtomicIntegerArray level;
	AtomicIntegerArray victim;
	
	public FilterLock(int N) {
		this.N = N;
		this.level = new AtomicIntegerArray(N);
		this.victim = new AtomicIntegerArray(N);
	}
	
	private boolean someoneStandingBeforeMe(int lev, int me) {
		for(int i = 0; i < N; i++) {
			if(i != me && level.get(i) >= lev) return true;
		}
		return false;
	}
	
	public void acquire(int me) {
		for(int i = 1; i < N; i++) {
			victim.set(i, me);
			level.set(me, i);
			while(someoneStandingBeforeMe(i, me) && victim.get(i) == me);
		}
	}
	
	public void release(int me) {
		level.set(me, 0);
	}
}
