package com.hideores.listener;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import com.hideores.main.HideOres;

public class PlayerMoveHandler implements Listener {
	private HideOres plugin;
	
	private static List<Integer> ores = HideOres.config.getOres();
	
	private static ConcurrentMap<String, Location> lastLocations = new ConcurrentHashMap<String, Location>();
	private static ConcurrentMap<Coordinate, Long> notifiedBlocks = new ConcurrentHashMap<Coordinate, Long>();
	
	public PlayerMoveHandler(Plugin plugin){
		this.plugin = (HideOres) plugin;
		
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run() {
				for(Map.Entry<Coordinate, Long> entry : notifiedBlocks.entrySet()){
					Coordinate key = entry.getKey();
					Long timestamp = entry.getValue();
					
					if(timestamp + 500 > new Date().getTime()){
						continue;
					}
					
					notifiedBlocks.remove(key);
				}
			}
		}, 20L, 1L);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent e){
		Player player = e.getPlayer();
		
		lastLocations.putIfAbsent(player.getName(), player.getLocation());
		
		if(!lastLocations.get(player.getName()).getWorld().getName().equals(e.getTo().getWorld().getName())){
			lastLocations.put(player.getName(), e.getTo());
		}
		
		Location lastLoc = lastLocations.get(player.getName());
		Location currentLoc = e.getTo();
		
		if(Math.abs(lastLoc.getX() - currentLoc.getX()) < 0.7
				&& Math.abs(lastLoc.getY() - currentLoc.getY()) < 0.7
				&& Math.abs(lastLoc.getZ() - currentLoc.getZ()) < 0.7){
			return;
		}
		lastLocations.put(player.getName(), e.getTo());
		
		int radius = HideOres.config.getRadius();
		
		int currentX = player.getLocation().getBlockX();
		int currentY = player.getLocation().getBlockY();
		int currentZ = player.getLocation().getBlockZ();
		//if(player.isOp())
			//player.sendMessage("move = " + currentX+","+currentY+","+currentZ);
		
		for(int y = -radius; y < radius; y++){
			for(int x = -radius; x < radius; x++){
				for(int z = -radius; z < radius; z++){
					Coordinate coord = new Coordinate(currentX+x, currentY+y, currentZ+z);
					if(notifiedBlocks.containsKey(coord)) continue;

					Block block = player.getWorld().getBlockAt(coord.getX(), coord.getY(), coord.getZ());
					int blockID = block.getTypeId();

					if (ores.contains(blockID)) {
						if (plugin.getBlockNotifier().canSee(block)) {
							plugin.getBlockNotifier().notifyBlock(player, coord.getX(), coord.getY(), coord.getZ());
							notifiedBlocks.put(coord, new Date().getTime());
						}
					}

				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDig(BlockDamageEvent e){
		Player player = e.getPlayer();

		Location loc = e.getBlock().getLocation();
		
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		
		int cradius = 1;
		
		for (int x = -cradius; x < cradius; x++) {
			for (int y = -cradius; y < cradius; y++) {
				for (int z = -cradius; z < cradius; z++) {
					Coordinate coord = new Coordinate(blockX + x, blockY + y, blockZ + z);
					if(notifiedBlocks.containsKey(coord)) continue;

					Block block = player.getWorld().getBlockAt(coord.getX(), coord.getY(), coord.getZ());
					int blockID = block.getTypeId();

					if (ores.contains(blockID)) {
						if (plugin.getBlockNotifier().canSee(block) && !block.isBlockPowered()) {
							plugin.getBlockNotifier().notifyBlock(player, coord.getX(), coord.getY(), coord.getZ());
							notifiedBlocks.put(coord, new Date().getTime());
						}
					}
				}

			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onClick(PlayerInteractEvent e){
		Player player = e.getPlayer();
		
		if(e.getClickedBlock() == null)
			return;
		
		Location loc = e.getClickedBlock().getLocation();
		
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		
		int cradius = 1;
		
		for (int x = -cradius; x < cradius; x++) {
			for (int y = -cradius; y < cradius; y++) {
				for (int z = -cradius; z < cradius; z++) {
					Coordinate coord = new Coordinate(blockX + x, blockY + y, blockZ + z);
					if(notifiedBlocks.containsKey(coord)) continue;

					Block block = player.getWorld().getBlockAt(coord.getX(), coord.getY(), coord.getZ());
					int blockID = block.getTypeId();

					if (ores.contains(blockID)) {
						if (plugin.getBlockNotifier().canSee(block) && !block.isBlockPowered()) {
							plugin.getBlockNotifier().notifyBlock(player, coord.getX(), coord.getY(), coord.getZ());
							notifiedBlocks.put(coord, new Date().getTime());
						}
					}

				}
			}
		}
	}
	
	public void onQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		
		lastLocations.remove(player.getName());
	}
}
