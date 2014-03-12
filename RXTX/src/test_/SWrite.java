package test_;
import java.io.*;
import java.util.*;
//import javax.comm.*;
import gnu.io.*;

public class SWrite {
	static CommPortIdentifier myPort;
	//static byte [] messageString = new byte[]{0x3A,0x48,0x7A,0x5F,0x00,(byte)0x9F,0x68,0x61,0x6E,0x64,(byte)0xF1,0x03};
	 static byte[] messageString   = new byte[]{0x3A,0x48,0x7A,0x5F,(byte)0xFF,(byte)0xFF ,0x73 ,0x68 ,0x75 ,0x74 ,0x77 ,0x03};  //关机                                                            //端口-127~128.溢出后减去256.
	 //static byte[] messageString =new byte[]{0x3A ,0x48 ,0x7A ,0x5F ,(byte)0xFF ,(byte)0xFF ,0x73 ,0x69 ,0x67 ,0x6E ,0x7E ,0x03};  //签到
	//static String messageString = "0x3A 0x48 0x7A 0x5F 0x00 0x9F 0x68 0x61 0x6E 0x64 0xF1 0x03";

	static SerialPort serialPort;

	static OutputStream outputStream;

	public static void main(String[] args) {
		try {
			myPort = CommPortIdentifier.getPortIdentifier("COM2");
			
		} catch (NoSuchPortException e) {
			//e.printStackTrace();
			System.out.println("无此端口");
		}

		try {
			serialPort = (SerialPort) myPort.open("w", 2000);
		} catch (PortInUseException e) {
			e.printStackTrace();
		}
		try {
			outputStream = serialPort.getOutputStream();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
		}
		try {
			outputStream.write(messageString);//.getBytes());
			System.out.println("写入" + myPort.getName() + "信息成功");
			serialPort.close();
			//outputStream.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
