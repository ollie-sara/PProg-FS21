package datastructures;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LazySkipList<T> {
	
	int MAX_LEVEL;
	Node<T> head, tail;
	
	public LazySkipList(int max) {
		this.MAX_LEVEL = max;
		this.head = new Node<T>(Integer.MIN_VALUE);
		this.tail = new Node<T>(Integer.MAX_VALUE);
		for(int i = 0; i < this.head.next.length; i++) {
			this.head.next[i] = tail;
		}
	}
	
	public int find(T val, Node<T>[] preds, Node<T>[] succs) {
		int key = val.hashCode();
		int lFound = -1;
		
		Node<T> pred = head; 
		
		for(int l = MAX_LEVEL; l >= 0; l--) {
			Node<T> curr = pred.next[l];
			
			while(curr.key < key) { // Iterate till we find a node that has equal or higher key
				pred = curr;
				curr = curr.next[l];
			}
			
			if(curr.key == key && lFound == -1) { // Is the node we found equal in key? Have we found it yet?
				lFound = l; // If yes&no, then we found our node at level l
			}
			
			preds[l] = pred; // Save what we found
			succs[l] = curr;
		}
		
		return lFound;
	}
	
	public String toString() {
		String out = "[";
		Node<T> curr = head;
		while(curr.next[0] != tail) {
			if(curr != head) out += curr.item.toString() + ", ";
			curr = curr.next[0];
		}
		if(curr != head) out += curr.item.toString();
		out += "]";
		return out;
	}
	
	@SuppressWarnings("unchecked")
	public boolean contains(T val) {
		Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL+1];
		Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL+1];
		
		int lFound = find(val, preds, succs);
		if(lFound != -1 && !succs[lFound].marked) return true;
		else return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean add(T val) {
		int topLevel = getRandomLevel();
		Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL+1];
		Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL+1];
		
		while(true) {
			int lFound = find(val, preds, succs);
			
			if(lFound != -1) {
				Node<T> found = succs[lFound];
				if(!found.marked) {
					while(!found.fullyLinked) {}
					return false;
				}
			}
			
			int highestLocked = -1;
			try {
				Node<T> pred, succ;
				boolean valid = true;
				
				for(int l = 0; valid && (l <= topLevel); l++) {	// LOCK ALL PREDECESSORS
					pred = preds[l];
					succ = succs[l];
					pred.lock();
					highestLocked = l;
					valid = !pred.marked && !succ.marked && pred.next[l] == succ;
				}
				
				if(!valid) continue;
				Node<T> newNode = new Node<>(val, topLevel);
				for(int i = 0; i <= topLevel; i++) {	// INSERT NEW NODE
					preds[i].next[i] = newNode;
					newNode.next[i] = succs[i];
				}
				newNode.fullyLinked = true;
				return true;
			} finally {
				for(int l = 0; l <= highestLocked; l++) {
					preds[l].unlock();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean remove(T val) {
		Node<T> victim = null;
		int topLevel = -1;
		boolean isMarked = false;
		Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL+1];
		Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL+1];
		
		while(true) {
			int lFound = find(val, preds, succs);
			if(lFound != -1) victim = succs[lFound];
			
			if(isMarked || (lFound != -1 && victim.topLevel == lFound && victim.fullyLinked && !victim.marked)) {
				if(!isMarked) {
					topLevel = victim.topLevel;
					victim.lock();
					if(victim.marked) {
						victim.unlock();
						return false;
					} else {
						victim.marked = true;	// LOGICAL REMOVE (MARK)
						isMarked = true;
					}
				}
				
				int highestLocked = -1;
				try {
					boolean valid = true;
					for(int i = 0; i <= topLevel && valid; i++) {	// LOCK ALL PREDECESSORS
						preds[i].lock();
						highestLocked = i;
						valid = !preds[i].marked && preds[i].next[i] == victim;
					}
					if(!valid) continue;
					
					for(int i = 0; i <= topLevel; i++) {	// PHYSICAL REMOVE (SET NEXT WITH LOCK)
						preds[i].next[i] = victim.next[i];
					}
					
					victim.unlock();
					return true;					
				} finally {
					for(int i = 0; i <= highestLocked; i++) {
						preds[i].unlock();
					}
				}
			} else return false;			
		}
	}
	
	private int getRandomLevel() {
		int out = (int) Math.random()*(MAX_LEVEL-1)+1;
		if(out == MAX_LEVEL) out--;
		return out;
	}
	
	private class Node<E> {
		
		Lock lock;
		int key;
		E item;
		Node<E>[] next;
		volatile boolean marked, fullyLinked;
		int topLevel;
		
		// Sentinel Constructor
		@SuppressWarnings("unchecked")
		public Node(int key) { 
			this.lock = new ReentrantLock();
			this.marked = false;
			this.fullyLinked = false;
			this.topLevel = MAX_LEVEL;
			this.key = key;
			this.next = (Node<E>[]) new Node[MAX_LEVEL+1];
		}
		
		// Key Constructor
		@SuppressWarnings("unchecked")
		public Node(E val, int height) {
			this.lock = new ReentrantLock();
			this.item = val;
			this.key = val.hashCode();
			this.next = (Node<E>[]) new Node[height+1];
			this.topLevel = height;
		}
		
		public void lock() {
			lock.lock();
		}
		
		public void unlock() {
			lock.unlock();
		}
		
	}
	
}
