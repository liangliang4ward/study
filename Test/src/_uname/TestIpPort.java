package _uname;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestIpPort {
	public static void main(String[] args) throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		String ip=addr.getHostAddress().toString();//获得本机IP
		String address=addr.getHostName().toString();//获得本机名称
		System.out.println(ip+"\\"+address);
	}
}
