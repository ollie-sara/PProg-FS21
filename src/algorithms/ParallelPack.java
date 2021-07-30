package algorithms;

import java.util.concurrent.RecursiveAction;

public class ParallelPack extends ParallelPrefixSum {
	
	protected Condition<Integer> condition;
	protected int[] fulfillsCondition;
	protected int[] pack;

	public ParallelPack(int i, int[] array, Condition<Integer> cond) {
		super(i);
		this.condition = cond;
		int[] indices = prefixSum(array);
		pack = new int[indices[array.length-1]];
		
		int numCPU = Runtime.getRuntime().availableProcessors();
		Thread[] threads = new Thread[numCPU];
		for(int t = 0; t < numCPU; t++) {
			final int tt = t;
			threads[t] = new Thread(new Runnable() {
				public void run() {
					for(int o = (int) Math.floor((double) array.length/numCPU)*tt; o < ((double) array.length/numCPU)*(tt+1); o++) {
						if(fulfillsCondition[o] == 1) {
							pack[indices[o]-1] = array[o];
						}
					}
				}
			});
			threads[t].start();
		}
		for(int t = 0; t < numCPU; t++)
			try {
				threads[t].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public int[] prefixSum(int[] arr) {
		this.array = arr;
		this.outArray = new int[arr.length];
		this.fulfillsCondition = new int[arr.length];
		Node root = new Node(0, arr.length, null);
		BuildTree r = new BuildTree(root);
		fjp.invoke(r);
		r.join();
				
		TraverseSum t = new TraverseSum(root);
		fjp.invoke(t);
		t.join();
		
		return outArray;
	}

	public int[] getPack() {
		return pack;
	}
	
	@SuppressWarnings("serial")
	class BuildTree extends RecursiveAction {
		Node node;
		
		BuildTree(Node n) {
			this.node = n;
		}
		
		@Override
		protected void compute() {
			if(node.high - node.low > 1) {
				int mid = (int) Math.ceil((node.high + node.low) / 2.0);
				BuildTree b1 = null;
				BuildTree b2 = null;
				
				if(node.high - mid > 0) {
					node.rightChild = new Node(mid, node. high, node);
					b2 = new BuildTree(node.rightChild);
					if(node.high - mid > SEQUENTIAL_CUTOFF) b2.invoke();
					else b2.compute();
				} 
				
				if(mid - node.low > 0) {
					node.leftChild = new Node(node.low, mid, node);
					b1 = new BuildTree(node.leftChild);
					b1.compute();
				}
				
				if(b2 != null && node.high - mid > SEQUENTIAL_CUTOFF) b2.join();
				node.sum = (b1 != null ? node.leftChild.sum : 0) + (b2 != null ? node.rightChild.sum : 0);
			} else {
				node.sum = (condition.fulfillsCondition(array[node.low]) ? 1 : 0);
				node.isLeaf = true;
				node.index = node.low;
				fulfillsCondition[node.index] = node.sum;
			}
		}		
	}
	
	public interface Condition<T> {
		public boolean fulfillsCondition(T o);
	}
}
