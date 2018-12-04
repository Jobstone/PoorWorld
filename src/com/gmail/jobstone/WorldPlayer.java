package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class WorldPlayer {
	
	public WorldPlayer (PoorWorld plugin) {
		
	}
	
	public static void saveInv(Player player, int world) {
		String w = worldString(world);
		
		File file = new File(PoorWorld.plugin.getDataFolder(), "players/"+player.getName()+"/"+w+"_inv.yml");
		FileConfiguration config = PoorWorld.plugin.getConfig();
		ConfigurationSection bagcontent = config.getConfigurationSection("bag");
		PlayerInventory baginv = player.getInventory();
		for (int i = 0; i < baginv.getSize(); i++) {
			ItemStack item = baginv.getItem(i) == null ? new ItemStack(Material.AIR) : baginv.getItem(i);
			bagcontent.createSection(String.valueOf(i), item.serialize());
		}
		Inventory enderinv = player.getEnderChest();
		ConfigurationSection endercontent = config.getConfigurationSection("ender");
		for (int i = 0; i < enderinv.getSize(); i++) {
			ItemStack item = enderinv.getItem(i) == null ? new ItemStack(Material.AIR) : enderinv.getItem(i);
			endercontent.createSection(String.valueOf(i), item.serialize());
		}
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadInv(Player player, int world) {
		String w = worldString(world);
		
		File file = new File(PoorWorld.plugin.getDataFolder(), "players/"+player.getName()+"/"+w+"_inv.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		PlayerInventory baginv = player.getInventory();
		ConfigurationSection bagcontent = config.getConfigurationSection("bag");
		for (int i = 0; i < baginv.getSize(); i++) {
			ItemStack it = ItemStack.deserialize(bagcontent.getConfigurationSection(String.valueOf(i)).getValues(false));
			baginv.setItem(i, it);
		}
		Inventory enderinv = player.getEnderChest();
		ConfigurationSection endercontent = config.getConfigurationSection("ender");
		for (int i = 0; i < enderinv.getSize(); i++) {
			ItemStack it = ItemStack.deserialize(endercontent.getConfigurationSection(String.valueOf(i)).getValues(false));
			enderinv.setItem(i, it);
		}
	}
	
	public static void saveData(Player player, int world) {
		String w = worldString(world);
		
		File file = new File(PoorWorld.plugin.getDataFolder(), "players/"+player.getName()+"/"+w+"_data.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.createSection("location", player.getLocation().serialize());
		Location loc;
		if (player.getBedSpawnLocation() != null)
			loc = player.getBedSpawnLocation();
		else
			loc = player.getWorld().getSpawnLocation();
		config.createSection("spawnlocation", loc.serialize());
		config.set("canfly", player.getAllowFlight());
		config.set("isfly", player.isFlying());
		config.set("saturation", (double) player.getSaturation());
		config.set("foodlevel", player.getFoodLevel());
		config.set("health", player.getHealth());
		config.set("velocity", player.getVelocity());
		config.set("falldistance", (double) player.getFallDistance()); 
		config.set("fireticks", player.getFireTicks());
		config.set("gravity", player.hasGravity());
		if (!player.getActivePotionEffects().isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (PotionEffect effect : player.getActivePotionEffects())
				list.add(effect.serialize());
			config.set("potioneffects", list);
		}
		else
			config.set("potioneffects", null);
		config.set("air", player.getRemainingAir());
		config.set("collidable", player.isCollidable());
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void loadData(Player player, int world) {
		GameMode mode = (world == 0) ? GameMode.SURVIVAL :GameMode.CREATIVE;
		player.setGameMode(mode);
		
		String w = worldString(world);
		
		File file = new File(PoorWorld.plugin.getDataFolder(), "players/"+player.getName()+"/"+w+"_data.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		Location loc;
		if (config.isConfigurationSection("location"))
			loc = Location.deserialize(config.getConfigurationSection("location").getValues(false));
		else
			loc = (world == 0) ? Bukkit.getWorld("world").getSpawnLocation() : Bukkit.getWorld("creative").getSpawnLocation();
		player.teleport(loc);
		
		Location spawnloc;
		if (config.isConfigurationSection("spawnlocation"))
			spawnloc = Location.deserialize(config.getConfigurationSection("spawnlocation").getValues(false));
		else
			spawnloc = (world == 0) ? Bukkit.getWorld("world").getSpawnLocation() : Bukkit.getWorld("creative").getSpawnLocation();
		player.setBedSpawnLocation(spawnloc, true);
		
		boolean canfly = (world == 0) ? false : true;
		player.setAllowFlight(canfly);
		
		boolean isfly;
		if (config.contains("isfly"))
			isfly = config.getBoolean("isfly");
		else
			isfly = false;
		player.setFlying(isfly);
		
		if (config.contains("saturation"))
			player.setSaturation((float)Math.round(config.getDouble("saturation")*1000)/1000);
		
		if (config.contains("foodlevel"))
			player.setFoodLevel(config.getInt("foodlevel"));
		
		if (config.contains("health"))
			player.setHealth((double)Math.round(config.getDouble("health")*1000)/1000);
		
		if (config.isConfigurationSection("velocity"))
			player.setVelocity(config.getVector("velocity"));
		
		if (config.contains("falldistance"))
			player.setFallDistance((float)Math.round(config.getDouble("falldistance")*10000)/10000);
		
		if (config.contains("fireticks"))
			player.setFireTicks(config.getInt("fireticks"));
		
		if (config.contains("gravity"))
			player.setGravity(config.getBoolean("gravity"));
		
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		if (config.isList("potioneffects"))
			for (Map<?, ?> map : config.getMapList("potioneffects"))
				player.addPotionEffect(new PotionEffect((Map<String, Object>) map));
		
		if (config.contains("air"))
			player.setRemainingAir(config.getInt("air"));
		
		if (config.contains("collidable"))
			player.setCollidable(config.getBoolean("collidable"));
	}
	
	public static String worldString(int world) {
		switch(world) {
		case 0:
			return "survival";
		case 1:
			return "creative";
		default:
			return null;
		}
	}
	
	private static Map<Player, Long> coolDown = new HashMap<Player, Long>();

	public static boolean isCD(Player player) {
		if (coolDown.containsKey(player) && coolDown.get(player) > System.currentTimeMillis())
			return true;
		else
			return false;
	}

	public static void setCD(Player player, int i) {
		Long cd = System.currentTimeMillis();
		coolDown.put(player, cd+1000*i);
	}
	
	public static int getCD(Player player) {
		return (int)((coolDown.get(player) - System.currentTimeMillis())/1000);
	}
	
	public static void setName(Player player) {
		String world = WorldPlayer.worldString(WorldPlayer.getWorldPart(player.getWorld().getName()));
		String name = "¡ìa["+world+"]¡ìr "+player.getName();
		player.setPlayerListName(name);
	}
	
	public static int getWorldPart(String world) {
		switch(world) {
		case "world":
		case "world_nether":
		case "world_the_end":
			return 0;
		case "creative":
			return 1;
		default:
			return -1;
		}
	}

}
