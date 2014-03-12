package test_;
import java.io.*;
import java.util.*;
import gnu.io.*;
import java.awt.*;
import java.util.Properties;
import javax.swing.*;
import java.awt.event.*;




public class com extends JFrame implements ActionListener,Runnable,SerialPortEventListener
{
	static Enumeration portList;
	static CommPortIdentifier portId;
	static SerialPort serialPort;
	static OutputStream outputStream;
	static InputStream inputStream;
	static CommDriver driver=null;
	static Properties props = new Properties();
	static boolean use=false;
	Thread readThread;
	JScrollPane p1;
	JTextField Text=new JTextField(10);
	JButton ok=new JButton("确定");
	JTextArea area=new JTextArea(15,10);
	JLabel l1=new JLabel("Message:");
	com()
	{
		setTitle("Com");
		setVisible(true);
		setLayout(null);
		p1=new JScrollPane(area);
		add(l1);l1.setBounds(35,30,100,25);
		add(Text);Text.setBounds(120,30,150,25);
		add(ok);ok.setBounds(290,30,80,23);
		add(p1);p1.setBounds(35,70,340,250);
		ok.addActionListener(this);
		setSize(400,400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	    Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width-400)/2,(screen.height-400)/2);
		addWindowListener
		(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(1);
				}
			}
		);
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==ok)
		{
 		 	/*String key="Driver";
 		 	String driverName="com.sun.comm.Win32Driver";
 		 	try
 		 	{
    			driver =(CommDriver) Class.forName(driverName).newInstance();
   				driver.initialize();
  			}
        	catch(Exception a){JOptionPane.showMessageDialog(null,a);}*/
			String message1=Text.getText();
			byte[] message = message1.getBytes();
			boolean portFound=false;
			String defaultPort="COM2";
			portList=CommPortIdentifier.getPortIdentifiers();
			System.out.println(portList.hasMoreElements());
			while(portList.hasMoreElements())
			{
				portId=(CommPortIdentifier)portList.nextElement();
				System.out.println("portId:"+portId.getName());
				if(portId.getPortType()==CommPortIdentifier.PORT_SERIAL)
				{
					if(portId.getName().equals(defaultPort))
					{
						portFound=true;
						if(!use)
						{
							try
							{
								serialPort=(SerialPort)portId.open("com",2000);
							}
							catch(PortInUseException a){JOptionPane.showMessageDialog(null,"端口正在使用");continue;}
						}
						try
						{
							outputStream=serialPort.getOutputStream();
						}
						catch(IOException a){}
						try
						{
							serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
						}
						catch(UnsupportedCommOperationException a){}
						try
						{
							serialPort.notifyOnOutputEmpty(true);
						}
						catch(Exception a){}
						try
						{
							outputStream.write(message);//.getBytes());
							area.append("\nsend to port is sucess:  "+message+"***");
							use=true;
						}
						catch(IOException a){}
					}
				}
			}
			if(!portFound)
			{
				JOptionPane.showMessageDialog(null,"can't find the port");
			}
			try
			{
				inputStream=serialPort.getInputStream();
			}
			catch(IOException a){}
			try
			{
				serialPort.addEventListener(this);
			}
			catch(TooManyListenersException a){}
			serialPort.notifyOnDataAvailable(true);
			readThread=new Thread(this);
			readThread.start();
		}
	}
	public void run()
	{
		try
		{
			Thread.sleep(2000);
		}
		catch(InterruptedException e){}
	}
	public static void main(String[] args)
	{
		new com();
	}
	public void serialEvent(SerialPortEvent event)
	{
 		switch(event.getEventType())
 		{
			case SerialPortEvent.BI:
			case SerialPortEvent.OE:
 			case SerialPortEvent.FE:
 			case SerialPortEvent.PE:
 			case SerialPortEvent.CD:
 			case SerialPortEvent.CTS:
 			case SerialPortEvent.DSR:
 			case SerialPortEvent.RI:
 			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
 			break;
 			case SerialPortEvent.DATA_AVAILABLE:
 			byte[] readBuffer=new byte[1];
 			try
 			{
 				while(inputStream.available()>0)
 				{
      				int numBytes=inputStream.read(readBuffer);
      				area.setLineWrap(true);
      				area.append(new String(readBuffer));
 				} 
     		}
     		catch(IOException e){}
			break;
		}
    } 
}