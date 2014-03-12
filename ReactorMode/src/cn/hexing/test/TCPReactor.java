package cn.hexing.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;

public class TCPReactor implements Runnable {

	final Selector selector;
	final ServerSocketChannel serverSocket;

	public static void main(String[] args) throws IOException {

		Executors.newCachedThreadPool().execute(new TCPReactor(10002));

	}

	TCPReactor(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		InetSocketAddress address = new InetSocketAddress(
				InetAddress.getLocalHost(), port);
		serverSocket.socket().bind(address);

		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector,
				SelectionKey.OP_ACCEPT);

		sk.attach(new Acceptor(selector, serverSocket));
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {

			try {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while (it.hasNext()) {
					dispatch(it.next());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());
		if (r != null) {
			r.run();
		}
	}

	class Acceptor implements Runnable {
		Selector selector;
		ServerSocketChannel serverSocket;

		public Acceptor(Selector selector, ServerSocketChannel serverSocket) {
			this.selector = selector;
			this.serverSocket = serverSocket;
		}

		public void run() {
			try {
				SocketChannel sc = serverSocket.accept();
				
				if(sc!=null){
					
					sc.configureBlocking(false);
					
					SelectionKey sk = sc.register(selector, 0);
					sk.interestOps(SelectionKey.OP_READ);
					
					selector.wakeup();
					
					sk.attach(new TCPHandler(sk, sc));
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
