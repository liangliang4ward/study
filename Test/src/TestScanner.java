import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class TestScanner {
	public static void main(String[] args) {
		byte[] message = "abcdefg sdf sdfsdf".getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(message);
		Scanner s = new Scanner(new InputStreamReader(bais));
		String str = s.next();
		System.out.println(str);
		System.out.println(s.next());
		
	}
}
