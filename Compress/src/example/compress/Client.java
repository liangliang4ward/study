package example.compress;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private static int BUFSIZE=256;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		String filename="abc.txt";
		
		
		FileInputStream fis =  new FileInputStream(filename);
		
		FileOutputStream fos =  new FileOutputStream(filename+".gz");

		Socket socket = new Socket("127.0.0.1", 3111);
		
		sendBytes(socket,fis);
		
		InputStream inStream = socket.getInputStream();
		
		int bytesRead;
		byte[] buffer = new byte[BUFSIZE];
		while((bytesRead=inStream.read(buffer))!=-1){
			fos.write(buffer, 0, bytesRead);
			System.out.print("R");
		}
			
		System.out.println();
		
		socket.close();
		fis.close();
		fos.close();
		inStream.close();
		
	}

	private static void sendBytes(Socket socket, FileInputStream fis) throws IOException {
		OutputStream outStream = socket.getOutputStream();
		int bytesRead;
		byte[] buffer = new byte[BUFSIZE];
		while((bytesRead=fis.read(buffer))!=-1){
			outStream.write(buffer, 0, bytesRead);
			System.out.print("W");
		}
		socket.shutdownOutput();
	}
}
