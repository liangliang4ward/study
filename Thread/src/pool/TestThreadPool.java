package pool;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {

	private static int produceTaskSleepTime = 2;
	private static int consumeTaskSleepTime = 2000;
	private static int produceTaskMaxNumber = 9;

	public static void main(String[] args) {

		// 构造一个线程池
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 4, 10,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1));
		for (int i = 1; i <= 10; i++) {
			try {
				// 产生一个任务，并将其加入到线程池
				String task = "task@ " + i;
				System.out.println("put " + task);
				threadPool.execute(new ThreadPoolTask(task));
				// 便于观察，等待一段时间
				Thread.sleep(produceTaskSleepTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class ThreadPoolTask implements Runnable, Serializable {
		private static final long serialVersionUID = 0;
		// 保存任务所需要的数据
		private Object threadPoolTaskData;
		
		ThreadPoolTask(Object tasks) {
			this.threadPoolTaskData = tasks;
			Runtime.getRuntime().addShutdownHook(new Thread(){
				@Override
				public void run(){
					System.out.println("dispose");
				}
			});
		}

		public void run() {
			// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
			System.out.println("start .." + threadPoolTaskData);
			try {
				// // 便于观察，等待一段时间
			} catch (Exception e) {
				e.printStackTrace();
			}
			threadPoolTaskData = null;
		}

		public Object getTask() {
			return this.threadPoolTaskData;
		}
	}
}