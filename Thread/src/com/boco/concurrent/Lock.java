package com.boco.concurrent;

import java.util.concurrent.Semaphore;

class Lock {
	/**
	 * 任务是否需要汇总
	 */
	boolean isJoin = false;

	/**
	 * 计数正在执行的线程数
	 */
	int thread_count;
	
	/**
	 * 线程使用许可证
	 */
	Semaphore semaphore;
	
	Lock(int threadPoolSize){
		semaphore = new Semaphore(threadPoolSize);;
	}

}