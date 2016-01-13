package com.hideores.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.hideores.core.v1_8_R2.PlayerHooker;
import com.hideores.main.HideOres;

public class PlayerJoinEventHandler implements Listener{
	private HideOres plugin;
	public PlayerJoinEventHandler(Plugin plugin){
		this.plugin = (HideOres) plugin;
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		plugin.getPlayerHooker().hookPlayer(player);
	}
}
