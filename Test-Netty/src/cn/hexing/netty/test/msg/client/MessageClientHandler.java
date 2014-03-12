package cn.hexing.netty.test.msg.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import cn.hexing.netty.test.model.Person;

public class MessageClientHandler extends SimpleChannelUpstreamHandler{
	private static final Logger logger = Logger.getLogger(
            MessageClientHandler.class.getName());


    @Override
    public void channelConnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) {
    	Person message = new Person();
    	message.setId(12);
    	message.setName("GaoLiang");
        e.getChannel().write(message);
    }

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
        // Send back the received message to the remote peer.
//        System.err.println("messageReceived send message "+e.getMessage());
//        try {
//            Thread.sleep(1000*3);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        e.getChannel().write(e.getMessage());
    	System.out.println(e.getMessage());
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }
}
