package cn.hexing.meter.mock.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import cn.hexing.meter.mock.MockerClient;

public class FutureMsgSendListener implements ChannelFutureListener{
	MockerClient client;
	public FutureMsgSendListener(MockerClient client){
		this.client = client;
	}
	
	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if(future.isSuccess()){
			client.onSend(System.currentTimeMillis());
		}
	}

}
