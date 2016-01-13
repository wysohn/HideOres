package com.hideores.main;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.plugin.Plugin;

import com.hideores.cache.ChunkCoord;
import com.hideores.cache.ChunkMapCache;

public class CacheManager implements Runnable{
	private HideOres plugin;
	private static final long TTL = 100;
	
	private static Map<ChunkCoord, ChunkMapCache> caches = new ConcurrentHashMap<ChunkCoord, ChunkMapCache>();
	@Override
	public void run(){
		if(caches.isEmpty()) return;
		
		for(Map.Entry<ChunkCoord, ChunkMapCache> entry : caches.entrySet()){
			ChunkCoord key = entry.getKey();
			ChunkMapCache value = entry.getValue();
			if(value.getLastModified() + TTL < new Date().getTime()){
				caches.remove(key);
			}
		}
	}
	
	public CacheManager(Plugin plugin){
		this.plugin = (HideOres) plugin;
		
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 1L, 10L);
	}
	
	public void putCache(ChunkCoord coord, byte[] data){
		caches.put(coord, new ChunkMapCache(data));
	}
	
	public byte[] getCache(ChunkCoord coord){
		if(!caches.containsKey(coord)) return null;
		
		return caches.get(coord).getChunkData();
	}
}
