/**
 * 管理终端RTU与通信的IP、PORT地址。 ip:port port为Hex字符串.
 * RtuAddress仅仅用于记录上行、下行报文日志。
 */
package cn.hexing.fk.rtu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class RtuAddressManager {
	private static final Map<Integer,RtuAddress> map = Collections.synchronizedMap(new HashMap<Integer,RtuAddress>(5120*10+7));

	public static void put(int rtu,String peerAddr ){
		RtuAddress addr = map.get(rtu);
		if( null != addr )
			addr.setPeerAddr(peerAddr);
		else{
			addr = new RtuAddress(peerAddr);
			map.put(rtu, addr);
		}
	}

	public static RtuAddress get(int rtua){
		return map.get(rtua);
	}
	
}
