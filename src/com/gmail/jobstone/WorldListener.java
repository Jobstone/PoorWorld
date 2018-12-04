package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class WorldListener implements Listener {
	
	private final PoorWorld plugin;
	
	public WorldListener (PoorWorld plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void createDirectory (PlayerJoinEvent e) {
		Player player = e.getPlayer();
		WorldPlayer.setName(player);
		File pFile = new File(plugin.getDataFolder(), "players/"+e.getPlayer().getName());
		if (!pFile.exists()) {
			pFile.mkdirs();
			File file0 = new File(pFile, "survival_inv.yml");
			FileConfiguration config0 = YamlConfiguration.loadConfiguration(file0);
			File file1 = new File(pFile, "survival_data.yml");
			FileConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
			File file2 = new File(pFile, "creative_inv.yml");
			FileConfiguration config2 = YamlConfiguration.loadConfiguration(file2);
			File file3 = new File(pFile, "creative_data.yml");
			FileConfiguration config3 = YamlConfiguration.loadConfiguration(file3);
			config1.createSection("location", Bukkit.getWorld("world").getSpawnLocation().serialize());
			config1.createSection("spawnlocation", Bukkit.getWorld("world").getSpawnLocation().serialize());
			config3.createSection("location", Bukkit.getWorld("creative").getSpawnLocation().serialize());
			config3.createSection("spawnlocation", Bukkit.getWorld("creative").getSpawnLocation().serialize());
			try {
				config0.save(file0);
				config1.save(file1);
				config2.save(file2);
				config3.save(file3);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			WorldPlayer.saveInv(player, 0);
			WorldPlayer.saveInv(player, 1);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void creativeExp (PlayerExpChangeEvent e) {
		if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			e.setAmount(0);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void noTP (EntityPortalEvent e) {
		if (e.getFrom().getWorld().getName().equals("creative"))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void chat (AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		String world = WorldPlayer.worldString(WorldPlayer.getWorldPart(player.getWorld().getName()));
		e.setFormat("¡ìa["+world+"]¡ìr"+e.getFormat());
	}

}
