package cc.isotopestudio.Raid.task;

import cc.isotopestudio.Raid.Raid;
import cc.isotopestudio.Raid.data.Data;
import cc.isotopestudio.Raid.data.InstanceData;
import cc.isotopestudio.Raid.data.PlayerData;
import cc.isotopestudio.Raid.gui.ClassGUI;
import cc.isotopestudio.Raid.gui.ClassGUI.OptionClickEventHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static cc.isotopestudio.Raid.Raid.plugin;

public class UpdateGUI extends BukkitRunnable {

    @Override
    public void run() {
        try {
            for (int i = 0; i < Data.gui.size(); i++) {
                Data.gui.get(i).Destory();
            }
        } catch (Exception ignored) {
        }
        Data.gui = new ArrayList<>();
        Data.guiCmd = new ArrayList<>();
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
            ArrayList<String> itemIndexList = new ArrayList<>();
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
            ArrayList<Integer> playerList = new ArrayList<>();
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
                            int limit = InstanceData.getLimit(instance);
                            int num = InstanceData.getNumPlayers(instance);
                            Player player = event.getPlayer();
                            if (!player.hasPermission("raid.tp." + instance)) {
                                player.sendMessage(Raid.prefix + "你没有权限");
                                return;
                            }
                            if (player.getLevel() < InstanceData.getLvLimit(instance)) {
                                player.sendTitle("§c无法进入", "§a你的等级不够，需要" + InstanceData.getLvLimit(instance) + "级");
                                return;
                            }
                            if (num >= limit) {
                                player.sendMessage(Raid.prefix + "副本人数达到限制");
                                return;
                            }
                            InstanceData.playerEnter.putIfAbsent(instance, new HashMap<>());
                            if (InstanceData.getDayLimit(instance) != -1
                                    && plugin.getPlayerData().getInt(player.getName() + ".instance." + instance,
                                    -100) >= InstanceData.getDayLimit(instance)) {
                                player.sendTitle("§c无法进入", "§a今天已达到进入此副本的限制" + InstanceData.getDayLimit(instance));
                                return;
                            }
                            long now = new Date().getTime();
                            if (InstanceData.playerEnter.get(instance).get(player.getName()) != null) {
                                long last = InstanceData.playerEnter.get(instance).get(player.getName());
                                int interval = InstanceData.getEnterInterval(instance) * 60 * 1000;
                                if (last + interval > now) {
                                    player.sendTitle("§c无法进入",
                                            "§a你还需要等待" + (int) (((last + interval - now) / 1000.0 / 60) + 0.5) + "分钟");
                                    return;
                                }
                            }
                            InstanceData.playerEnter.get(instance).put(player.getName(), now);
                            plugin.getPlayerData().set(player.getName() + ".instance." + instance,
                                    plugin.getPlayerData().getInt(player.getName() + ".instance." + instance, 0) + 1);
                            plugin.savePlayerData();
                            player.teleport(InstanceData.gettp(instance));
                            if (InstanceData.getRemainTime(instance) < 0) {
                                InstanceData.setStartTime(instance);
                            }
                            PlayerData.storePlayerLocation(player);
                            if (InstanceData.getTitle(instance) != null && InstanceData.getSubtitle(instance) != null) {
                                player.sendTitle(InstanceData.getTitle(instance), InstanceData.getSubtitle(instance));
                            }
                        }
                    };
                }
            }
            newGUI.setPleyerList(playerList);
            newGUI.setHandlerList(handler);
            Data.gui.add(newGUI);
        }
        /*
        Set<String> instances = plugin.getInstanceData().getKeys(false);
        for (String instance1 : instances) {
            int instance = Integer.parseInt(instance1);
            if (InstanceData.getRemainTime(instance) < 0) {
                InstanceData.resetStartTime(instance);
                for (Player player : InstanceData.getWorld(instance).getPlayers()) {
                    PlayerData.teleportToLocation(player);
                }
            }
        }
        */
    }

    @SuppressWarnings("deprecation")
    private ItemStack getItemStack(String arg, int damage) {
        int itemNumber = -1;
        try {
            itemNumber = Integer.parseInt(arg);
        } catch (Exception ignored) {
        }
        ItemStack item;
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
