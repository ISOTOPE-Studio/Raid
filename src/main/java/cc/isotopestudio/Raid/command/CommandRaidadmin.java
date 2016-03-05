package cc.isotopestudio.Raid.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.isotopestudio.Raid.Raid;
import cc.isotopestudio.Raid.data.Data;
import cc.isotopestudio.Raid.data.InstanceData;

public class CommandRaidadmin implements CommandExecutor {

	private final Raid plugin;

	public CommandRaidadmin(Raid plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("raidadmin")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Raid.prefix + "必须要玩家才能执行");
				return true;
			}
			if (!sender.hasPermission("raid.admin")) {
				sender.sendMessage(Raid.prefix + "你没有权限");
				return true;
			}
			if (args.length != 2) {
				sender.sendMessage(Raid.prefix + "/raidadmin <副本ID> pos1 - 设置第一个坐标点");
				sender.sendMessage(Raid.prefix + "/raidadmin <副本ID> pos2 - 设置第二个坐标点");
				sender.sendMessage(Raid.prefix + "/raidadmin <副本ID> tp - 设置传送点");
				return true;
			}
			int num = 0;
			try {
				num = Integer.parseInt(args[0]);
				if (num < 1) {
					sender.sendMessage(Raid.prefix + args[0] + "不是一个有效的数字");
					return true;
				}
			} catch (Exception e) {
				sender.sendMessage(Raid.prefix + args[0] + "不是一个有效的数字");
			}
			Location loc = ((Player) sender).getLocation();
			InstanceData data = new InstanceData(plugin);
			if (args[1].equalsIgnoreCase("pos1")) {
				data.setInstancePos1(num, loc);
				sender.sendMessage(Raid.prefix + "成功设置副本" + args[0] + "的第一个坐标");
			}
			if (args[1].equalsIgnoreCase("pos2")) {
				data.setInstancePos2(num, loc);
				sender.sendMessage(Raid.prefix + "成功设置副本" + args[0] + "的第二个坐标");
			}
			if (args[1].equalsIgnoreCase("tp")) {
				data.setInstancetp(num, loc);
				sender.sendMessage(Raid.prefix + "成功设置副本" + args[0] + "的传送点");
			}
			return true;
		}
		return false;
	}
}
