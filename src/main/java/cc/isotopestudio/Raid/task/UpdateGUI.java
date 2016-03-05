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
import cc.isotopestudio.Raid.gui.ClassGUI;

public class UpdateGUI /* extends BukkitRunnable */ {
	private final Raid plugin;

	public UpdateGUI(Raid plugin) {
		this.plugin = plugin;
	}

	// @Override
	public void run() {
		Data.gui = new ArrayList<ClassGUI>();
		Set<String> guiName = plugin.getGUIData().getKeys(false);
		Set<String> keys = plugin.getGUIData().getKeys(true);
		Iterator<String> it = guiName.iterator();
		Iterator<String> keyIt = keys.iterator();
		while (it.hasNext()) {
			System.out.println(2);
			int num = Integer.parseInt(it.next());
			int size = plugin.getGUIData().getInt(num + ".column") * 9;
			String name = plugin.getGUIData().getString(num + ".name");
			ClassGUI newGUI = new ClassGUI(name, size, null, plugin);
			ArrayList<String> itemIndexList = new ArrayList<String>();
			ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
			while (keyIt.hasNext()) {
				String temp = keyIt.next();
				System.out.println(temp);
				String[] tempKey = temp.split("\\.");
				if (tempKey.length == 3 && tempKey[1].equals("slot")) {
					System.out.println(tempKey[0] + tempKey[1] + tempKey[2]);
					itemIndexList.add(tempKey[2]);
				}
				if (temp.equals((num + 1) + "")) {
					break;
				}
			}
			for (String itemIndex : itemIndexList) {
				String itemName = plugin.getGUIData().getString(num + ".slot." + itemIndex + ".name");
				List<String> loreList = plugin.getGUIData().getStringList(num + ".slot." + itemIndex + ".lore");
				String[] lore = new String[loreList.size()];
				for (int i = 0; i < loreList.size(); i++) {
					lore[i] = loreList.get(i);
				}
				int pos = plugin.getGUIData().getInt(num + ".slot." + itemIndex + ".pos");
				int damage = plugin.getGUIData().getInt(num + ".slot." + itemIndex + ".damage", 0);
				String icon = plugin.getGUIData().getString(num + ".slot." + itemIndex + ".name");
				ItemStack item = getItemStack(plugin.getGUIData().getString(num + ".slot." + itemIndex + ".item"),
						damage);
				newGUI.setOption(pos, item, itemName, lore);
			}
			System.out.println("Index" + num + " size" + size + " " + itemIndexList.toString());
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
