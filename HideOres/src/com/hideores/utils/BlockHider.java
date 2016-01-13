package com.hideores.utils;

import com.hideores.main.HideOres;
import com.hideores.main.HideOresConfigs;

public class BlockHider {
	public static HideOresConfigs config = HideOres.config;
	
	public static void checkAndCopy(byte[] original, boolean newDataSystem) {
		//LER init
		LittleEndianReader LER = new LittleEndianReader(original);
		
		if(newDataSystem){
			//while has bytes to read
			while(LER.getRemaining() > 0){
				//a single data
				short data = LER.readShort();
				
				//extract type and meta from data
				int type = (data >> 4);
				//int meta = (data & 0x000F);
				
				//if ore
				if (config.getOres().contains(type)) {
					
					int replacingType;//replacing block //TODO: make replacing block list
					int replacingMeta = 0;//replacing meta
					
					if(config.getReplacingBlocks().size() == 0)//set to 1 if nothing inside the list
						replacingType = 1;
					else{
						int random = (int) (Math.random() * (config.getReplacingBlocks().size()));
						replacingType = config.getReplacingBlocks().get(random);//get a random block from list
					}

					short combine = 0;

					//backup
					combine = (short) ((replacingType << 4) | replacingMeta);//combine type and meta

					//Bukkit.getLogger().log(Level.INFO,"type ["+type+"] found and changed into ["+replacingType+"]");
					
					data = combine;//replace previous data
					
					
					LER.putShort(data);//write new data
					
					//Bukkit.getLogger().info("sky: "+skylight+" block: "+blocklight
					//		+" type: "+replacingType+" meta: "+replacingMeta);
				}		
			}
			
		}else{
			while(LER.getRemaining() > 0){
				byte data = LER.readByte();

				//Bukkit.getLogger().log(Level.INFO,"type ["+data+"] found");
				//if ore
				if (config.getOres().contains(Integer.valueOf(data))) {
					int replacingType;//replacing block //TODO: make replacing block list
					
					if(config.getReplacingBlocks().size() == 0)//set to 1 if nothing inside the list
						replacingType = 1;
					else{
						int random = (int) (Math.random() * (config.getReplacingBlocks().size()));
						replacingType = config.getReplacingBlocks().get(random);//get a random block from list
					}

					//Bukkit.getLogger().log(Level.INFO,"type ["+data+"] found and changed into ["+replacingType+"]");
					
					data = (byte) replacingType;//replace previous data
					LER.putByte(data);//write new data
				}	
			}
		}
		
		System.arraycopy(LER.toByteArray(), 0, original, 0, original.length);
	}
	
	public static void checkAndCopy(char[] original){
		byte[] b = new byte[original.length];
		int i = 0;
		for(char c : original){
			b[i] = (byte) c; i++;
		}
		
		System.arraycopy(b, 0, original, 0, original.length);
	}

}
