package cn.hexing;

/**
 * 
 * 
 * 项目名称：ThreadDeadLock
 * 类名称：DeadLockApp
 * 类描述：死锁程序入口
 * 创建人：Defonds
 * 创建时间：2010-1-26 下午02:37:27
 * 修改人：Defonds
 * 修改时间：2010-1-26 下午02:37:27
 * 修改备注：
 * @version 
 *
 */
public class DeadLockApp {

	public static void main(String[] args) {
		
		/**
		 * 死锁演示线程初始化
		 */
		ResourceManager resourceManager = new ResourceManager();
		CustomizeThread customizedThread0 = new CustomizeThread(resourceManager,1,2);
		CustomizeThread customizedThread1 = new CustomizeThread(resourceManager,2,4);
		
		/**
		 * 死锁演示线程启动
		 */
		customizedThread0.start();
		customizedThread1.start();
	}

}
