package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import algorithms.ParallelMergeSort;

class ParallelMergeSortTest {
	
	long startTime;
	
	@BeforeEach
	void setUp() {
		startTime = System.nanoTime();
		System.out.println("---------------------------------------");
	}
	
	@AfterEach
	void setDown(TestInfo info) {
		int timeUsed = (int) Math.round((System.nanoTime() - startTime)/1000000.0);
		String name = info.getDisplayName();
		System.out.println(name + " required " + timeUsed + "ms");
	}

	@Test
	void trivialTest() {
		int[] array1 = {9, 3, 4, 0, 8, 5, 1, 6, 2, 7};
		int[] sorted1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		ParallelMergeSort pms = new ParallelMergeSort(array1, 2);
		pms.sort();
		System.out.println(Arrays.toString(array1));
		System.out.println(Arrays.toString(sorted1));
		assertArrayEquals(sorted1, array1);
	}
	
	@Test
	void random1000() {
		randomN(1000);
	}
	
	@Test
	void random1000000() {
		randomN(1000000);
	}
	
	@Test
	void random10000000() {
		randomN(10000000);
	}
 	
	void randomN(int N) {
		int[] unsorted1 = new int[N];
		int[] unsorted2 = new int[N];
		int[] sorted = new int[N];
		
		for(int i = 0; i < N; i++) {
			unsorted1[i] = i;
			unsorted2[i] = i;
			sorted[i] = i;
		}
		
		for(int i = 0; i < 4*N; i++) {
			int o1 = (int) Math.floor(Math.random()*N);
			int o2 = (int) Math.floor(Math.random()*N);
			swap(unsorted1, o1, o2);
			swap(unsorted2, o1, o2);
		}
		
		long localStartTime = System.nanoTime();
		ParallelMergeSort pms1 = new ParallelMergeSort(unsorted1, 1000);
		pms1.sort();
		int timeUsed = (int) Math.round((System.nanoTime() - localStartTime)/1000000.0);
		System.out.println("Parallel: " + timeUsed + "ms");
		
		localStartTime = System.nanoTime();
		ParallelMergeSort pms2 = new ParallelMergeSort(unsorted2, N);
		pms2.sort();
		timeUsed = (int) Math.round((System.nanoTime() - localStartTime)/1000000.0);
		System.out.println("Sequential: " + timeUsed + "ms");
		
		assertArrayEquals(unsorted1, sorted);
		assertArrayEquals(unsorted2, sorted);
	}
	
	private void swap(int[] a, int x, int y) {
		int temp = a[x];
		a[x] = a[y];
		a[y] = temp;
	}

}
