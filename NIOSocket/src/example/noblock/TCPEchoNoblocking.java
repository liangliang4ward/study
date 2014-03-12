package example.noblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class TCPEchoNoblocking {
	public static void main(String[] args) throws IOException {
		
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		
		if(!channel.connect(new InetSocketAddress(3111))){
			while(!channel.finishConnect()){
				System.out.println("unfinish");
			}
		}
		
		byte[] value = "absdfsdfffff".getBytes();
		int length = value.length;
		ByteBuffer buffer = ByteBuffer.wrap(value);
		int bytesWrite=0;
		while(bytesWrite<length){
			
			if(buffer.hasRemaining()){
				bytesWrite+=channel.write(buffer);
			}
			
		}
	}
}
