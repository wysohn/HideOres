package com.hideores.core.v1_8_R2;

import java.util.Map;

import org.bukkit.World;

import com.hideores.cache.ChunkCoord;
import com.hideores.cache.ChunkMapCache;
import com.hideores.core.Iinstance.IMapChunkCalculation;
import com.hideores.main.HideOres;
import com.hideores.utils.BlockHider;
import com.hideores.utils.ReflectionHelper;

import net.minecraft.server.v1_8_R2.PacketPlayOutMapChunk;
import net.minecraft.server.v1_8_R2.PacketPlayOutMapChunk.ChunkMap;
import net.minecraft.server.v1_8_R2.PacketPlayOutMapChunkBulk;

public class MapChunkCalculation implements IMapChunkCalculation{	
	public static void calcAndChange(World world, PacketPlayOutMapChunk packet){
		int x = (int) ReflectionHelper.getPrivateField(packet, "a");
		int z = (int) ReflectionHelper.getPrivateField(packet, "b");
		//get original chunk map c from packet
		ChunkMap originalChunkMap = (ChunkMap) ReflectionHelper.getPrivateField(packet, "c");
		
		//make a copy of chunk map
		ChunkMap newChunkMap = new ChunkMap();
		newChunkMap.b = originalChunkMap.b;

		//calculate and change new chunk map
		ChunkCoord coord = new ChunkCoord(world.getName(),x,z);
		if((newChunkMap.a = HideOres.getCacheManager().getCache(coord)) == null){
			newChunkMap.a = originalChunkMap.a.clone();
			BlockHider.checkAndCopy(newChunkMap.a, true);
			
			HideOres.getCacheManager().putCache(coord, newChunkMap.a.clone());
		}
		
		//put new chunk map to packet
		ReflectionHelper.setPrivateField(packet, "c", newChunkMap);		
	}
	public static void calcAndChange(World world, PacketPlayOutMapChunkBulk packet){
		int[] xArray = (int[]) ReflectionHelper.getPrivateField(packet, "a");
		int[] zArray = (int[]) ReflectionHelper.getPrivateField(packet, "b");
		
		ChunkMap[] originalChunkMapArray = (ChunkMap[]) ReflectionHelper.getPrivateField(packet, "c");
		ChunkMap[] newChunkMapArray = new ChunkMap[originalChunkMapArray.length];
		
		int index = 0;
		for(ChunkMap originalChunkMap : originalChunkMapArray){
			int x = xArray[index];
			int z = zArray[index];
			
			//make a copy of chunk map
			ChunkMap newChunkMap = new ChunkMap();
			newChunkMap.b = originalChunkMap.b;

			//calculate and change new chunk map
			ChunkCoord coord = new ChunkCoord(world.getName(),x,z);
			if((newChunkMap.a = HideOres.getCacheManager().getCache(coord)) == null){
				newChunkMap.a = originalChunkMap.a.clone();
				BlockHider.checkAndCopy(newChunkMap.a, true);
				
				HideOres.getCacheManager().putCache(coord, newChunkMap.a.clone());
			}
			
			//put it into new array
			newChunkMapArray[index] = newChunkMap; index++;
		}
		
		//put new chunk map array to packet
		ReflectionHelper.setPrivateField(packet, "c", newChunkMapArray);	
		
	}
}
