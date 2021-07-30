package locks;

public class DeckersLock{
	
	volatile int turn;
	volatile boolean want1;
	volatile boolean want2;
	
	public DeckersLock() {
		this.turn = 1;
		this.want1 = false;
		this.want2 = false;
	}
	
	public void acquire(boolean isThread2) {
		if(!isThread2) {
			want1 = true;
			while(want2) {
				if(turn == 2) {
					want1 = false;
					while(turn != 1);
					want1 = true;
				}
			}
		} else {
			want2 = true;
			while(want1) {
				if(turn == 1) {
					want2 = false;
					while(turn != 2);
					want2 = true;
				}
			}
		}
	}

	public void release(boolean isThread2) {
		if(!isThread2) {
			turn = 2;
			want1 = false;
		} else {
			turn = 1;
			want2 = false;
		}
	}

}
