package cn.hexing.netty.test.buffer;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;

public class Test1 {
	public static void main(String[] args) {
		ChannelBuffer buffer = ChannelBuffers.buffer(100);
		buffer.writeByte(100);
		buffer.compareTo(buffer);
		buffer=ChannelBuffers.hexDump("31322313");
		System.out.println(buffer.readByte());
		System.out.println(buffer.readByte());
		System.out.println(buffer.readByte());
		System.out.println(buffer.readByte());
		
	}
}
