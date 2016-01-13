package com.hideores.main;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.hideores.cache.ChunkCoord;
import com.hideores.cache.ChunkMapCache;
import com.hideores.core.Iinstance.IBlockNotifier;
import com.hideores.core.Iinstance.IMapChunkCalculation;
import com.hideores.core.Iinstance.IPlayerHooker;
import com.hideores.listener.PlayerJoinEventHandler;
import com.hideores.listener.PlayerMoveHandler;
import com.hideores.utils.ReflectionHelper;

public class HideOres extends JavaPlugin {
	private static Plugin instance;
	public static HideOresConfigs config;
	
	private static String version;
	private static String packageName = "com.hideores.core";
	
	private static boolean usePaperSpigot = false;
	private static boolean useSpigot = false;
	
	private IBlockNotifier iBlockNotifier;
	private IMapChunkCalculation iMapChunkCalculation;
	private IPlayerHooker iPlayerHooker;
	
	private static CacheManager cacheManager;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		return super.onCommand(sender, command, label, args);
	}

	@Override
	public void onDisable() {
		///////////////////////////////////////
		this.reloadConfig();
		
		this.saveConfig();
		///////////////////////////////////////////

	}

	@Override
	public void onEnable() {
		instance = this;
		config = new HideOresConfigs(this);
		///////////////////////////////////////////
		
		String packageName = getServer().getClass().getPackage().getName();
		version = packageName.substring(packageName.lastIndexOf('.') + 1);
		
        if(Package.getPackage("org.github.paperspigot") != null){
            getLogger().info("PaperSpigot Detected!");
            getLogger().info("using custom packet for PaperSpigot");
            usePaperSpigot = true;
            
            HideOres.packageName += ".paperspigot";
            
            disableSpigotXray();
        }else if(Package.getPackage("org.spigotmc") != null){
            getLogger().info("Spigot Detected!");
            getLogger().info("disabling spigot's built-in anti-xray for better support.");
            useSpigot = true;
            
            disableSpigotXray();
        }
		
		try {
			getLogger().info("preparing support for verison : "+version);
			iBlockNotifier = (IBlockNotifier) createInstance(IBlockNotifier.class, "BlockNotifier");
			iMapChunkCalculation = (IMapChunkCalculation) createInstance(IMapChunkCalculation.class, "MapChunkCalculation");
			iPlayerHooker = (IPlayerHooker) createInstance(IPlayerHooker.class, "PlayerHooker");
			
			getLogger().info("initiation complete : "+version);
		}catch(ClassNotFoundException e){
			getLogger().warning(version+" is not compatible with this plugin.");
			getLogger().warning("disabling...");
			getServer().getPluginManager().disablePlugin(this);
		}catch (InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			getLogger().warning("initiation failed.");
			getLogger().warning("disabling...");
			getServer().getPluginManager().disablePlugin(this);
			e.printStackTrace();
		}
		
		cacheManager = new CacheManager(this);
		
		try{
			getServer().getPluginManager().registerEvents(new PlayerJoinEventHandler(this),this);
			getServer().getPluginManager().registerEvents(new PlayerMoveHandler(this),this);
		}catch(Exception e){}

		
		
	}
	
	private void disableSpigotXray(){
        try {
        	Class<?> craftworldClazz = Class.forName("org.bukkit.craftbukkit."+version+".CraftWorld");
			Class<?> worldclazz = Class.forName("net.minecraft.server."+version+".World");
			
			for(World world : getServer().getWorlds()){
				
				Method gethandle = craftworldClazz.getMethod("getHandle");
				Object nmsworld = worldclazz.cast(gethandle.invoke(craftworldClazz.cast(world)));
				Field configField = worldclazz.getDeclaredField("spigotConfig");
				Object config = configField.get(nmsworld);
				ReflectionHelper.setPrivateField(config, "antiXray", Boolean.valueOf(false));
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	public static Plugin getInstance(){
		return instance;
	}
	
	public IBlockNotifier getBlockNotifier() {
		return iBlockNotifier;
	}

	public IMapChunkCalculation getMapChunkCalculation() {
		return iMapChunkCalculation;
	}

	public IPlayerHooker getPlayerHooker() {
		return iPlayerHooker;
	}

	public static boolean isUsePaperSpigot() {
		return usePaperSpigot;
	}

	public static boolean isUseSpigot() {
		return useSpigot;
	}
	
	

	public static CacheManager getCacheManager() {
		return cacheManager;
	}

	public static Object createInstance(Class<?> targetClass, String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Class<?> clazz = Class.forName(packageName+"."+version+"."+className);
		return targetClass.cast(clazz.getConstructor().newInstance());
	}
}
