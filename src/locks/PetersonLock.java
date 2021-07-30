package locks;

public class PetersonLock {
	volatile boolean flag1;
	volatile boolean flag2;
	volatile int victim;
	
	public PetersonLock() {
		this.flag1 = false;
		this.flag2 = false;
		this.victim = 1;
	}
	
	public void acquire(boolean is2) {
		if(!is2) {
			flag1 = true;
			victim = 1;
			while(flag2 && victim == 1);
		} else {
			flag2 = true;
			victim = 2;
			while(flag1 && victim == 2);
		}
	}
	
	public void release(boolean is2) {
		if(!is2) {
			flag1 = false;
		} else {
			flag2 = false;
		}
	}
}
