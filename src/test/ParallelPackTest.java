package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import algorithms.ParallelPack.Condition;
import algorithms.ParallelPack;

class ParallelPackTest {
	
	@BeforeEach
	void setUp() {
		System.out.println("------------------------------");
	}
	
	@Test
	void trivialTest() {
		Condition<Integer> largerOrEquals10 = new Condition<Integer>() {
			@Override
			public boolean fulfillsCondition(Integer o) {
				if(o >= 10) return true;
				else return false;
			}
		};
		
		int[] array = {5, 12, 6, 50, 19, 2, 3, 52, 14};
		int[] expected = {12, 50, 19, 52, 14};
		
		ParallelPack pack = new ParallelPack(1, array, largerOrEquals10);
		assertArrayEquals(expected, pack.getPack());
	}
	
	@Test
	void random20() {
		randomN(20, "random20");
	}
	
	@Test
	void random1000() {
		randomN(1000, "random1000");
	}
	
	@Test
	void random10K() {
		randomN(10000, "random10K");
	}
	
	@Test
	void random100K() {
		randomN(100000, "random100K");
	}
	
	@Test
	void randomMIL() {
		randomN(1000000, "randomMIL");
	}
	
	void randomN(int N, String name) {
//		Condition<Integer> topHalf = new Condition<Integer>() {
//			@Override
//			public boolean fulfillsCondition(Integer o) {
//				if(o.intValue() >= N/2) return true;
//				else return false;
//			}
//		};
		Condition<Integer> lastDigitIsEven = new Condition<Integer>() {
			@Override
			public boolean fulfillsCondition(Integer o) {
				if(o.intValue()%2 == 0) return true;
				else return false;
			}
		};
		
		int[] array = new int[N];
		for(int i = 0; i < N; i++) {
			array[i] = (int) ((double) Math.random()*N);
		}
		
		LinkedList<Integer> temp = new LinkedList<>();
		for(int i = 0; i < array.length; i++) {
			if(lastDigitIsEven.fulfillsCondition(array[i])) temp.add(array[i]);
		}
		int[] expected = new int[temp.size()];
		Iterator<Integer> it = temp.iterator();
		for(int i = 0; i < temp.size(); i++) {
			expected[i] = it.next();
		}
		
		long startTime = System.nanoTime();
		ParallelPack pack = new ParallelPack(200, array, lastDigitIsEven);
		int[] result = pack.getPack();
		double timeNeeded = (double) Math.round((System.nanoTime() - startTime)/10000)/100;
		System.out.println(name + " required " + timeNeeded + "ms");
		
		for(int i = 0; i < Math.min(10, expected.length); i++) {
			System.out.println(result[i] + " ?= " + expected[i]);
		}
		assertArrayEquals(expected, result);
	}

}
