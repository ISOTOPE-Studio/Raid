package cc.isotopestudio.Raid.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cc.isotopestudio.Raid.Raid;
import cc.isotopestudio.Raid.data.Data;
import cc.isotopestudio.Raid.data.InstanceData;
import cc.isotopestudio.Raid.gui.ClassGUI;
import cc.isotopestudio.Raid.gui.ClassGUI.OptionClickEventHandler;

public class UpdateGUI extends BukkitRunnable {
	private final Raid plugin;

	public UpdateGUI(Raid plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < Data.gui.size(); i++) {
				Data.gui.get(i).Destory();
			}
		} catch (Exception e) {
		}
		Data.gui = new ArrayList<ClassGUI>();
		Data.guiCmd = new ArrayList<String>();
		Set<String> guiName = plugin.getGUIData().getKeys(false);
		Set<String> keys = plugin.getGUIData().getKeys(true);
		Iterator<String> it = guiName.iterator();
		Iterator<String> keyIt = keys.iterator();
		while (it.hasNext()) {
			int num = Integer.parseInt(it.next());
			int size = plugin.getGUIData().getInt(num + ".column") * 9;
			String cmd = plugin.getGUIData().getString(num + ".command", "" + num);
			Data.guiCmd.add(cmd);
			String name = plugin.getGUIData().getString(num + ".name");
			ClassGUI newGUI = new ClassGUI(name, size, plugin);
			ArrayList<String> itemIndexList = new ArrayList<String>();
			OptionClickEventHandler[] handler = new OptionClickEventHandler[size];
			while (keyIt.hasNext()) {
				String temp = keyIt.next();
				String[] tempKey = temp.split("\\.");
				if (tempKey.length == 3 && tempKey[1].equals("slot")) {
					itemIndexList.add(tempKey[2]);
				}
				if (temp.equals((num + 1) + "")) {
					break;
				}
			}
			ArrayList<Integer> playerList = new ArrayList<Integer>();
			final InstanceData data = new InstanceData(plugin);
			for (String itemIndex : itemIndexList) {
				String itemName = plugin.getGUIData().getString(num + ".slot." + itemIndex + ".name");
				int pos = plugin.getGUIData().getInt(num + ".slot." + itemIndex + ".pos");
				int damage = plugin.getGUIData().getInt(num + ".slot." + itemIndex + ".damage", 0);
				ItemStack item = getItemStack(plugin.getGUIData().getString(num + ".slot." + itemIndex + ".item"),
						damage);
				List<String> loreList = plugin.getGUIData().getStringList(num + ".slot." + itemIndex + ".lore");
				String[] lore = new String[loreList.size()];
				for (int i = 0; i < loreList.size(); i++) {
					if (loreList.get(i).contains("{") && loreList.get(i).contains("}")) {
						playerList.add(pos);
					}
					lore[i] = loreList.get(i);
				}
				newGUI.setOption(pos, item, itemName, lore);
				String function = plugin.getGUIData().getString(num + ".slot." + itemIndex + ".function", "null");
				if (function.contains("tp ")) {
					final int instance = Integer.parseInt(function.substring(3));
					handler[pos] = new ClassGUI.OptionClickEventHandler() {
						@SuppressWarnings("deprecation")
						@Override
						public void onOptionClick(ClassGUI.OptionClickEvent event) {
							int limit = data.getInstanceLimit(instance);
							int num = data.getNumPlayers(instance);
							Player player = event.getPlayer();
							if (!player.hasPermission("raid.tp." + instance)) {
								player.sendMessage(Raid.prefix + "你没有权限");
								return;
							}
							if (player.getLevel() < data.getInstanceLvLimit(instance)) {
								player.sendTitle("§c无法进入", "§a你的等级不够，需要" + data.getInstanceLvLimit(instance) + "级");
								return;
							}
							if (num >= limit) {
								player.sendMessage(Raid.prefix + "副本人数达到限制");
								return;
							}
							if (InstanceData.playerEnter.get(instance) == null) {
								InstanceData.playerEnter.put(instance, new HashMap<String, Long>());
							}
							if (data.getInstanceDayLimit(instance) != -1
									&& plugin.getPlayerData().getInt(instance + "." + player.getName(), -100) >= data
											.getInstanceDayLimit(instance)) {
								player.sendTitle("§c无法进入", "§a今天已达到进入此副本的限制" + data.getInstanceDayLimit(instance));
								return;
							}
							long now = new Date().getTime();
							if (InstanceData.playerEnter.get(instance).get(player.getName()) != null) {
								long last = InstanceData.playerEnter.get(instance).get(player.getName());
								int interval = data.getInstanceInterval(instance) * 60 * 1000;
								System.out.print(now);
								System.out.print(last);
								System.out.print(interval);
								if (last + interval > now) {
									player.sendTitle("§c无法进入",
											"§a你还需要等待" + (int) (((last + interval - now) / 1000.0 / 60) + 0.5) + "分钟");
									return;
								}
							}
							InstanceData.playerEnter.get(instance).put(player.getName(), now);
							plugin.getPlayerData().set(instance + "." + player.getName(),
									plugin.getPlayerData().getInt(instance + "." + player.getName(), 0) + 1);
							plugin.savePlayerData();
							player.teleport(data.getInstancetp(instance));
							if (data.getInstanceTitle(instance) != null && data.getInstanceSubtitle(instance) != null) {
								player.sendTitle(data.getInstanceTitle(instance), data.getInstanceSubtitle(instance));
							}
						}
					};
				}
			}
			newGUI.setPleyerList(playerList);
			newGUI.setHandlerList(handler);
			Data.gui.add(newGUI);
		}

	}

	@SuppressWarnings("deprecation")
	private ItemStack getItemStack(String arg, int damage) {
		int itemNumber = -1;
		try {
			itemNumber = Integer.parseInt(arg);
		} catch (Exception e) {
		}
		ItemStack item = null;
		if (itemNumber != -1) {
			item = new ItemStack(itemNumber, 1, (byte) damage);
		} else {
			try {
				item = new ItemStack(Material.getMaterial(arg), 1, (byte) damage);
			} catch (Exception e) {
				item = null;
			}
		}
		return item;
	}

}
