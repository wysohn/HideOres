package com.hideores.core.v1_8_R3;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.hideores.core.Iinstance.IBlockNotifier;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.World;

public class BlockNotifier implements IBlockNotifier{
	public void notifyBlock(Player player, double x, double y, double z){
		CraftPlayer p = (CraftPlayer) player;
		//World world = p.getHandle().getWorld();
		
		Location loc = new Location(player.getWorld(), x, y, z);
		loc.getBlock().getState().update(true);
		
		//world.notify(new BlockPosition(x, y, z));
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
					
					if(!Block.getById(BlockID).isOccluding()) return true;
				}
			}
		}
		
		return false;
	}
}
