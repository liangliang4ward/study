/**
 *  VolatileTest.java
 *
 *  Copyright ekupeng,Inc. 2012   
 */


/**
 * @ClassName: VolatileTest
 * @Description: Volatile测试
 * @author Emerson emsn1026@gmail.com
 * @date 2012-11-29 下午06:57:44
 * @version V1.0
 * 
 */
public class VolatileTest extends Thread {

	// 非volatile标志
	private static boolean flag1 = false;
	// volatile标志
	private static volatile boolean flag2 = false;

	private int i = 0;

	public void run() {
		//Object o = new Object();

		//synchronized (o) {
			/*
			 * 注释1
			 */
			while (!flag1) {
				i++;
                                //注意 ： System.out.println(i);
				/*
				 * 注释2
				 */
				if (flag2) {
					System.out.println("over:" + i);
					break;
				}
			}
		//}
	}

	public static void main(String[] args) {

		VolatileTest t = new VolatileTest();
		t.start();

		try {
			Thread.currentThread().sleep(2000);
			// 先更改flag1
			t.flag1 = true;
			/*
			 * 注释3
			 */
//			Thread.currentThread().sleep(1000);
			// 将flag2置为true，如果有机会进入if(flag2),则将退出循环
			t.flag2 = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
