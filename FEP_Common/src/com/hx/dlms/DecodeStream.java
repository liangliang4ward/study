package com.hx.dlms;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import cn.hexing.fk.utils.HexDump;

public class DecodeStream extends InputStream {

	public static final DecodeStream wrap(ByteBuffer buf){
		return new DecodeStream(buf);
	}

	public static final DecodeStream wrap(byte[] buf){
		return new DecodeStream(buf);
	}
	
	/**
	 * @param decs: hex string to decode.
	 * @return
	 */
	public static final DecodeStream wrap(String decs){
		return new DecodeStream(HexDump.toByteBuffer(decs));
	}
	
	private ByteBuffer buffer = null;
	
	public DecodeStream(ByteBuffer buf){
		buffer = buf;
	}
	
	public DecodeStream(byte[] buf, int offset,int length ){
		buffer = ByteBuffer.wrap(buf, offset, length);
	}
	
	public DecodeStream(byte[] buf){
		buffer = ByteBuffer.wrap(buf);
	}
	
	@Override
	public int read() throws IOException {
		return buffer.get() & 0xFF;
	}
	
	//01 00 00 00 06 7F 1F 04 00 00 BC 1F 04 B0
	
	/**
	 * Get first byte value, position will not change.
	 * @return
	 * @throws IOException
	 */
	public int peek(){
		return buffer.get(buffer.position());
	}

	/**
	 * Returns: the total number of bytes read into the buffer, 
	 *     or -1 is there is no more data because the end of the stream has been reached.
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return read(b,0,b.length);
	}

	@Override
	public int read(byte[] b, int offset, int len) throws IOException {
		int bytesToRead = Math.min(len, buffer.remaining());
		buffer.get(b,offset,bytesToRead);
		return bytesToRead;
	}

	@Override
	public long skip(long n) throws IOException {
		int actualSkip = Math.min((int)n, buffer.remaining());
		buffer.position(buffer.position()+actualSkip);
		return actualSkip;
	}

	@Override
	public int available() throws IOException {
		return buffer.remaining();
	}

	@Override
	public void close() throws IOException {
		buffer = null;
	}

	public int position(){
		return buffer.position();
	}
	
	public void position(int pos){
		buffer.position(pos);
	}
	
	/**
	 * Parameter no use.
	 */
	@Override
	public void mark(int readlimit) {
		buffer.mark();
	}
	
	public void mark() {
		buffer.mark();
	}

	@Override
	public void reset() throws IOException {
		buffer.reset();
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public final String toString(){
		return HexDump.hexDumpCompact(buffer);
	}
}
