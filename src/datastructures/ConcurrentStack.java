package datastructures;

import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentStack<T> {
	
	AtomicReference<Node<T>> top;
	Node<T> end;
	
	public ConcurrentStack() {
		Node<T> newNode = new Node<>();
		top = new AtomicReference<>();
		top.set(newNode);
		end = newNode;
	}
	
	public void push(T val) {
		Node<T> newNode = new Node<>(val);
		do {
			newNode.pred = top.get();
		} while(!top.compareAndSet(newNode.pred, newNode));
	}
	
	public T pop() {
		Node<T> toPop = null;
		do {
			toPop = top.get();
			if(toPop.value == null) return null;
		} while(!top.compareAndSet(toPop, toPop.pred));
		return toPop.value;
	}
	
	private class Node<E> {
		E value;
		Node<E> pred;
		
		Node() { // Sentinel Constructor
			value = null;
			pred = null;
		} 
		
		Node(E val) { // Default Constructor
			value = val;
		}
	}
}
