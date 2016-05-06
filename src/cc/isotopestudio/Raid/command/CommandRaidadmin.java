package cc.isotopestudio.Raid.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.isotopestudio.Raid.Raid;
import cc.isotopestudio.Raid.data.InstanceData;

public class CommandRaidadmin implements CommandExecutor {

	private final Raid plugin;

	public CommandRaidadmin(Raid plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("raidadmin")) {

			if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
				plugin.onReload();
				sender.sendMessage(Raid.prefix + "成功重载");
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(Raid.prefix + "必须要玩家才能执行");
				return true;
			}
			if (!sender.hasPermission("raid.admin")) {
				sender.sendMessage(Raid.prefix + "你没有权限");
				return true;
			}
			if (!(args.length == 2 || args.length == 3)) {
				sender.sendMessage(Raid.prefix + "/raidadmin <副本ID> pos1 - 设置第一个坐标点");
				sender.sendMessage(Raid.prefix + "/raidadmin <副本ID> pos2 - 设置第二个坐标点");
				sender.sendMessage(Raid.prefix + "/raidadmin <副本ID> tp - 设置传送点");
				sender.sendMessage(Raid.prefix + "/raidadmin <副本ID> limit <数量> - 设置副本玩家限制");
				sender.sendMessage(Raid.prefix + "/raidadmin <副本ID> time <数量> - 设置副本时间限制");
				sender.sendMessage(Raid.prefix + "/raidadmin reload - 重载配置文件");
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
				return true;
			}
			Location loc = ((Player) sender).getLocation();
			InstanceData data = new InstanceData(plugin);
			if (args[1].equalsIgnoreCase("pos1")) {
				data.setPos1(num, loc);
				sender.sendMessage(Raid.prefix + "成功设置副本" + args[0] + "的第一个坐标");
				return true;
			}
			if (args[1].equalsIgnoreCase("pos2")) {
				data.setPos2(num, loc);
				sender.sendMessage(Raid.prefix + "成功设置副本" + args[0] + "的第二个坐标");
				return true;
			}
			if (args[1].equalsIgnoreCase("tp")) {
				data.settp(num, loc);
				sender.sendMessage(Raid.prefix + "成功设置副本" + args[0] + "的进入传送点");
				return true;
			}
			if (args[1].equalsIgnoreCase("limit")) {
				data.setLimit(num, Integer.parseInt(args[2]));
				sender.sendMessage(Raid.prefix + "成功设置副本" + args[0] + "的玩家限制");
				return true;
			}
			if (args[1].equalsIgnoreCase("time")) {
				data.setTimeLimit(num, Integer.parseInt(args[2]));
				sender.sendMessage(Raid.prefix + "成功设置副本" + args[0] + "的时间限制");
				return true;
			}
			return true;
		}
		return false;
	}
}
