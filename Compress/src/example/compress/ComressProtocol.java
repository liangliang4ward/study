package example.compress;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ComressProtocol implements Runnable{

	private static final int BUFSIZE=1024;
	private Socket clientSocket;
	
	public ComressProtocol(Socket socket){
		this.clientSocket = socket;
	}
	
	public static void handleCompressClient(Socket socket){
		try {
			InputStream fis = socket.getInputStream();
			GZIPOutputStream gzipos = new GZIPOutputStream(socket.getOutputStream());
			byte[] buffer = new byte[BUFSIZE];
			int bytesRead;
			while((bytesRead=fis.read(buffer))!=-1){
				gzipos.write(buffer, 0, bytesRead);
			}
			gzipos.finish();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void run() {
		handleCompressClient(clientSocket);
	}

}
