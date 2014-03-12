package cn.hexing.meter.mock.spi;

import io.netty.channel.Channel;

public interface Client {

	public void run();
	
	public void onDisconnect(Channel channel);
	
	public void onConnect(Channel channel);
	
	public long getLastSendTime();
	
	public long getLastRecvTime();
	
}
