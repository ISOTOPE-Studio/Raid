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
				sender.sendMessage(Raid.prefix + "����Ҫ��Ҳ���ִ��");
				return true;
			}
			if (!sender.hasPermission("raid.admin")) {
				sender.sendMessage(Raid.prefix + "��û��Ȩ��");
				return true;
			}
			if (args.length != 2) {
				sender.sendMessage(Raid.prefix + "/raidadmin <����ID> pos1 - ���õ�һ�������");
				sender.sendMessage(Raid.prefix + "/raidadmin <����ID> pos2 - ���õڶ��������");
				sender.sendMessage(Raid.prefix + "/raidadmin <����ID> tp - ���ô��͵�");
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
			}
			Location loc = ((Player) sender).getLocation();
			InstanceData data = new InstanceData(plugin);
			if (args[1].equalsIgnoreCase("pos1")) {
				data.setInstancePos1(num, loc);
				sender.sendMessage(Raid.prefix + "�ɹ����ø���" + args[0] + "�ĵ�һ������");
			}
			if (args[1].equalsIgnoreCase("pos2")) {
				data.setInstancePos2(num, loc);
				sender.sendMessage(Raid.prefix + "�ɹ����ø���" + args[0] + "�ĵڶ�������");
			}
			if (args[1].equalsIgnoreCase("tp")) {
				data.setInstancetp(num, loc);
				sender.sendMessage(Raid.prefix + "�ɹ����ø���" + args[0] + "�Ĵ��͵�");
			}
			return true;
		}
		return false;
	}
}
