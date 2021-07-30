package datastructures;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class ConcurrentSortedList<T> {
	
	Node<T> head;
	
	public ConcurrentSortedList() {
		this.head = new Node<>(Integer.MIN_VALUE);
	}
	
	public String toString() {
		Node<T> curr = head.next.getReference();
		String out = "[";
		if(curr != null) {
			boolean nextMarked = head.next.isMarked();
			while(curr != null) {
				if(!nextMarked) out += curr.value.toString() + ", ";
				nextMarked = curr.next.isMarked();
				curr = curr.next.getReference();
			}
		}
		out += "]";
		return out;
	}
	
	public boolean add(T val) {
		int key = val.hashCode();
		while(true) {
			Window<T> win = find(val);
			Node<T> pred = win.first;
			Node<T> succ = win.second;
			
			if(succ != null && succ.key == key) return false; // Element already exists in list
			else {
				Node<T> node = new Node<>(val);
				node.next.set(succ, false);
				if(pred.next.compareAndSet(succ, node, false, false)) return true;
			}
		}
	}
	
	public boolean remove(T val) {
		if(val == null) return false;
		int key = val.hashCode();
		while(true) {
			Window<T> win = find(val);
			Node<T> victim = win.second;
			if(victim == null) return false;
			Node<T> pred = win.first;
			Node<T> succ = victim.next.getReference();
			if(victim.key != key) return false;
			else if(!victim.next.compareAndSet(succ, succ, false, true)) continue;	// try mark, fail -> restart
			else {																	// try physically remove, fail -> give up
				pred.next.compareAndSet(victim, succ, false, false);
				return true;
			}
		}
	}
	
	private Window<T> find(T val) {
		Node<T> pred, curr;
		boolean done;
		int key = val.hashCode();
		
		while(true) {
			pred = head;
			curr = pred.next.getReference();
			done = false;
			
			while(!done) {
				Node<T> succ = null;
				while(curr != null && curr.key < key && curr.next != null && !curr.next.isMarked()) {
					pred = curr;
					curr = curr.next.getReference();
					if(curr != null) succ = curr.next.getReference();
					else succ = null;
				}
				if(curr == null) {	// List empty
					return new Window<T>(pred, curr);
				} 
				if(curr.key >= key) {	// Found or passed our queried key
					return new Window<T>(pred, curr);
				} else if(curr.next == null) {	// hit end of the list
					return new Window<T>(pred, curr);
				} else if(curr.next.isMarked()) {	// Fixing required, ie. logically removed stuff still there
					System.out.println("-----------> Had to fix");
					while(curr.next.isMarked() && !done) {
						if(pred.next.compareAndSet(curr, succ, false, false)) {
							curr = succ;
							succ = curr.next.getReference();
						} else {
							done = true;
							continue;
						}
					}
				}
			}
		}
	}
	
	private class Window<E> {
		Node<E> first, second;
		
		Window(Node<E> first, Node<E> second) {
			this.first = first;
			this.second = second;
		}
	}
	
	private class Node<E> {
		E value;
		int key;
		AtomicMarkableReference<Node<E>> next;
		
		Node(int key) {	// Sentinel Constructor
			this.key = key;
			this.next = new AtomicMarkableReference<>(null, false);
			this.value = null;
		}
		
		Node(E val) {	// Default Constructor
			this.key = val.hashCode();
			this.next = new AtomicMarkableReference<>(null, false);
			this.value = val;
		}
	}
}
