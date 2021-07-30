package datastructures;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentBufferQueue<T> {
	LinkedList<T> list;
	Lock edit;
	Condition nonEmpty, nonFull;
	int capacity;
	int size;
	Node<T> head, tail;
	
	public ConcurrentBufferQueue(int l) {
		list = new LinkedList<>();
		edit = new ReentrantLock();
		nonEmpty = edit.newCondition();
		nonFull = edit.newCondition();
		capacity = l;
		initializeBuffer();
	}
	
	public void enqueue(T val) {
		edit.lock();
		while(size == capacity) {
			try {
				nonFull.await();
			} catch (InterruptedException e) {}
		}
		tail.value = val;
		tail = tail.next;
		size++;
		if(size == 1) nonEmpty.signal();
		edit.unlock();
	}
	
	public T dequeue() {
		edit.lock();
		while(size == 0) {
			try {
				nonEmpty.await();
			} catch (InterruptedException e) {}
		}
		T out = head.value;
		head = head.next;
		size--;
		if(size == capacity-1) nonFull.signal();
		edit.unlock();
		return out;
	}
	
	private void initializeBuffer() {
		edit.lock();
		head = new Node<>();
		Node<T> current = head;
		for(int i = 0; i < capacity; i++) {
			current.next = new Node<>();
			current = current.next;
		}
		current.next = head;
		tail = head;
		edit.unlock();
	}
	
	private class Node<E> {
		E value;
		Node<E> next;
	}
}
