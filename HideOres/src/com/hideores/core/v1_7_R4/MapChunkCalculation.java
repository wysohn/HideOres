package com.hideores.core.v1_7_R4;

import java.util.Map;
import java.util.zip.Deflater;

import org.bukkit.World;

import com.hideores.cache.ChunkCoord;
import com.hideores.cache.ChunkMapCache;
import com.hideores.core.Iinstance.IMapChunkCalculation;
import com.hideores.main.HideOres;
import com.hideores.utils.BlockHider;
import com.hideores.utils.ReflectionHelper;

import net.minecraft.server.v1_7_R4.PacketPlayOutMapChunk;
import net.minecraft.server.v1_7_R4.PacketPlayOutMapChunkBulk;
import net.minecraft.server.v1_8_R1.ChunkMap;

public class MapChunkCalculation implements IMapChunkCalculation{
	public static void calcAndChange(World world, PacketPlayOutMapChunk packet){
		int x = (int) ReflectionHelper.getPrivateField(packet, "a");
		int z = (int) ReflectionHelper.getPrivateField(packet, "b");
		
		//get uncompressedData from packet
		byte[] unCompressedData = (byte[]) ReflectionHelper.getPrivateField(packet, "f");
		
		//make a new data array
		byte[] newData;
		
		//calculate and change new chunk map
		ChunkCoord coord = new ChunkCoord(world.getName(),x,z);
		if((newData = HideOres.getCacheManager().getCache(coord)) == null){
			newData = new byte[unCompressedData.length];
			
			System.arraycopy(unCompressedData, 0, newData, 0, unCompressedData.length);
			BlockHider.checkAndCopy(newData, true);
			
			HideOres.getCacheManager().putCache(coord, newData.clone());
		}
		
		//compression
		Deflater deflater = new Deflater(-1);
		byte[] compressedData;
		int compressionSize;
		
        try {
            deflater.setInput(newData, 0, newData.length);
            deflater.finish();
            compressedData = new byte[newData.length];
            compressionSize = deflater.deflate(compressedData);
        } finally {
            deflater.end();
        }
		
		//put new data to packet
		ReflectionHelper.setPrivateField(packet, "e", compressedData);
		ReflectionHelper.setPrivateField(packet, "h", compressionSize);		
	}
	public static void calcAndChange(World world, PacketPlayOutMapChunkBulk packet){
		int[] xArray = (int[]) ReflectionHelper.getPrivateField(packet, "a");
		int[] zArray = (int[]) ReflectionHelper.getPrivateField(packet, "b");
		
		//get uncompressedDatas from packet
		byte[][] unCompressedDatas = (byte[][]) ReflectionHelper.getPrivateField(packet, "inflatedBuffers");
		
		//fill data into newData
		byte[][] newDatas = new byte[unCompressedDatas.length][];
		for(int i=0;i<unCompressedDatas.length;i++){
			newDatas[i] = new byte[unCompressedDatas[i].length];
			System.arraycopy(unCompressedDatas[i], 0, newDatas[i], 0, unCompressedDatas[i].length);
		}
		
		byte[] buildBuffer = new byte[0];
		
		int index = 0;
		int bufferSize = 0;
		for(byte[] newData : newDatas){
			int x = xArray[index];
			int z = zArray[index];
			
			//calculate and change new chunk map
			ChunkCoord coord = new ChunkCoord(world.getName(), x,z);
			if(HideOres.getCacheManager().getCache(coord) == null){
				BlockHider.checkAndCopy(newData, true);
				
				HideOres.getCacheManager().putCache(coord, newData.clone());
			}else{
				newData = HideOres.getCacheManager().getCache(coord);
			}
			
            if (buildBuffer.length < bufferSize + newData.length) {
                byte[] abyte = new byte[bufferSize + newData.length];

                System.arraycopy(buildBuffer, 0, abyte, 0, buildBuffer.length);
                buildBuffer = abyte;
            }
			
			System.arraycopy(newData, 0, buildBuffer, bufferSize, newData.length);
			bufferSize += newData.length;
			
			index++;
		}
		
		//compression
		Deflater deflater = new Deflater(-1);
		byte[] compressedData;
		int compressionSize;
		
        try {
            deflater.setInput(buildBuffer, 0, bufferSize);
            deflater.finish();
            compressedData = new byte[bufferSize];
            compressionSize = deflater.deflate(compressedData);
        } finally {
            deflater.end();
        }
		
		//put new chunk map array to packet
		ReflectionHelper.setPrivateField(packet, "buffer", compressedData);	
		ReflectionHelper.setPrivateField(packet, "size", compressionSize);	
	}
}
