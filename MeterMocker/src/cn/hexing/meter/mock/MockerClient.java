package cn.hexing.meter.mock;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

import cn.hexing.meter.mock.coder.ByteMessageDecoder;
import cn.hexing.meter.mock.coder.ByteMessageEncoder;
import cn.hexing.meter.mock.factory.DlmsMessageCreator;
import cn.hexing.meter.mock.factory.MessageCreator;
import cn.hexing.meter.mock.listener.FutureDisConnectListener;
import cn.hexing.meter.mock.listener.FutureMsgSendListener;
import cn.hexing.meter.mock.spi.Attribute;
import cn.hexing.meter.mock.spi.AttributeOption;
import cn.hexing.meter.mock.spi.Client;

public class MockerClient implements Client{

    private int RECONNECT_DELAY = 5;

    private final String host;
    private final int port;

    private final MockerMessageHandler handler = new DlmsMockMessageHandler(this);
    
    private MessageCreator msgCreator = new DlmsMessageCreator();
    
    private String meterID;
    
    private int heartInterval=2;

	public MockerClient(String str, int i) {
		this.host = str;
		this.port= i;
		heartInterval = Integer.parseInt(System.getProperty("meter.heart.interval"));
		
	}

	public void run() {
       final ChannelFuture c = configureBootstrap(new Bootstrap()).connect();
       c.addListener(new FutureDisConnectListener(MockerClient.this));
    }
	
	public  Bootstrap configureBootstrap(Bootstrap b) {
        return configureBootstrap(b, new NioEventLoopGroup());
    }

	public Bootstrap configureBootstrap(Bootstrap b, EventLoopGroup g) {
		b.group(g).channel(NioSocketChannel.class).remoteAddress(host, port)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("decoder",new ByteMessageDecoder());
						ch.pipeline().addLast("encoder",new ByteMessageEncoder());
						ch.pipeline().addLast(handler);
					}
				});
		return b;
	}

	public void onDisconnect(Channel channel) {
		//处理连接断开
		final EventLoop loop = channel.eventLoop();
		loop.schedule(new Runnable() {
			@Override
			public void run() {
				MockerClient.this.run();
			}
		},RECONNECT_DELAY,TimeUnit.SECONDS);
	}

	public void onConnect(final Channel channel) {
		final EventLoop loop = channel.eventLoop();
		loop.schedule(new Runnable() {
			@Override
			public void run() {
				loop.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
							Attribute attr = new Attribute();
							attr.set(AttributeOption.METER_ID,meterID);
							byte[] msg=msgCreator.createHeartBeat(attr);
							channel.writeAndFlush(msg).addListener(new FutureMsgSendListener(MockerClient.this));
					}
				}, 0, heartInterval, TimeUnit.MINUTES);
			}
		}, 10, TimeUnit.SECONDS);
	}
	
	
    private long lastSendTime;
    private long lastRecvTime;
	@Override
	public long getLastSendTime() {
		return lastSendTime;
	}

	@Override
	public long getLastRecvTime() {
		return lastRecvTime;
	}

	public void onSend(long time) {
		this.lastSendTime = time;
	}

	public void setMeterID(String meterID) {
		this.meterID = meterID;
	}
}
