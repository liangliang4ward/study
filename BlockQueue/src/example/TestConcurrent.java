package example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TestConcurrent {

	private final static int LOOPSIZE=100000;
	public static void main(String[] args) throws InterruptedException {
//		List<String> values = Collections.synchronizedList(new ArrayList<String>(100000));
		List<String> values = new LinkedList<String>();
		for(int i=0;i<LOOPSIZE;i++){
			values.add(i+"");
		}
		
		long t1 = System.currentTimeMillis();
		WriteThread w = new WriteThread(values);
		ReadThread r = new ReadThread(values);
		w.start();
		r.start();
		w.join();
		r.join();
		long t2 = System.currentTimeMillis();
		System.out.println("use time:"+(t2-t1));
	}
	
	static class WriteThread extends Thread{
		List<String> values;
		public WriteThread(List<String> values){
			this.values = values;
		}
		@Override
		public void run(){
			int i=0;
			while(i++<LOOPSIZE){
				values.add(""+i);
			}
			System.out.println("add");
		}
	}
	
	static class ReadThread extends Thread{
		List<String> values;
		public ReadThread(List<String> values){
			this.values = values;
		}
		@Override
		public void run(){
			int i=0;
			while(i++<LOOPSIZE){
				values.remove(i);
			}
			System.out.println("remove");

		}
	}
	
}
