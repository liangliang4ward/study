package test_.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.*;
import java.util.ArrayList;

public class WriteBean {
	static CommPortIdentifier myPort;

	static SerialPort serialPort;

	OutputStream outputStream;

	InputStream inputStream;

	/*
	 *  static byte[] lightGreen = new byte[]{0x02,0x00,0x00,0x04,0x6A,0x10,0x02,0x70,0x03};//开绿灯
	 public static void main(String args[]){
	 new WriteBean(lightGreen);

	 }*/

	public WriteBean(byte[] messageString) {
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
			outputStream.write(messageString);// .getBytes());
			System.out.println("写入" + myPort.getName() + "信息成功");
			System.out.print("写入的信息是：");

			toHex strHex = new toHex();
			
			ReturnEqual ee = new ReturnEqual();

			strHex.WriteToHex(messageString);
			
			System.out.print("\n读到的信息是：");
			try {
				int timeOut = 2;
				int count=0;
				//int r = inputStream.read();
				while (true) {
					 int r = inputStream.read();
								
					if (r != -1) {
						count++;
						strHex.ReadToHex(r);

					} else if (timeOut <= 0) {
						System.out.println("\nTimeout to Get a Reply!");
						break;
					} else {
						Thread.sleep(1000);
						System.out.print("\nWaiting for Get a Reply!");
						timeOut--;
					}
				}
				System.out.println(count);
												
				} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
		}
		
		serialPort.close();
	}
}
