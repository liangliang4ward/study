package cn.hexing.meter.mock.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteMessageDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		int readableBytes = in.readableBytes();
		if(readableBytes<=0) return;
		byte[] b = new byte[readableBytes];
		in.readBytes(b);
		out.add(b);
		
	}



}
