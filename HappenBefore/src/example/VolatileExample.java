package example;

public class VolatileExample extends Thread {
	// 设置类静态变量,各线程访问这同一共享变量
	private static volatile boolean flag = false;

	// 无限循环,等待flag变为true时才跳出循环
	public void run() {
		long t1 = System.currentTimeMillis();
		while (!flag) {
		}
		;
		long t2 = System.currentTimeMillis();
		System.out.println("time:"+(t2-t1));
	}

	public static void main(String[] args) throws Exception {
		new VolatileExample().start();
		// sleep的目的是等待线程启动完毕,也就是说进入run的无限循环体了
		Thread.sleep(100);
		flag = true;
	}
}