/**
 * Create DLMS message.
 */
package com.hx.dlms.message;

import cn.hexing.fk.message.IMessage;
import cn.hexing.fk.message.IMessageCreator;

/**
 * @author BaoHongwei Email: hbao2k@gmail.com
 *
 */
public class DlmsMessageCreator implements IMessageCreator {

	/* (non-Javadoc)
	 * @see com.hzjbbis.fk.message.IMessageCreator#create()
	 */
	public IMessage create() {
		return new DlmsMessage();
	}

	/* (non-Javadoc)
	 * @see com.hzjbbis.fk.message.IMessageCreator#createHeartBeat(int)
	 */
	public IMessage createHeartBeat(int reqNum) {
		return null;
	}

}
