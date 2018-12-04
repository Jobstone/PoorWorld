package com.gmail.jobstone;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PoorWorldTabCompleter implements TabCompleter {
	
	public PoorWorldTabCompleter(){}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = new ArrayList<String>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equals("poorworld")) {
				if (args.length == 1) {
					int from = WorldPlayer.getWorldPart(player.getWorld().getName());
					if (from == 1)
						list.add("survival");
					else
						list.add("creative");
				}
			}
		}
		return list;
	}

}
