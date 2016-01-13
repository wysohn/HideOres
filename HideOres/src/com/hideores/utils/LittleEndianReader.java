package com.hideores.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LittleEndianReader{
	private ByteBuffer bytebuffer;
	
	public LittleEndianReader(byte[] data){
		bytebuffer = ByteBuffer.wrap(data);
		bytebuffer.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	public byte readByte(){
		return bytebuffer.get();
	}
	public short readShort(){
		return bytebuffer.getShort();
	}
	
	public void putByte(byte b){
		bytebuffer.position(bytebuffer.position() - 1);
		bytebuffer.put(b);
	}
	public void putShort(short s){
		bytebuffer.position(bytebuffer.position() - 2);
		bytebuffer.putShort(s);
	}
	
	public void reset(){
		bytebuffer.rewind();
	}
	public int getRemaining(){
		return bytebuffer.remaining();
	}
	public byte[] toByteArray(){
		return bytebuffer.array();
	}
}
