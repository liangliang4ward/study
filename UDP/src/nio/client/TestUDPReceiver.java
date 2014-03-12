package nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class TestUDPReceiver {
	public static void main(String[] args) throws IOException {
		
		DatagramChannel channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(3111));
		
		ByteBuffer buffer = ByteBuffer.allocate(50);
		channel.receive(buffer);
		System.out.println(buffer.position());
		
		
	}
}
