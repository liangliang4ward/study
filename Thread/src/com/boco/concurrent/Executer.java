package com.boco.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 并行计算框架
 * @author xueliang
 * 例子：
 * 创建10个线程,true表示需要汇总结果
 * Executer e = new Executer(10, true);
 * for(...){
 * 	  Job<Result> job = new MyJob();
 *    //Job<List<Result>> job = new MyJob();
 * 	  e.fork(job);//将任务派发给executer
 * }
 * 将结果汇总后返回
 * List<Result> obj = e.join();
 * e.shutdown();
 */
public class Executer {
	// 存储任务的执行结果
	private List<Future> futres = new ArrayList<Future>();
	// 条件队列锁,以及线程计数器
	private Lock lock = null;
	// 线程池
	private ExecutorService pool = null;
	
	/**
	 * 创建并发执行器
	 * @param threadPoolSize 执行任务的线程数
	 * @param isJoin 是否需要将任务结果汇总
	 */
	public Executer(int threadPoolSize, boolean isJoin) {
		pool = Executors.newFixedThreadPool(threadPoolSize);
		lock = new Lock(threadPoolSize);
		lock.isJoin = isJoin;
	}

	/**
	 * 任务派发
	 * @param <E> 任务返回值类型
	 * @param job
	 */
	public <E> void fork(Job<E> job) {
		// 设置同步锁
		job.setLock(lock);
		// 获取许可，如果没有可用线程许可，则一直等待
		lock.semaphore.acquireUninterruptibly();
		// 将任务派发给线程池去执行
		Future future = pool.submit(job);
		// 判断是否需要汇总结果
		if(lock.isJoin){
			futres.add(future);
			// 增加线程数
			synchronized (lock) {
				lock.thread_count++;
			}
		}
	}

	/**
	 * 汇集任务结果
	 * @param <E>
	 * @return E类型的集合
	 */
	public <E> List<E> join() {
		synchronized (lock) {
			while (lock.thread_count > 0) {// 检查线程数，如果为0，则表示所有任务处理完成
				// System.out.println("threadCount: "+THREAD_COUNT);
				try {
					// 如果任务没有全部完成，则挂起。等待完成的任务给予通知
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		List list = new ArrayList();
		// 取出每个任务的处理结果，汇总后返回
		for (Future future : futres) {
			try {
				// 因为任务都已经完成，这里直接get
				Object result = future.get();
				if (result != null) {
					if (result instanceof Collection)
						list.addAll((Collection) result);
					else
						list.add(result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 关闭所有任务
	 */
	public void shutdown(){
		if(pool != null)
			pool.shutdown();
	}
}