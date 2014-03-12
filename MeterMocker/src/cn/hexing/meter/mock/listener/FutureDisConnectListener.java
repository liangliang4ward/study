package cn.hexing.meter.mock.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import cn.hexing.meter.mock.MockerClient;

public class FutureDisConnectListener implements ChannelFutureListener{

	MockerClient client;
	public FutureDisConnectListener(MockerClient client){
		this.client = client;
	}
	
	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if(!future.isSuccess()){
			System.err.println("unconnect,schedule");
			client.onDisconnect(future.channel());
		}else{
			System.err.println("connect success");
		}
	}
	
}
