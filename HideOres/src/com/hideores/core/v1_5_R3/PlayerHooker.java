package com.hideores.core.v1_5_R3;

import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.hideores.core.Iinstance.IPlayerHooker;
import com.hideores.utils.ReflectionHelper;

public class PlayerHooker implements IPlayerHooker{
	public void hookPlayer(Player player){
        CraftPlayer p = (CraftPlayer) player;
        ReflectionHelper.setPrivateFinal(p.getHandle(), "chunkCoordIntPairQueue", new ChunkCoordQueue(p));
	}
}
