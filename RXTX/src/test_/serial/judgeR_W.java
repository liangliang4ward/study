package test_.serial;
import   java.util.Iterator;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.*;
import java.util.ArrayList;

public class judgeR_W {
	static CommPortIdentifier myPort;

	static SerialPort serialPort;

	OutputStream outputStream;

	InputStream inputStream;

	/*
	 * static byte[] lightGreen = new
	 * byte[]{0x02,0x00,0x00,0x04,0x6A,0x10,0x02,0x70,0x03};//开绿灯 public static
	 * void main(String args[]){ new WriteBean(lightGreen);
	 *  }
	 */
	public static void main(String args[]){
		 byte[] searchAll = new byte[] { 0x02,0x00 ,0x00 ,0x04 ,0x46 ,0x52 ,
			(byte)0x9C ,0x03 };// 尋所有的卡
		// new judgeR_W(searchAll);
	}
	public   judgeR_W(byte[] messageString) {
		try {
			myPort = CommPortIdentifier.getPortIdentifier("COM3");

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
				
				ee.ReadEqual(r);
				
			
				
				
			} catch (IOException e) {
			}

		} catch (IOException e) {
		}
		serialPort.close();
	}

}
