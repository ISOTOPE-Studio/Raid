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
			if (args.length != 1) {
				sender.sendMessage(Raid.prefix + "/raid <数字> 打开菜单");
				return true;
			}
			int num = 0;
			try {
				num = Integer.parseInt(args[0]);
			} catch (Exception e) {
				sender.sendMessage(Raid.prefix + "/raid <数字> 打开菜单");
			}
			try {
				Data.gui.get(num).open((Player) sender);
			} catch (Exception e) {
				sender.sendMessage(Raid.prefix + "菜单不存在");
			}
			return true;
		}
		return false;
	}
}
