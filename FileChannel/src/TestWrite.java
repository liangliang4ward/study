import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * @author gaoll
 *
 */
public class TestWrite {
	public static void main(String[] args) throws IOException {
		RandomAccessFile rf = new RandomAccessFile("test.txt", "rw");
		FileChannel channel = rf.getChannel();
		ByteBuffer buffer=ByteBuffer.allocate(48);
		int read=channel.read(buffer);
		while(read!=-1){
			buffer.flip();
			while(buffer.hasRemaining())
				System.out.println((char)buffer.get());
			buffer.clear();
			read=channel.read(buffer);
			
		}
		
		
		
		rf.close();
	}
}
