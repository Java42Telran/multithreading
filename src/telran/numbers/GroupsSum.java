package telran.numbers;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;
public class GroupsSum {
private int[][] groups;

public GroupsSum(int[][] groups) {
	this.groups = groups;
}
public long computeSum() {
	List<FutureTask<Long>> tasks = getFutureTasks();
	startTasks(tasks);
	return tasks.stream().map(t -> {
		try {
			return t.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return 0L;
		}
	}).mapToLong(n -> n).sum();
}
protected void startTasks(List<FutureTask<Long>> tasks) {
	tasks.stream().map(task -> new Thread(task)).forEach(Thread::start);
	
}
private List<FutureTask<Long>> getFutureTasks() {
	
	return IntStream.range(0, groups.length)
			.mapToObj(i -> new FutureTask<>(new OneGroupSum(groups[i])))
			.toList();
}
}
