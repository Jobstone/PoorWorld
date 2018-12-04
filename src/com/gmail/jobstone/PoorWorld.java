package com.gmail.jobstone;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PoorWorld extends JavaPlugin {
	
	public static PoorWorld plugin;
	
	public void onEnable() {
		
		this.saveDefaultConfig();
		PoorWorld.plugin = this;
		PluginCommand command = getCommand("poorworld");
		command.setExecutor(new PoorWorldExecutor(this));
		command.setTabCompleter(new PoorWorldTabCompleter());
		new WorldListener(this);
		new WorldPlayer(this);
		
		new BukkitRunnable() {
			public void run() {
				
				for (Player player :Bukkit.getOnlinePlayers()) {
					if (PoorLogin.logined.contains(player.getName()))
						WorldPlayer.saveData(player, WorldPlayer.getWorldPart(player.getWorld().getName()));
				}
				
			}
		}.runTaskTimerAsynchronously(this, 600, 600);
		
		WorldCreator worldcreator = new WorldCreator("creative");
		Bukkit.createWorld(worldcreator);
		
	}
	
}
