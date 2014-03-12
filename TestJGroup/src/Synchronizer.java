import java.io.InputStream;
import java.io.OutputStream;

import org.jgroups.Channel;
import org.jgroups.ChannelClosedException;
import org.jgroups.ChannelException;
import org.jgroups.ChannelNotConnectedException;
import org.jgroups.ExtendedReceiverAdapter;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.View;


public class Synchronizer extends ExtendedReceiverAdapter{
	private Channel channel = null;
	
	public Synchronizer(){
		try {
			channel = new JChannel("tcp2.xml");
			channel.setReceiver(this);
			channel.connect("Synchronzier");
			channel.getState(null, 20*1000);
		} catch (ChannelException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		Synchronizer s = new Synchronizer();
	}

	@Override
	public void receive(Message msg) {
		System.out.println(msg);
	}

	@Override
	public void viewAccepted(View new_view) {
		System.out.println(new_view);
		try {
			if(new_view.getCreator()!=channel.getAddress())
				channel.send(new_view.getCreator(), channel.getAddress(), "hello msg");
		} catch (ChannelNotConnectedException e) {
			e.printStackTrace();
		} catch (ChannelClosedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getState(OutputStream ostream) {
		super.getState(ostream);
	}

	@Override
	public void setState(InputStream istream) {
		super.setState(istream);
	}

}
