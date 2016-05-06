package cc.isotopestudio.Raid.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.isotopestudio.Raid.Raid;
import cc.isotopestudio.Raid.data.Data;

public class CommandRaid implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("raid")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Raid.prefix + "必须要玩家才能执行");
				return true;
			}
			Player player = (Player) sender;
			if (args.length != 1) {
				player.sendMessage(Raid.prefix + "/raid <数字> 打开菜单");
				return true;
			}

			// Custom CMD
			int index = 0;
			for (String tempCmd : Data.guiCmd) {
				if (args[0].equalsIgnoreCase(tempCmd)) {
					Data.gui.get(index).open(player);
					return true;
				}
				index++;
			}

			// Normal index
			int num = 0;
			try {
				num = Integer.parseInt(args[0]);
			} catch (Exception e) {
				player.sendMessage(Raid.prefix + "/raid <数字> 打开菜单");
				return true;
			}
			Data.gui.get(num).open(player);
			/*
			try {
			} catch (Exception e) {
				sender.sendMessage(Raid.prefix + "菜单不存在");
				return true;
			}*/
			return true;
		}
		return false;
	}
}
