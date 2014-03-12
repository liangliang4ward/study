package com.hx.dlms.message;

import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.IMessageCreator;

/**
 * 
 * @author gaoll
 *
 * @time 2013-3-30 ÉÏÎç10:17:03
 *
 * @info DlmsHdlcMessageCreator
 */
public class DlmsHdlcMessageCreator implements IMessageCreator {

	@Override
	public IMessage create() {
		// TODO Auto-generated method stub
		return new DlmsHDLCMessage();
	}

	@Override
	public IMessage createHeartBeat(int reqNum) {
		// TODO Auto-generated method stub
		return null;
	}

}
