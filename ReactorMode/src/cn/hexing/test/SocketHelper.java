package cn.hexing.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketHelper {

	public byte[] readSocket(SocketChannel sc) {
		ByteBuffer bb = ByteBuffer.allocate(1000);
		try {
			sc.read(bb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bb.flip();
		bb.compact();

		return bb.array();
	}

	public void writeSocket(byte[] bytes, SocketChannel sc) {
		try {
			sc.write(ByteBuffer.wrap(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
