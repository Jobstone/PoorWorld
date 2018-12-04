package com.gmail.jobstone;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PoorWorldExecutor implements CommandExecutor {
	
	@SuppressWarnings("unused")
	private final PoorWorld plugin;
	  
	public PoorWorldExecutor(PoorWorld plugin)
	{
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("poorworld")) {
				
				if (args.length == 1) {
					if (args[0].equals("survival")) {
						int from = WorldPlayer.getWorldPart(player.getWorld().getName());
						int to = 0;
						
						if (from != to) {
							if (!WorldPlayer.isCD(player)) {
								player.setShoulderEntityLeft(null);
								player.setShoulderEntityRight(null);
								WorldPlayer.saveInv(player, from);
								WorldPlayer.saveData(player, from);
								WorldPlayer.loadInv(player, to);
								WorldPlayer.loadData(player, to);
								WorldPlayer.setCD(player, 60);
								WorldPlayer.setName(player);
								player.sendMessage("��7��PoorWorld�����������������磡��");
							}
							else
								player.sendMessage("��7��PoorWorld����������-����������ƶ���Ҫ�ȴ�1���ӣ�����������ȴ�"+WorldPlayer.getCD(player)+"�롣");
						}
						else
							player.sendMessage("��7��PoorWorld�������ھ����������磡");
						
					}
					else if (args[0].equals("creative")) {
						int from = WorldPlayer.getWorldPart(player.getWorld().getName());
						int to = 1;
						
						if (from != to) {
							if (!WorldPlayer.isCD(player)) {
								WorldPlayer.saveInv(player, from);
								WorldPlayer.saveData(player, from);
								WorldPlayer.loadInv(player, to);
								WorldPlayer.loadData(player, to);
								WorldPlayer.setCD(player, 60);
								WorldPlayer.setName(player);
								player.sendMessage("��7��PoorWorld���������˴������磡");
								
								Advancement adv = Bukkit.getAdvancement(new NamespacedKey("poorcraft", "enter_creative"));
								AdvancementProgress pro = player.getAdvancementProgress(adv);
								pro.awardCriteria("enter_creative");
							}
							else
								player.sendMessage("��7��PoorWorld����������-����������ƶ���Ҫ�ȴ�1���ӣ�����������ȴ�"+WorldPlayer.getCD(player)+"�롣");
						}
						else
							player.sendMessage("��7��PoorWorld�������ھ��ڴ������磡");
					}
				}
				
			}
		}
		
		return true;
	}
	
	

}
