package example.compress;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	public static void main(String[] args) throws IOException {
		
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress("127.0.0.1", 3111));
		ExecutorService service = Executors.newCachedThreadPool();
		while(true){
			
			Socket client = server.accept();
			service.execute(new ComressProtocol(client));
		}
		
	}
}
