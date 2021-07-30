package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import algorithms.ParallelPrefixSum;

class ParallelPrefixSumTest {
	
	@BeforeEach
	void setUp() {
		System.out.println("---------------------------------------");
	}
	
	@Test
	void trivialTest() {
		int[] array = {1,1,1,1,1,1,1,1,1,1};
		int[] solution = {1,2,3,4,5,6,7,8,9,10};
		
		ParallelPrefixSum pps = new ParallelPrefixSum(500);
		array = pps.prefixSum(array);
		assertArrayEquals(solution, array);
	}
	
	@Test
	void random1000() {
		randomN(1000, "random1000");
	}
	
	@Test
	void randomMIL() {
		randomN(1000000, "randomMIL");
	}
	
	void randomN(int N, String name) {
		int[] array = new int[N];
		for(int i = 0; i < N; i++) {
			array[i] = (int) Math.random()*N;
		}
		
		ParallelPrefixSum pps = new ParallelPrefixSum(500);
		
		long startTime = System.nanoTime();
		int[] prefixSum = pps.prefixSum(array);
		double timeNeededParallel = (double) Math.round((System.nanoTime() - startTime) / 10000)/100;
		System.out.println(name + " required parallel: " + timeNeededParallel + "ms");
		
		startTime = System.nanoTime();
		int[] expected = new int[N];
		fillPrefixSum(array, N, expected);
		double timeNeededSequential = (double) Math.round((System.nanoTime() - startTime) / 10000)/100;
		System.out.println(name + " required sequentially: " + timeNeededSequential + "ms");
		assertArrayEquals(expected, prefixSum);
		
	}
	
	void fillPrefixSum(int arr[], int n, int prefixSum[]) {
		prefixSum[0] = arr[0];
		for (int i = 1; i < n; ++i)
			prefixSum[i] = prefixSum[i - 1] + arr[i];
	}
}
