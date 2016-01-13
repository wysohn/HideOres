package com.hideores.core.Iinstance;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface IBlockNotifier {
	public void notifyBlock(Player player, double x, double y, double z);
    public boolean canSee(Block block);
}
