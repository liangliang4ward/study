package com.platform.test;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
/**
 * 接收收据包
 *
 */
public class Receive2 extends ReceiverAdapter
{
	JChannel channel;
	String user_name = System.getProperty("user.name", "n/a");
	
	public static void main(String[] args) throws Exception
	{
		//接收收据端
		new Receive2().start();
	}
	
	private void start() throws Exception
	{
		//创建一个通道
		channel = new JChannel();
		//创建一个接收器
		channel.setReceiver(this);
		//加入一个群
		channel.connect("ChatCluster");
	}

	//覆盖父类的方法
	@Override
	public void receive(Message msg)
	{
		//具体参见msg的参数
		String receiveData=(String)msg.getObject();
		System.out.println("  发过来的数据是:  " +receiveData);
	}

	@Override
	public void viewAccepted(View new_view)
	{
		System.out.println("** view: " + new_view);
	}
}

