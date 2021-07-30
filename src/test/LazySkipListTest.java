package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import datastructures.LazySkipList;

import org.junit.jupiter.api.BeforeEach;

class LazySkipListTest {
	
	LazySkipList<Integer> list;
	
	@BeforeEach
	void start() {
		list = new LazySkipList<>(10);
	}
	
	@Test
	void addTest() {
		list.add(5);
		list.add(2);
		list.add(3);
		list.add(1);
		list.add(0);
		list.add(4);
		
		assertEquals("[0, 1, 2, 3, 4, 5]", list.toString());
		
		list.add(8);
		assertFalse(list.add(8));
		list.add(9);
		assertFalse(list.add(9));
		list.add(6);
		list.add(7);
		list.add(10);
		
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", list.toString());
		
		assertTrue(list.contains(0));
		assertTrue(list.contains(4));
		assertTrue(list.contains(10));
		assertFalse(list.contains(11));
		assertFalse(list.contains(12));
		assertFalse(list.contains(-1));
	}
	
	@Test
	void removeTest() {
		list.add(5);
		list.add(2);
		list.add(3);
		list.add(1);
		list.add(0);
		list.add(4);
		
		assertEquals("[0, 1, 2, 3, 4, 5]", list.toString());
		assertTrue(list.contains(4));
		
		assertTrue(list.remove(4));
		assertEquals("[0, 1, 2, 3, 5]", list.toString());
		assertFalse(list.contains(4));
		assertFalse(list.remove(4));
	}
	
	@Test
	void stringTest() {
		LazySkipList<String> stringList = new LazySkipList<>(10);
		stringList.add("test");
		stringList.add("string");
		stringList.add("add");
		assertFalse(stringList.add("add"));
		assertTrue(stringList.contains("add"));
		assertTrue(stringList.remove("add"));
		assertFalse(stringList.contains("add"));
		assertFalse(stringList.remove("add"));
	}
}
