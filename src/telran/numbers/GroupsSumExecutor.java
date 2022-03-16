package telran.numbers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class GroupsSumExecutor extends GroupsSum {

	public GroupsSumExecutor(int[][] groups) {
		super(groups);
		
	}
	@Override
	protected void startTasks(List<FutureTask<Long>> tasks) {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		tasks.forEach(executor::execute);
		executor.shutdown();
	}

}
