package com.boco.test;

import java.util.List;

import com.boco.concurrent.Executer;

public class Test {
	public static void main(String[] args) {
		Executer e = new Executer(50,true);
		long start = System.currentTimeMillis();
		for(int i = 0; i < 100; i++){
			e.fork(new MyJob(i));
		}
		List<Result> l = e.join();
		for (Result result : l) {
			System.out.println(result + " \t " + result.getCode());
		}
		long time = System.currentTimeMillis() - start;
		System.out.println("result size: " + l.size() + " time: " + time);
		e.shutdown();
	}
}