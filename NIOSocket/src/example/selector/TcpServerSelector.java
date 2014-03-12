package example.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class TcpServerSelector {
	public static void main(String[] args) throws IOException {
		
		Selector selector = Selector.open();
		
		ServerSocketChannel server = ServerSocketChannel.open();
		server.socket().bind(new InetSocketAddress(3111));
		server.configureBlocking(false);
		server.register(selector, SelectionKey.OP_ACCEPT);
		
		TcpProtocol protocol = new EchoProtocol(1024);
		
		while(true){
			
			if(selector.select(3000)==0){
				System.out.println(" . ");
				continue;
			}
			
			Iterator<SelectionKey> its = selector.selectedKeys().iterator();
			while(its.hasNext()){
				
				SelectionKey key = its.next();
				
				if(key.isAcceptable()){
					protocol.handleAccept(key);
				}
				
				if(key.isReadable()){
					protocol.handleRead(key);
				}
				
				if(key.isWritable()){
					protocol.handleWrite(key);
				}
				
				its.remove();
				
			}
		}
		
		
		
	}
}
