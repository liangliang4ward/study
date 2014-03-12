/**
 * 当Socket读操作时，如果捕获到IOException，则表示对方关闭TCP连接，需要抛出SocketClientCloseException。
 * 
 */
package cn.hexing.fk.exception;

import java.io.IOException;

/**
 *
 */
public class SocketClientCloseException extends RuntimeException {
	private static final long serialVersionUID = 6187628305543356505L;
	
	public SocketClientCloseException(IOException e){
		super(e);
	}
	
	public SocketClientCloseException(String message){
		super(message);
	}
}
