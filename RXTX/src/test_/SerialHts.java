package test_;


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
    //static byte[] c = new byte[]{0x02,0x00,0x00,0x04,0x6A,0x10,0x02,0x70,0x03};//¿ªÂÌµÆ
    static byte[] c = new byte[]{0x02,0x00,0x00 ,0x04 ,0x6A ,0x01 ,0x6F ,0x03};//¹ØÂÌµÆ
   // static byte[] c = new byte[]{0x02 ,0x00 ,0x00 ,0x04 ,0x46 ,0x52 ,(byte)0x9C ,0x03};//Ñ°¿¨
  // static byte[] c= new byte[]{0x02,0x00 ,0x00 ,0x04 ,0x47 ,0x04 ,0x4F ,0x03};//·À³åÍ»
    static {
		System.loadLibrary("MasterRD");
		System.loadLibrary("MasterCom");
	}
    public static void main(String[] args) {
		SerialHts SH = new SerialHts();
		
		SH.PortName = new String("COM3");
		if(!SH.Initialize())
		{
			System.out.println("init failed!");
		}
		
		for(int i=0;i<6;i++){
			//send 
			try {
				 //byte[] c=new byte[]{0x02,0x00,0x00,0x04,0x6A,0x10,0x02,0x70,0x03};//¿ªÂÌµÆ
				out.write(c);
				
				System.out.print("Send:" +  "	");
				//ÒÔ16½øÖÆÊä³ö
				for(int j = 0; j < c.length; j++)
				   {
				    
				    String strHex = new String();
				    strHex = Integer.toHexString(c[j]).toUpperCase();
				    if(strHex.length() > 3)
				     System.out.print(strHex.substring(6));
				    else
				     if(strHex.length() < 2)
				      System.out.print("0" + strHex);
				     else
				      System.out.print(strHex);
				    
				    System.out.print(" ");
				   }

				
				System.out.println("");
				
				
				//System.out.print("Send:" + c + "	");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//receive
			int r;
			try {
				int timeOut = 5;
				while(true){
					r = in.read();
					byte[] q= new byte[50];
					/*int a=in.read(q);
					System.out.println("qqqq"+a);*/
					//System.out.print("Reply:" +  "	");
					if(r!=-1){
					//byte[] c=new byte[]{0x02,0x00,0x00,0x04,0x6A,0x10,0x02,0x70,0x03};
						
						for(int j = 0; j < r; j++){
						
						String rHex = new String();
					    rHex = Integer.toHexString(r).toUpperCase();
					    
					    
					    
					    if(rHex.length() > 3)
						     System.out.print(rHex.substring(6));
						    else
						     if(rHex.length() < 2)
						      System.out.print("0" + rHex);
						     else
						      System.out.print(rHex);
					          System.out.print(" ");
					    
						
						break;}
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
