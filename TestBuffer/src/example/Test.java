package example;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class Test {
	public static void main(String[] args) {
		
		ByteBuffer buffer = ByteBuffer.allocate(50);

		//init:
		//	buffer.capacity() =50;
		//	buffer.limit() = 50;
		//	buffer.position = 0;
		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());
		System.out.println(buffer.position());
		//flip: (limit=position,position=0)
		//	buffer.limit()=0;
		//	buffer.position=0;
		buffer.flip();
		System.out.println(buffer.limit());
		System.out.println(buffer.position());
		
		//clear:(reset)
		buffer.clear();
		buffer.putInt(4);
		//after putInt:
		//	limit =50;
		//	position=4;
		System.out.println(buffer.limit());
		System.out.println(buffer.position());
		//flip:
		//	limit=4;
		//	position=0;
		buffer.flip();
		System.out.println(buffer.getInt());
		System.out.println(buffer.position());
		
		buffer.clear();
		
		buffer.put("abc".getBytes());
		buffer.compact();
	}
}
