package test_.serial;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class SerialHts {
    
	String PortName;
    CommPortIdentifier portId;
    SerialPort serialPort;
    static OutputStream out;
    static InputStream  in;
    static byte[] c=new byte[]{0x02,0x00,0x00,0x04,0x6A,0x10,0x02,0x70,0x03};//¿ªÂÌµÆ

	public static void main(String[] args) {
		SerialHts SH = new SerialHts();
		
		SH.PortName = new String("COM3");
		if(SH.Initialize()==false)
		{
			System.out.println("init failed!");
		}
		
		for(int j=0;j<2;j++){
			//send 
			try {
				
				out.write(c);
				System.out.print("Send:" + c + "	");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//receive
			int r;
			try {
				int timeOut = 3;
				while(true){
					r = in.read();
					if(r!=-1){
						System.out.println("Reply:" + (char)r);
						break;
					}else if(timeOut<=0){
						System.out.println("\nTimeout to Get a Reply!");
						break;
					}else{
						Thread.sleep(1000);
						System.out.print("\nWaiting for Get a Reply!");
						timeOut--;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} //end for
		
		SH.serialPort.close();
	}

    public boolean Initialize()
    {
        boolean InitSuccess = true;
	    try
	    {
	        portId = CommPortIdentifier.getPortIdentifier(PortName);
	        try
	        {
	            serialPort = (SerialPort) portId.open("Serial_Communication", 2000);
	        } 
	        catch (PortInUseException e)
	        {
	            return !InitSuccess;
	        }
	        
	        //Use InputStream in to read from the serial port, and OutputStream
	        //out to write to the serial port.
	        try
	        {
	            in  = serialPort.getInputStream();
	            out = serialPort.getOutputStream();
	        }
	        catch (IOException e)
	        {
	            return !InitSuccess;
	        }
	        
	        //Initialize the communication parameters to 9600, 8, 1, none.
	        try
	        {
	             serialPort.setSerialPortParams(19200,
	                        SerialPort.DATABITS_8,
	                        SerialPort.STOPBITS_1,
	                        SerialPort.PARITY_NONE);
	        } 
	        catch (UnsupportedCommOperationException e)
	        {
	            return !InitSuccess;
	        }
	    } 
	    catch (NoSuchPortException e)
	    {
	        return !InitSuccess;
	    }
	    
        // return success information
	    return InitSuccess;
    }
}
