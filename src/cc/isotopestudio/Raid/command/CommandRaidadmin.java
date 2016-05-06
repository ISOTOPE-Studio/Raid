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
				sender.sendMessage(Raid.prefix + "�ɹ�����");
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(Raid.prefix + "����Ҫ��Ҳ���ִ��");
				return true;
			}
			if (!sender.hasPermission("raid.admin")) {
				sender.sendMessage(Raid.prefix + "��û��Ȩ��");
				return true;
			}
			if (!(args.length == 2 || args.length == 3)) {
				sender.sendMessage(Raid.prefix + "/raidadmin <����ID> pos1 - ���õ�һ�������");
				sender.sendMessage(Raid.prefix + "/raidadmin <����ID> pos2 - ���õڶ��������");
				sender.sendMessage(Raid.prefix + "/raidadmin <����ID> tp - ���ô��͵�");
				sender.sendMessage(Raid.prefix + "/raidadmin <����ID> limit <����> - ���ø����������");
				sender.sendMessage(Raid.prefix + "/raidadmin <����ID> time <����> - ���ø���ʱ������");
				sender.sendMessage(Raid.prefix + "/raidadmin reload - ���������ļ�");
				return true;
			}
			int num = 0;
			try {
				num = Integer.parseInt(args[0]);
				if (num < 1) {
					sender.sendMessage(Raid.prefix + args[0] + "����һ����Ч������");
					return true;
				}
			} catch (Exception e) {
				sender.sendMessage(Raid.prefix + args[0] + "����һ����Ч������");
				return true;
			}
			Location loc = ((Player) sender).getLocation();
			InstanceData data = new InstanceData(plugin);
			if (args[1].equalsIgnoreCase("pos1")) {
				data.setPos1(num, loc);
				sender.sendMessage(Raid.prefix + "�ɹ����ø���" + args[0] + "�ĵ�һ������");
				return true;
			}
			if (args[1].equalsIgnoreCase("pos2")) {
				data.setPos2(num, loc);
				sender.sendMessage(Raid.prefix + "�ɹ����ø���" + args[0] + "�ĵڶ�������");
				return true;
			}
			if (args[1].equalsIgnoreCase("tp")) {
				data.settp(num, loc);
				sender.sendMessage(Raid.prefix + "�ɹ����ø���" + args[0] + "�Ľ��봫�͵�");
				return true;
			}
			if (args[1].equalsIgnoreCase("limit")) {
				data.setLimit(num, Integer.parseInt(args[2]));
				sender.sendMessage(Raid.prefix + "�ɹ����ø���" + args[0] + "���������");
				return true;
			}
			if (args[1].equalsIgnoreCase("time")) {
				data.setTimeLimit(num, Integer.parseInt(args[2]));
				sender.sendMessage(Raid.prefix + "�ɹ����ø���" + args[0] + "��ʱ������");
				return true;
			}
			return true;
		}
		return false;
	}
}
