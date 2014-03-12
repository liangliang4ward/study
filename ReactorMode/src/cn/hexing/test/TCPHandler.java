package cn.hexing.test;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class TCPHandler implements Runnable {

	private final static String module = TCPHandler.class.getName();

	private final SocketChannel sc;

	private final SelectionKey sk;

	private SocketHelper socketHelper; // Socket读写帮助类
	private static final int READING = 0, SENDING = 1;

	private int state = READING;

	public TCPHandler(SelectionKey sk, SocketChannel sc) throws IOException {

		this.sc = sc;

		this.sk = sk;

		socketHelper = new SocketHelper();

	}
	@Override
	public void run() { // 线程run方法

		try {

			if (state == READING)
				read(); // 读取数据

			else if (state == SENDING)
				send(); // 写入数据

		} catch (Exception ex) {

			sk.cancel();

		}

	}

	// 从SocketChannel中读取数据

	private void read() throws Exception {

		try {

			// 从Socket中读取byte[]数组

			byte[] bytes = socketHelper.readSocket(sc);

			if (bytes.length == 0)
				throw new Exception();

			System.out.println(" ge result is :" + new String(bytes));

			state = SENDING;

			sk.interestOps(SelectionKey.OP_WRITE); // 注册新的事件

		} catch (Exception ex) {

			throw new Exception(ex);

		}

	}

	// 向SocketChannel写入数据

	private void send() throws Exception {

		try {

			// 写入测试数据

			String request1 = "come back";

			System.out.println(" send result is :" + request1);

			socketHelper.writeSocket(request1.getBytes(),sc);

			state = READING;

			sk.interestOps(SelectionKey.OP_READ);

		} catch (Exception ex) {

			throw new Exception(ex);

		}

	}

}