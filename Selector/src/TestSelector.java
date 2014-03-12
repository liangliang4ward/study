import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


public class TestSelector {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		sc.connect(new InetSocketAddress("127.0.0.1", 1234));
		while(!sc.finishConnect()){
			System.out.println("unfinished");
		}
		String msg = "1234567890";
		ByteBuffer bb = ByteBuffer.allocate(48);
		bb.put(msg.getBytes());
		bb.flip();
		
		while(bb.hasRemaining()){
			sc.write(bb);
		}
		Thread.sleep(10000);

		
		
	}
}
