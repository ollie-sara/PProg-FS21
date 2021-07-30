package algorithms;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort {
	ForkJoinPool fjp;
	int[] toSort;
	int sequential_cutoff;
	
	public ParallelMergeSort(int[] toSort, int sequential_cutoff) {
		this.fjp = new ForkJoinPool();
		this.toSort = toSort;
		this.sequential_cutoff = sequential_cutoff;
	}
	
	public void sort() {
		Sort s = new Sort(0, toSort.length);
		fjp.invoke(s);
		s.join();
	}
	
	@SuppressWarnings("serial")
	class Sort extends RecursiveAction {		
		int low, high;
		
		public Sort(int l, int h) {
			this.low = l;
			this.high = h;
		}
		
		@Override
		protected void compute() {
			int mid = (high+low)/2;
			
			if(high-low <= 1) {
				return;
			} else if(high-low == 2) {
				if(toSort[high-1] > toSort[low]) {
					return;
				} else {
					int temp = toSort[low];
					toSort[low] = toSort[high-1];
					toSort[high-1] = temp;
					return;
				}
			} else if(high-low <= sequential_cutoff) {
				Sort h1 = new Sort(low, mid);
				Sort h2 = new Sort(mid, high);
				h1.compute();
				h2.compute();
				merge(mid);
				return;
			} else {
				Sort h1 = new Sort(low, mid);
				Sort h2 = new Sort(mid, high);
				h1.invoke();
				h2.compute();
				h1.join();
				merge(mid);
				return;
			}
		}
		
		private void merge(int mid) {
			int it1 = low;
			int it2 = mid;
			int it3 = 0;
			int[] sorted = new int[high-low];
			
			while(it1 != mid && it2 != high) {
				if(toSort[it1] > toSort[it2]) {
					sorted[it3++] = toSort[it2++];
				} else {
					sorted[it3++] = toSort[it1++];
				}
			}
			
			while(it1 != mid) {
				sorted[it3++] = toSort[it1++];
			}
			
			while(it2 != high) {
				sorted[it3++] = toSort[it2++];
			}
			
			for(int i = 0; i < sorted.length; i++) {
				toSort[i+low] = sorted[i];
			}
			
		}
	}
	
}