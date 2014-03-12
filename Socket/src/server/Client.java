package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Client {
	SocketChannel sc;
	ServerSocketChannel ssc;
	Server server;
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	String peerAddr;
	
	private List<String> sendingList;
	private String msg ="";
	public Client(SocketChannel sc, Server server) {
		sendingList = Collections.synchronizedList(new LinkedList<String>());
		this.sc=sc;
		this.peerAddr=this.sc.socket().getInetAddress()+":"+this.sc.socket().getPort();
		this.server = server;
		this.ssc = server.getServerSocketChannel();
		for(int i=0;i<100;i++){
			msg+=UUID.randomUUID().toString();
		}
		server.connectd(this.peerAddr, this);
		new ReadThread().start();
		new WriteThread().start();
	}
	
	public void close(){
		try {
			if(sc!=null){
				sc.socket().shutdownInput();
				sc.socket().shutdownOutput();
				sc.socket().close();
				server.disconnected(this.peerAddr);
				sc=null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Object messageSingle = new Object();
	
	public void addNewMessage(String msg){
		synchronized(messageSingle){
			sendingList.add(msg);
			messageSingle.notify();
		}

	}
	
	class WriteThread extends Thread{
		
		public void run(){
			this.setName("server.Client-WriteThread");
			while(true){
				
				try {
					while (sendingList.size() == 0 && sc != null) {
						synchronized (messageSingle) {
							messageSingle.wait(50);
						}
					}
					if (sc == null)
						break;
					sendMsg();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
			
		}
	}
	
	int count=0;
	public void sendMsg(){
		sendMsg(msg);
	}
	
	public void sendMsg(String msg){
		try {
			sc.write(ByteBuffer.wrap(msg.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("send msg success "+peerAddr+",msg:"+msg);		
	}
	
	class ReadThread extends Thread{
		
		@Override
		public void run(){
			this.setName("server.Client-ReadThread");
			while(sc!=null){
				int n=0 ;
				try {
					n= sc.read(buffer);
					
					buffer.flip();
				} catch (IOException e) {
					e.printStackTrace();
					close();
				}
				if(n<0){
				}
				
				if(sc==null){
					break;
				}
				if(++count==5){
					sendMsg();
					count=0;
				}
//				System.out.println("echo "+Client.this.peerAddr);
				try {
					sc.write(buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
//				if( buffer.hasRemaining() )
//					buffer.compact();
//				else
					buffer.clear();	
			}
		
		}
	}
	

}
