package com.boco.concurrent;

import java.util.concurrent.Callable;

/**
 * @author xueliang
 *
 * @param <T> 任务的执行结果类型
 */
public abstract class Job<E> implements Callable<E> {

	// 锁
	private Lock lock = null;

	void setLock(Lock lock) {
		this.lock = lock;
	}

	public E call() throws Exception {
		E result = null;
		try {
			result = this.execute();// 执行子类具体任务
		} catch (Exception e) {
			e.printStackTrace();
		}
		//判断是否需要将结果汇总
		if(lock.isJoin){
			synchronized (lock) {
				// 处理完业务后，任务结束，递减线程数，同时唤醒主线程
				lock.thread_count--;
				lock.notifyAll();
			}
		}
		lock.semaphore.release();//释放线程使用许可
		return result;
	}

	/**
	 * 业务处理函数
	 */
	public abstract E execute();

}