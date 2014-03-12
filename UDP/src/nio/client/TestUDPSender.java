package nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class TestUDPSender {
	public static void main(String[] args) throws IOException {
		
		DatagramChannel channel = DatagramChannel.open();
		
		channel.send(ByteBuffer.wrap("abc".getBytes()), new InetSocketAddress("localhost",3111));
		
	}
}
