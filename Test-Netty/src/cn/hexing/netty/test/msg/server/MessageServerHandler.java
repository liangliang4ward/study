package cn.hexing.netty.test.msg.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.WriteCompletionEvent;

import cn.hexing.netty.test.model.Person;

public class MessageServerHandler extends SimpleChannelUpstreamHandler{
    private static final Logger logger = Logger.getLogger(
            MessageServerHandler.class.getName());

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
    	if(e.getMessage() instanceof String ){
    		System.err.println("Receive:"+e.getMessage());
    	}
    	e.getChannel().write("\r\n send: ");
//        if (!(e.getMessage() instanceof Person)) {
//            return;//(1)
//        }
//        Person p=(Person) e.getMessage();
//        System.err.println("got msg:"+p);
//        e.getChannel().write(p);//(2)
    }

    
    @Override
	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
			throws Exception {
		super.writeComplete(ctx, e);
	}


	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);
		System.out.println("Channel Connected:"+e);
	}


	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelClosed(ctx, e);
		System.out.println("Channel Closed:"+e);
	}


	@Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }

}
