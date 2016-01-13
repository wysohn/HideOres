package com.hideores.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class HideOresConfigs {
	private FileConfiguration config;
	private JavaPlugin plugin;
	
	public FileConfiguration getConfig() {
		return config;
	}

	private List<Integer> ores = new ArrayList<Integer>(){{add(56);}};
	private List<Integer> replacingBlocks = new ArrayList<Integer>(){{add(1);add(3);}};
	private int radius = 6;
	
	public HideOresConfigs(JavaPlugin plugin){
		this.config = plugin.getConfig();
		this.plugin = plugin;
		
		config.options().copyDefaults(true);
		config.addDefault("ores", ores);
		config.addDefault("replacingBlocks", replacingBlocks);
		config.addDefault("radius", radius);
		
		loadConfigs();
		
		plugin.saveConfig();
	}
	
	public void loadConfigs(){
		ores = config.getIntegerList("ores");
		replacingBlocks = config.getIntegerList("replacingBlocks");
	}
	public void saveConfigs(){
		config.set("ores", this.getOres());
		config.set("replacingBlocks", this.getReplacingBlocks());
		
		plugin.saveConfig();
	}

	public List<Integer> getOres() {
		return ores;
	}

	public void setOres(List<Integer> ores) {
		this.ores = ores;
	}

	public List<Integer> getReplacingBlocks() {
		return replacingBlocks;
	}

	public void setReplacingBlocks(List<Integer> replacingBlocks) {
		this.replacingBlocks = replacingBlocks;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
}
