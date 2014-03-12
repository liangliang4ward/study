package cn.hexing.fk.common.spi;

import cn.hexing.fk.message.IMessage;

public interface IClientModule extends IModule {
	boolean sendMessage(IMessage msg);
}
