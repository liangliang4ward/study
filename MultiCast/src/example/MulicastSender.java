package example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulicastSender {
	public static void main(String[] args) throws IOException {
		MulticastSocket mc = new MulticastSocket();
		mc.setTimeToLive(4);
		mc.send(new DatagramPacket("abv".getBytes(), 3 ));
	}
}
