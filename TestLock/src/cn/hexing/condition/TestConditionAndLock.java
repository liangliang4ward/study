package cn.hexing.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestConditionAndLock {
	
	
	ReentrantLock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	
	public TestConditionAndLock(){
		new Thread1().start();
		new Thread2().start();
	}
	
	class Thread1 extends Thread{
		
		@Override
		public void run(){
			
			try {
				lock.lock();
				System.out.println("I got a lock,now I want a single.");
				condition.await();
				System.out.println("I got a single.");
				lock.unlock();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	class Thread2 extends Thread{
		@Override
		public void run(){
			lock.lock();
			System.out.println("I got a lock,now I want send a single");
			condition.signalAll();
			System.out.println("send single succssfull");
			lock.unlock();
		}
	}
	
	
	
	public static void main(String[] args) {
		new TestConditionAndLock();
	}
}
