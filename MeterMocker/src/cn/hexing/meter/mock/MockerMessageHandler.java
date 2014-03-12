package cn.hexing.meter.mock;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public abstract class MockerMessageHandler extends SimpleChannelInboundHandler<Object>{

	protected MockerClient client;
	
	public MockerMessageHandler(MockerClient dlmsMockClient) {
		this.client = dlmsMockClient;
	}

	@Override
	protected abstract void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception;
	
	@Override
	public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		client.onDisconnect(ctx.channel());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		client.onConnect(ctx.channel());
	}

}
