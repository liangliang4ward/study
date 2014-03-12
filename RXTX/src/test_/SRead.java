package test_;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class SRead implements Runnable, SerialPortEventListener {
	static CommPortIdentifier portId;

	InputStream inputStream;
	
	BufferedReader in;

	OutputStream outputStream;

	SerialPort serialPort;

	Thread readThread;

	public static void main(String[] args) {
		try {
			portId = CommPortIdentifier.getPortIdentifier("COM2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//new WriteBean();
		SRead reader = new SRead();
		System.out.println("读到的信息:");
		int i;
		try {
			while ((i = reader.inputStream.read()) != -1) {
				System.out.println((char) i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public SRead() {
		try {
			serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
		} catch (PortInUseException e) {
			e.printStackTrace();
		}
		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
		}
		serialPort.notifyOnDataAvailable(true);
		try {
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			// serialPort.close();
		} catch (UnsupportedCommOperationException e) {
		}
		readThread = new Thread(this);
		readThread.start();
	}

	public void run() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
		}
	}

	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		/*case SerialPortEvent.BI:
		 case SerialPortEvent.OE:
		 case SerialPortEvent.FE:
		 case SerialPortEvent.PE:
		 case SerialPortEvent.CD:
		 case SerialPortEvent.CTS:
		 case SerialPortEvent.DSR:
		 case SerialPortEvent.RI:
		 case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
		 break;*/
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[100] ;

			try {
				while (inputStream.available() > 0) {
					//int numBytes = inputStream.read(readBuffer);
					inputStream.read(readBuffer);
					System.out.print(readBuffer);
				}
				System.out.print(new String(readBuffer));
			} catch (IOException e) {
			}
			break;
		}
	}
}
