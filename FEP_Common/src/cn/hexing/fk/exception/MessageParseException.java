/**
 * 消息解析异常
 */
package cn.hexing.fk.exception;

import java.nio.ByteBuffer;

import cn.hexing.fk.utils.HexDump;

/**
 *
 */
public class MessageParseException extends Exception {
	private static final long serialVersionUID = -5985134647725926736L;
	private ByteBuffer buffer;
	
    public MessageParseException(String message)
    {
        super(message);
    }
    
	public MessageParseException(String message, ByteBuffer buff) {
		super(message);
		if (null == buff)
			return;
		if (buff.position() > 0) //已经读取了一些数据
			buff.rewind();
		buffer = buff.slice();
	}
	
	public String getMessage() {
		String message = super.getMessage();

		if (message == null) {
			message = "";
		}

		if (buffer != null) {
			return message + ((message.length() > 0) ? " " : "") + "(Hexdump: "
					+ HexDump.hexDump(buffer) + ')';
		} else {
			return message;
		}
	}

    public String getLocalizedMessage()
    {
        return getMessage();
    }
}
