package datastructures;

import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentUnboundedQueue<T> {
	
	AtomicReference<Node<T>> head, tail;
	
	public ConcurrentUnboundedQueue() {
		Node<T> sentinel = new Node<>(null);
		this.head = new AtomicReference<>(sentinel);
		this.tail = new AtomicReference<>(sentinel);
	}
	
	public T dequeue() {
		while(true) {
			Node<T> first = head.get();
			Node<T> last = tail.get();
			Node<T> next = first.next.get();
			if(first == last) {	// Does the list appear empty?
				if(next == null) return null;	// Is it actually empty?
				else {	// If the tail just couldn't get updated, fix it yourself
					tail.compareAndSet(last, next);
				}
			} else {	// No? Ok dequeue
				T value = next.value;
				if(head.compareAndSet(first, next)) return value; // Try updating head, if it doesn't work: retry
			}
		}
	}
	
	public void enqueue(T val) {
		Node<T> newNode = new Node<>(val);
		while(true) {
			System.out.println("Enqueuing " + val);
			Node<T> last = tail.get();
			Node<T> next = last.next.get();
			if(next == null) {
				if(!last.next.compareAndSet(next, newNode)) continue;
				tail.compareAndSet(last, newNode);
				return;
			} else {
				tail.compareAndSet(last, next);
			}
		}
	}
	
	private class Node<E> {
		E value;
		AtomicReference<Node<E>> next;
					
		Node(E val) {
			this.value = val;
			this.next = new AtomicReference<>(null);
		}
	}
 	
}
