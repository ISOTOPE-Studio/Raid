package cc.isotopestudio.Raid.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
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
		Set<String> guiName = plugin.getGUIData().getKeys(false);
		Set<String> keys = plugin.getGUIData().getKeys(true);
		Iterator<String> it = guiName.iterator();
		Iterator<String> keyIt = keys.iterator();
		while (it.hasNext()) {
			int num = Integer.parseInt(it.next());
			int size = plugin.getGUIData().getInt(num + ".column") * 9;
			String name = plugin.getGUIData().getString(num + ".name");
			ClassGUI newGUI = new ClassGUI(name, size, plugin);
			ArrayList<String> itemIndexList = new ArrayList<String>();
			OptionClickEventHandler[] handler = new OptionClickEventHandler[size];
			while (keyIt.hasNext()) {
				String temp = keyIt.next();
				// System.out.println(temp);
				String[] tempKey = temp.split("\\.");
				if (tempKey.length == 3 && tempKey[1].equals("slot")) {
					// System.out.println(tempKey[0] + tempKey[1] + tempKey[2]);
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
						@Override
						public void onOptionClick(ClassGUI.OptionClickEvent event) {
							int limit = data.getInstanceLimit(instance);
							int num = data.getNumPlayers(instance);
							if (num < limit) {
								event.getPlayer().teleport(data.getInstancetp(instance));
								event.getPlayer().sendMessage(Raid.prefix + "成功传送到副本" + instance);
							} else {
								event.getPlayer().sendMessage(Raid.prefix + "副本人数达到限制" + instance);
							}
						}
					};
				}
			}
			newGUI.setPleyerList(playerList);
			newGUI.setHandlerList(handler);
			// System.out.println("Index" + num + " size" + size + " " +
			// itemIndexList.toString());
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
