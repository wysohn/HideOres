package com.hideores.core.v1_7_R1;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.hideores.core.Iinstance.IBlockNotifier;

import net.minecraft.server.v1_7_R1.World;
import net.minecraft.server.v1_7_R1.Block;

public class BlockNotifier implements IBlockNotifier{
	public void notifyBlock(Player player, double x, double y, double z){
		CraftPlayer p = (CraftPlayer) player;
		World world = p.getHandle().getWorld();
		
		world.notify((int)x, (int)y, (int)z);
	}
	
	@Override
	public boolean canSee(org.bukkit.block.Block block) {
		org.bukkit.World world = block.getWorld();
		
		int radius = 1;
		
		int blockX = block.getX();
		int blockY = block.getY();
		int blockZ = block.getZ();
		
		for(int x = -radius; x <= radius ; x++){
			for(int y = -radius; y <= radius ; y++){
				for(int z = -radius; z <= radius ; z++){
					if(x == 0 && y == 0 && z == 0) continue;
					
					int BlockID = world.getBlockAt(blockX + x, blockY + y, blockZ + z).getTypeId();
					
					if(!Block.e(BlockID).r()) return true;
				}
			}
		}
		
		return false;
	}
}
