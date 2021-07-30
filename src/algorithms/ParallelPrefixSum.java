package algorithms;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
//import java.util.LinkedList;

public class ParallelPrefixSum {
	
	protected ForkJoinPool fjp;
	protected int[] array;
	protected int[] outArray;
	protected int SEQUENTIAL_CUTOFF;
	
	public ParallelPrefixSum(int i) {
		this.fjp = new ForkJoinPool();
		this.SEQUENTIAL_CUTOFF = i;
	}
	
	public int[] prefixSum(int[] arr) {
		this.array = arr;
		this.outArray = new int[arr.length];
		Node root = new Node(0, arr.length, null);
		BuildTree r = new BuildTree(root);
		fjp.invoke(r);
		r.join();
				
		TraverseSum t = new TraverseSum(root);
		fjp.invoke(t);
		t.join();
		
//		LinkedList<Node> queue = new LinkedList<>();
//		queue.add(root);
//		while(!queue.isEmpty()) {
//			Node n = queue.poll();
//			System.out.println(n.low + " " + n.high + ": " + n.sum + " | " + n.fromLeft);
//			if(n.leftChild != null) queue.add(n.leftChild);
//			if(n.rightChild != null) queue.add(n.rightChild);
//		}
		
		return outArray;
	}
	
	@SuppressWarnings("serial")
	class TraverseSum extends RecursiveAction {
		Node node;
		
		public TraverseSum(Node n) {
			this.node = n;
		}
		
		@Override
		protected void compute() {
			TraverseSum t1 = null;
			TraverseSum t2 = null;
			
			if(node.rightChild != null) {
				node.rightChild.fromLeft = node.fromLeft + node.leftChild.sum;
				t2 = new TraverseSum(node.rightChild);
				if(node.high - node.low > SEQUENTIAL_CUTOFF) t2.invoke();
				else t2.compute();
			}
			
			if(node.leftChild != null) {
				node.leftChild.fromLeft = node.fromLeft;
				t1 = new TraverseSum(node.leftChild);
				t1.compute();
			}
			
			if(t2 != null && node.high - node.low > SEQUENTIAL_CUTOFF) t2.join();
			if(node.isLeaf) {
				outArray[node.index] = node.fromLeft + node.sum;
			}
		}
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
				node.sum = array[node.low];
				node.isLeaf = true;
				node.index = node.low;
			}
		}
		
	}
	
	class Node {
		int high, low;
		int sum, fromLeft;
		Node parent;
		Node leftChild, rightChild;
		boolean isLeaf;
		int index;
		
		Node(int l, int h, Node parent) {
			this.high = h;
			this.low = l;
			this.parent = parent;
			this.leftChild = null;
			this.rightChild = null;
			this.sum = 0;
			this.fromLeft = 0;
		}
	}
}


