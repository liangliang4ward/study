package cn.hexing;

/**
 * 
 * 
 * 项目名称：ThreadDeadLock
 * 类名称：CustomizeThread
 * 类描述：自定义线程类
 * 创建人：Defonds
 * 创建时间：2010-1-26 下午02:29:01
 * 修改人：Defonds
 * 修改时间：2010-1-26 下午02:29:01
 * 修改备注：
 * @version 
 *
 */
public class CustomizeThread extends Thread {

	private ResourceManager resourceManger;//资源管理类的私有引用，通过此引用可以通过其相关接口对资源进行读写
	private int a,b;//将要写入资源的数据
	
	/**
	 * 
	 * 创建一个新的实例 CustomizeThread.
	 * @param resourceManger
	 * @param a
	 * @param b
	 */
	public CustomizeThread(ResourceManager resourceManger,int a,int b){
		this.resourceManger = resourceManger;
		this.a = a;
		this.b = b;
	}
	
	/**
	 * 重写 java.lang.Thread 的 run 方法
	 */
	public void run(){
		/**
		 * 为了演示死锁的出现，这里对资源进行反复读写
		 * 实际业务中可能只读写一次
		 */
		while(true){
			this.resourceManger.read();
			this.resourceManger.write(this.a, this.b);
		}
	}
}
