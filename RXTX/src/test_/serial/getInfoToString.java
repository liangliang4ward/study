package test_.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class getInfoToString {
	static CommPortIdentifier myPort;

	static SerialPort serialPort;

	OutputStream outputStream;

	InputStream inputStream;
	
	byte[] t;

	/*
	 *  static byte[] lightGreen = new byte[]{0x02,0x00,0x00,0x04,0x6A,0x10,0x02,0x70,0x03};//开绿灯
	 public static void main(String args[]){
	 new WriteBean(lightGreen);

	 }*/
	public static void main(String args[]){
		new WriteBean(new byte[]{0x02,0x00,0x00,0x04,0x6A,0x10,0x02,0x70,0x03});
	}

	public byte[] WriteBean(byte[] messageString,String Selectedport) {
		try {
			myPort = CommPortIdentifier.getPortIdentifier(Selectedport);

		} catch (NoSuchPortException e) {
			// e.printStackTrace();
			System.out.println("无此端口");
		}

		try {
			serialPort = (SerialPort) myPort.open("w", 2000);
		} catch (PortInUseException e) {
			e.printStackTrace();
		}
		try {
			outputStream = serialPort.getOutputStream();
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
		}
		try {
			outputStream.write(messageString);

			try {
				ReturnEqual ee = new ReturnEqual();
				ArrayList x=new ArrayList();
				
				int count = 0;
				while (true) {
					int d = inputStream.read();
					if (d != -1) {
						x.add(new Integer(d));
						
						count++;
						
					} 
					
					else {
						System.out.println("zzzz" + count);
						break;
					}
				}
				
				int[] b=new int[count];
				byte[]r=new byte[count];
				///////////////////////////////////////////////////////
				Iterator   it   =   x.iterator();
				//////////////////////////////////////////////////////
				for(int y=0;y<count;y++){
					b[y]=((Integer) it.next()).intValue(); 
					r[y]=(byte)b[y];
					
				}
				t=r;
				ee.ReadEqual(r);
				
				} catch (IOException e) {
				e.printStackTrace();
			} 
			}catch (IOException e) {
			}
		
		
		serialPort.close();
		return t;
	}

}
