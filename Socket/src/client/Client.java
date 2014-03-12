package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * 连接服务端，向服务端发送消息
 * @author gaoll
 *
 */
public class Client {
	private Socket socket ;
	private String ip;
	private int port;
	ByteBuffer readBuffer;
	String msg ="";
	
	public Client(String ip,int port){
		this.ip = ip;
		this.port=port;
		readBuffer=ByteBuffer.allocate(1024);
		for(int i=0;i<100;i++){
			msg+=UUID.randomUUID().toString();
		}
		new ReadThread().start();
		new WriteThread().start();

	}
	
	
	public void send() throws IOException{
		System.out.println("send msg:"+msg);
		send(msg);
	}
	public void send(String msg) throws IOException{
		socket.getOutputStream().write(msg.getBytes());
		socket.getOutputStream().flush();
		System.out.println("send msg:"+msg);
	}
	
	
	
	public static void main(String[] args) {
		new Client("127.0.0.1",3111);
	}
	private boolean connect() {
		try {
			socket = new Socket();
			InetSocketAddress ar = new InetSocketAddress(ip,port);
			socket.setSoTimeout(1000);
			socket.connect(ar,1000);
		} catch (SocketException e) {
			socket=null;
			return false;
		} catch (IOException e) {
			socket=null;
			return false;
		}
		return true;
	}
	
	/**
	 * 向服务器发送消息
	 */
	class WriteThread extends Thread{
		@Override
		public void run(){
			this.setName("client.Client-WriteThread");
			while(true){
				if(socket!=null && socket.isConnected()){
					try {
						send();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					} catch (IOException e) {
						socket =null;
					}
				}
			}			
		}
	}
	
	class ReadThread extends Thread{
		@Override
		public void run(){
			this.setName("client.Client-ReadThread");
			while(true){
				while(null ==socket){
					connect();
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				doReceive();
			}
		}
	}
	
	public void doReceive(){
		int len = readBuffer.remaining();
		if( len == 0 ){
			readBuffer.position(0);
			readBuffer.clear();
			len = readBuffer.remaining();
		}
		byte[] in = readBuffer.array();
		int off = readBuffer.position();
		int n=0;
		try {
			n = socket.getInputStream().read(in,off,len);
		} catch (IOException e) {
			return ;
		}
		if( n<=0 ) return ;
		readBuffer.position(off+n);
		readBuffer.flip();
		
		handBuffer();
	}
	int count=0;
	private void handBuffer() {
//		while(readBuffer.hasRemaining())
//			readBuffer.get();
//		try {
//			send();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		if(++count==5){
			try {
				send();
				count=0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		readBuffer.clear();
	}
	
}
