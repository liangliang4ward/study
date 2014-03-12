package cn.hexing.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
	
	
	ReentrantLock lock =new ReentrantLock();
	Condition condition=lock.newCondition();
	
	public Test(){
	}
	
}
