package com.hideores.cache;

import java.util.Date;

public class ChunkMapCache{
	private byte[] chunkData;
	private long lastModified = -1;
	
	public ChunkMapCache(byte[] chunkData){
		this.chunkData = chunkData;
		this.lastModified = new Date().getTime();
	}

	public byte[] getChunkData() {
		return chunkData;
	}

	public void setChunkData(byte[] chunkData) {
		this.chunkData = chunkData;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	
	
	
}
