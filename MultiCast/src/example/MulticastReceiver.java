package example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver {
	public static void main(String[] args) throws IOException {
		MulticastSocket mc = new MulticastSocket();
		mc.joinGroup(InetAddress.getByName(""));
		byte[] msg = new byte[3];
		mc.receive(new DatagramPacket(msg, 3));
		System.out.println(new String(msg));
	}
}
