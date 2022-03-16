package telran.numbers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GroupsSumTest {
	private static final long NUMBERS_PER_GROUP = 10000;
	private static final long N_GROUPS = 10000;
	static int [][] largeGroups;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		largeGroups = Stream.generate(() -> getOneGroup())
				.limit(N_GROUPS).toArray(int[][]::new);
	}
	static int[] getOneGroup() {
		return  ThreadLocalRandom.current().ints().limit(NUMBERS_PER_GROUP).toArray();
	}

	@Test
	void functionalTest() {
		int[][] groups = {{1,1},{1,1},{1,1},{1,1},{1,1}};
		GroupsSum groupsSum = new GroupsSum(groups);
		assertEquals(10, groupsSum.computeSum());
	}
	@Test
	void performanceTestThreads() {
		GroupsSum groupsSum = new GroupsSum(largeGroups);
		System.out.println(groupsSum.computeSum());
	}
	@Test
	void performanceTestStreams() {
		System.out.println(Arrays.stream(largeGroups).mapToLong(a ->
		Arrays.stream(a).asLongStream().sum()).sum());
		
	}
	@Test
	void performanceTestExecutor() {
		GroupsSum groupsSum = new GroupsSumExecutor(largeGroups);
		System.out.println(groupsSum.computeSum());
	}

}
