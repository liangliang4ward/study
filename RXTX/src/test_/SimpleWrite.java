package test_;
import java.io.*;
import java.util.*;
//import javax.comm.*;
import gnu.io.*;

public class SimpleWrite {
	static Enumeration portList;

	static CommPortIdentifier portId;

	static String messageString = "3A 48 7A 5F 00 9F 68 61 6E 64 F1 03";

	static SerialPort serialPort;

	static OutputStream outputStream;

	public static void main(String[] args) {

		portList = CommPortIdentifier.getPortIdentifiers();
		//System.out.println("没有串口");
		
		while(portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			// System.out.println(portId.getName());
	
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals("COM2")) {
					System.out.println("11");
				}else{
					continue;
				}
	
				// if (portId.getName().equals("/dev/term/a")) { 
				try {
					serialPort = (SerialPort) portId.open("SimpleWriteApp", 2000);
				} catch (PortInUseException e) {
					System.out.println("PortInUseException");
				}
	
				try {
					outputStream = serialPort.getOutputStream();
				} catch (IOException e) {
	
				}
	
				try {
					serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
							SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				} catch (UnsupportedCommOperationException e) {
	
				}
	
				try {
					outputStream.write(messageString.getBytes());
					System.out.println("写入" + portId.getName() + "信息成功");
					serialPort.close();
				} catch (IOException e) {
	
				}
			}
			
		}//end while
			
	}//end main
}
