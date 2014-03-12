package rxtx.test;

import java.util.concurrent.LinkedBlockingQueue;

public class BlockList {
	public static void main(String[] args) throws InterruptedException {
		LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
		queue.take();
	}
}
