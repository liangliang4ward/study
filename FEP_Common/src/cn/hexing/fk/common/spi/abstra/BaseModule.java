/**
 * 模块对象的抽象基类
 */
package cn.hexing.fk.common.spi.abstra;

import cn.hexing.fk.common.spi.IModule;

/**
 *
 */
public abstract class BaseModule implements IModule {

	public String getName() {
		return "undefine";
	}

	public String getTxfs() {
		return "??";
	}

	public boolean isActive() {
		return true;
	}

	public abstract boolean start();

	public abstract void stop();

	public long getLastReceiveTime() {
		return 0;
	}

	public long getLastSendTime() {
		return 0;
	}

	public int getMsgRecvPerMinute() {
		return 0;
	}

	public int getMsgSendPerMinute() {
		return 0;
	}

	public long getTotalRecvMessages() {
		return 0;
	}

	public long getTotalSendMessages() {
		return 0;
	}

	public String profile() {
		return "<profile>empty</profile>";
	}

}
