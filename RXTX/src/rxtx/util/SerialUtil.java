package rxtx.util;

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
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

public  class SerialUtil implements SerialPortEventListener,Runnable{

	protected InputStream input = null;
	protected OutputStream output = null;
	protected SerialPort port = null;
	protected String portName;
	CommPortIdentifier cpi = null;
	public enum Setting {PARAMS_TIMEOUT,PARAMS_PORT,PARAMS_DATABITS,PARAMS_STOPBITS,PARAMS_PARITY,PARAMS_RATE};
	
	Map<Setting,String> params;

	@Override
	public void serialEvent(SerialPortEvent event) {
		onReceive(event);
	}
	
	public SerialUtil(Map<Setting,String> params){
		this.params = params;
	}
	
	private void init() {
		int timeOut = Integer.parseInt(params.get(Setting.PARAMS_TIMEOUT));
		String portName=params.get(Setting.PARAMS_PORT).toUpperCase();
		int dataBit = Integer.parseInt(params.get(Setting.PARAMS_DATABITS));
		int stopBit = Integer.parseInt(params.get(Setting.PARAMS_STOPBITS));
		int parity = Integer.parseInt(params.get(Setting.PARAMS_PARITY));
		int rate=Integer.parseInt(params.get(Setting.PARAMS_RATE));
		
		try {
			cpi = CommPortIdentifier.getPortIdentifier(portName);
			port = (SerialPort) cpi.open(this.toString(), timeOut);
			input = port.getInputStream();
			output = port.getOutputStream();
			port.addEventListener(this);
			port.notifyOnDataAvailable(true);
			port.setSerialPortParams(rate, dataBit, stopBit, parity);
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		}
		
		
	}

	public boolean connect(){
		init();
		return true;
	}
	
	@Override
	public void run() {
		connect();
	}
	
	public boolean sendMessage(String content){
		try {
			output.write(content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void onReceive(SerialPortEvent event){
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
		break;
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[20];
			StringBuilder sb = new StringBuilder();
			try {
				while (input.available() > 0){
					int num=input.read(readBuffer);
					byte[] temp = new byte[num];
					System.arraycopy(readBuffer, 0, temp, 0, num);
					sb.append(new String(temp).trim()+" ");
				}
				System.out.println(sb);
				sendMessage("receive\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	

	public static void main(String[] args) {
		HashMap<Setting,String> params = new HashMap<Setting, String>();
		params.put(Setting.PARAMS_TIMEOUT, "1000");
		params.put(Setting.PARAMS_RATE, "2400");
		params.put(Setting.PARAMS_DATABITS, "8");
		params.put(Setting.PARAMS_STOPBITS, "1");
		params.put(Setting.PARAMS_PARITY, "1");
		params.put(Setting.PARAMS_PORT, "COM5");
		SerialUtil su = new SerialUtil(params);
		new Thread(su).start();
	}
}
