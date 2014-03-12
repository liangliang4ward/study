package com.hitangjun.rxtx.demo;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This version of the TwoWaySerialComm example makes use of the
 * SerialPortEventListener to avoid polling.
 * 
 */
public class EventOnTwoWaySerialComm {
	public EventOnTwoWaySerialComm() {
		super();
	}

	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				(new Thread(new SerialWriter(out))).start();

				serialPort.addEventListener(new SerialReader(in));
				serialPort.notifyOnDataAvailable(true);

			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	/**
	 * Handles the input coming from the serial port. A new line character is
	 * treated as the end of a block in this example.
	 */
	public static class SerialReader implements SerialPortEventListener {
		private InputStream in;

		private byte[] buffer = new byte[1024];

		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void serialEvent(SerialPortEvent arg0) {
			int data;

			try {
				int len = 0;
				while ((data = in.read()) > -1) {
					if (data == '\n') {
						break;
					}
					buffer[len++] = (byte) data;
				}
				System.out.print(new String(buffer, 0, len));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

	}

	/** */
	public static class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		public void run() {
			try {
				int c = 0;
				while ((c = System.in.read()) > -1) {
					this.out.write(c);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public static void main(String[] args) {
		try {
			(new EventOnTwoWaySerialComm()).connect("COM2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
     * <p>在指定的时间内从串口读取指定长度的数据</p>
     * <pre>注意: 如果在指定的时间内未能读取指定长度的数据,
     * <p>      那么将返回实际读取的数据以及对应的长度</pre>
     * @param data 用于存放从串口读取的数据的缓冲区
     * @param rdLen 指定要读取的数据的长度
     * @param timeout 指定读取数据的时间长度
     * @return 实际读取的数据的长度
     * @throws  IOException 如果读串口时低层 API 抛出异常
     */
    /*public int read(byte[] data, int rdLen, int timeout)
        throws IOException {

        try {
            if (!sPort.isReceiveTimeoutEnabled()){
                sPort.enableReceiveTimeout(timeout);
            }
        } catch (UnsupportedCommOperationException e) {
            throw new IOException(e.toString());
        }

        int offset = 0;
        int curRdLen = rdLen;
        long start = System.currentTimeMillis();
        long end = start + timeout;

        while (offset < rdLen && System.currentTimeMillis() < end) {
        //while (offset < rdLen) {
            try {
                int len = is.read(data, offset, curRdLen);
                offset += len;
                curRdLen -= len;
            } catch (IOException e) {
                throw e;
            }
        }
        if (offset > 0)
        {
//            HexDumper.dumpLogger(logger,"<< ", data, 0, offset);
        }
        return offset;
    }*/

}
