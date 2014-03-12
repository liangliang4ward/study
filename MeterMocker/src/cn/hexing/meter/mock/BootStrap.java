package cn.hexing.meter.mock;

import cn.hexing.meter.mock.utils.PropertiesLoader;


/**
 * 
 * @author gaoll
 * @time  2013年10月17日14:58:29
 * 启动
 */
public class BootStrap {

	public static void main(String[] args) throws InterruptedException {
		PropertiesLoader.loadPropertieFile2SystemProperties();
		
		String ip = System.getProperty("gate.ip");
		int port = Integer.parseInt(System.getProperty("gate.port"));
		int meterCount = Integer.parseInt(System.getProperty("meter.count"));
		int meterStartNo = Integer.parseInt(System.getProperty("meter.address.no"));
		
		for(int i=0;i<meterCount;i++){
			MockerClient mc = new MockerClient(ip, port);
			mc.setMeterID("10"+"0000000000".substring((""+(i+meterStartNo)).length())+(i+meterStartNo));
			mc.run();
		}
	}
	
	
	
}
