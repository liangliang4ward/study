package rxtx.test;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class CommUtil implements SerialPortEventListener{
	SerialPort port = null;
	InputStream input;
	OutputStream output;
	CommPortIdentifier cpi;
	
	public CommUtil(Enumeration portList, String name){

		try {
			cpi=CommPortIdentifier.getPortIdentifier(name.toUpperCase());
			port=(SerialPort) cpi.open("My"+name, 2000);
		} catch (PortInUseException e) {
			System.out.println(e);
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		}
		
		try {
			input=port.getInputStream();
			output=port.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			port.addEventListener(this);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		port.notifyOnDataAvailable(true);
		try {
			port.setSerialPortParams(2400, SerialPort.DATABITS_8, // 设置串口读写参数
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void serialEvent(SerialPortEvent event) {
		switch(event.getEventType()){
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			System.out.println("神马都是浮云");
		break;
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[20];
			try {
				while (input.available() > 0) {
					System.out.println(input.available());
					int numBytes = input.read(readBuffer);
					System.out.println(numBytes);
				}
				send("receive");
				System.out.println(new String(readBuffer).trim());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	public void send(String content){
	
		try {
			output.write(content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(OutputStream os,String content){
		
		try {
			os.write(content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void closePort() {
		if (port != null) {
			port.close();
		}
	}
	
	public static void main(String[] args) {
		Enumeration en = CommPortIdentifier.getPortIdentifiers(); 
		CommUtil comm = new CommUtil(en, "com3");
		comm.send("hello");
	}


}