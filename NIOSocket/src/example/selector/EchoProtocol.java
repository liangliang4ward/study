package example.selector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class EchoProtocol implements TcpProtocol{

	private int bufSize;
	
	public EchoProtocol(int bufSize){
		this.bufSize = bufSize;
	}
	
	@Override
	public void handleAccept(SelectionKey key) throws IOException {
		SocketChannel scoketChannel = ((ServerSocketChannel)key.channel()).accept();
		scoketChannel.configureBlocking(false);
		scoketChannel.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocate(bufSize));
	}

	@Override
	public void handleWrite(SelectionKey key) throws IOException {
		ByteBuffer buffer=(ByteBuffer) key.attachment();
		buffer.flip();
		SocketChannel channel=(SocketChannel) key.channel();
		channel.write(buffer);
		if(!buffer.hasRemaining()){
			key.interestOps(SelectionKey.OP_READ);
		}
		buffer.compact();
	}

	@Override
	public void handleRead(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer buffer=(ByteBuffer) key.attachment();
		long bufRead=socketChannel.read(buffer);
		if(bufRead==-1){
			socketChannel.close();
		}else if ( bufRead >0 ){
			key.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
		}
	}

}
