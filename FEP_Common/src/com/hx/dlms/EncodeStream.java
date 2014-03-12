package com.hx.dlms;

import java.io.ByteArrayOutputStream;

public class EncodeStream extends ByteArrayOutputStream {
	
	public EncodeStream(){
		super(512);
	}
	
	/**
	 * Writes the specified byte to this output stream
	 */
	@Override
	public void write(int b){
		super.write(b);
	}

	public byte[] dump(){
		byte[] result = toByteArray();
		super.reset();
		return result;
	}
}
