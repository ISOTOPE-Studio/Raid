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
				sender.sendMessage(Raid.prefix + "����Ҫ��Ҳ���ִ��");
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(Raid.prefix + "/raid <����> �򿪲˵�");
				return true;
			}
			int num = 0;
			try {
				num = Integer.parseInt(args[0]);
			} catch (Exception e) {
				sender.sendMessage(Raid.prefix + "/raid <����> �򿪲˵�");
			}
			try {
				Data.gui.get(num).open((Player) sender);
			} catch (Exception e) {
				sender.sendMessage(Raid.prefix + "�˵�������");
			}
			return true;
		}
		return false;
	}
}
