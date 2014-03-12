package thread.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Worker {
	
	public static void start(final Executor parent,final Runnable runnable){
		parent.execute(runnable);
	}
	
	public static void main(String[] args) {
		Worker.start(Executors.newCachedThreadPool(), new Runnable() {
			@Override
			public void run() {
				System.out.println("¹þ¹þ");
			}
		});
	}
}
