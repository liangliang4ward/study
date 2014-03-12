package example;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class Test {
	public static void main(String[] args) throws SocketException {
		Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
		while(en.hasMoreElements()){
			
			NetworkInterface nif = en.nextElement();
			System.out.println("dispName:"+nif.getDisplayName());
			Enumeration<InetAddress> eni = nif.getInetAddresses();
			while(eni.hasMoreElements()){
				InetAddress inet = eni.nextElement();
				System.out.println("ia:"+inet);
			}
			
			List<InterfaceAddress> ifas = nif.getInterfaceAddresses();
			for(InterfaceAddress ifs:ifas){
				System.out.println(ifs.toString());
			}
			
			
		}
		
	}
}
