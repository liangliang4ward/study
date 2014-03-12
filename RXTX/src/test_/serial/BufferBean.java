package test_.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.InputStream;
import java.io.OutputStream;

public class BufferBean implements Runnable, SerialPortEventListener {
	static CommPortIdentifier portId;

	InputStream inputStream;

	OutputStream outputStream;

	SerialPort serialPort;

	Thread readThread;

	public static void main(String[] args) {
		try {
			portId = CommPortIdentifier.getPortIdentifier("COM2");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
//	public void serialEvent(SerialPortEvent evt)
//	{
//	　　 switch (evt.getEventType())
//	　　 {
//	　　 case SerialPortEvent.CTS :
//		　　 System.out.println("CTS event occured.");
//		　　 break;
//	　　 case SerialPortEvent.CD :
//		　　 System.out.println("CD event occured.");
//		　　 break;
//	　　 case SerialPortEvent.BI :
//		　　 System.out.println("BI event occured.");
//		　　 break;
//	　　 case SerialPortEvent.DSR :
//	　　 System.out.println("DSR event occured.");
//	　　 break;
//	　　 case SerialPortEvent.FE :
//	　　 System.out.println("FE event occured.");
//	　　 break;
//	　　 case SerialPortEvent.OE :
//	　　 System.out.println("OE event occured.");
//	　　 break;
//	　　 case SerialPortEvent.PE :
//	　　 System.out.println("PE event occured.");
//	　　 break;
//	　　 case SerialPortEvent.RI :
//	　　 System.out.println("RI event occured.");
//	　　 break;
//	　　 case SerialPortEvent.OUTPUT_BUFFER_EMPTY :
//	　　 System.out.println("OUTPUT_BUFFER_EMPTY event occured.");
//	　　 break;
//	　　 case SerialPortEvent.DATA_AVAILABLE :
//	　　 System.out.println("DATA_AVAILABLE event occured.");
//	　　 int ch;
//	　　 StringBuffer buf = new StringBuffer();
//	　　 InputStream input = serialPort.getInputStream
//	　　 try {
//	　　 while ( (ch=input.read()) > 0) {
//	　　 buf.append((char)ch); 
//	　　 }
//	　　 System.out.print(buf);
//	　　 } catch (IOException e) {}
//	　　 break;
//	　　 }
//	　　}

	public void run() {
		// TODO Auto-generated method stub	
	}

	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
