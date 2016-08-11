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
        } catch (Exception ignored) {
        }
        Data.gui = new ArrayList<ClassGUI>();
        Data.guiCmd = new ArrayList<String>();
        Set<String> guiName = plugin.getGUIData().getKeys(false);
        Set<String> keys = plugin.getGUIData().getKeys(true);
        Iterator<String> it = guiName.iterator();
        Iterator<String> keyIt = keys.iterator();
        final InstanceData data = new InstanceData(plugin);
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
                            int limit = data.getLimit(instance);
                            int num = data.getNumPlayers(instance);
                            Player player = event.getPlayer();
                            if (!player.hasPermission("raid.tp." + instance)) {
                                player.sendMessage(Raid.prefix + "你没有权限");
                                return;
                            }
                            if (player.getLevel() < data.getLvLimit(instance)) {
                                player.sendTitle("§c无法进入", "§a你的等级不够，需要" + data.getLvLimit(instance) + "级");
                                return;
                            }
                            if (num >= limit) {
                                player.sendMessage(Raid.prefix + "副本人数达到限制");
                                return;
                            }
                            if (InstanceData.playerEnter.get(instance) == null) {
                                InstanceData.playerEnter.put(instance, new HashMap<String, Long>());
                            }
                            if (data.getDayLimit(instance) != -1
                                    && plugin.getPlayerData().getInt(player.getName() + ".instance." + instance,
                                    -100) >= data.getDayLimit(instance)) {
                                player.sendTitle("§c无法进入", "§a今天已达到进入此副本的限制" + data.getDayLimit(instance));
                                return;
                            }
                            long now = new Date().getTime();
                            if (InstanceData.playerEnter.get(instance).get(player.getName()) != null) {
                                long last = InstanceData.playerEnter.get(instance).get(player.getName());
                                int interval = data.getEnterInterval(instance) * 60 * 1000;
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
                            player.teleport(data.gettp(instance));
                            InstanceData idata = new InstanceData(plugin);
                            if (idata.getRemainTime(instance) < 0) {
                                idata.setStartTime(instance);
                            }
                            new PlayerData(plugin).storePlayerLocation(player);
                            if (data.getTitle(instance) != null && data.getSubtitle(instance) != null) {
                                player.sendTitle(data.getTitle(instance), data.getSubtitle(instance));
                            }
                        }
                    };
                }
            }
            newGUI.setPleyerList(playerList);
            newGUI.setHandlerList(handler);
            Data.gui.add(newGUI);
            if (data.getNumPlayers(num) < 1)
                data.resetStartTime(num);
        }

        Set<String> instances = plugin.getInstanceData().getKeys(false);
        Iterator<String> iit = instances.iterator();
        PlayerData pdata = new PlayerData(plugin);
        while (iit.hasNext()) {
            int instance = Integer.parseInt(iit.next());
            if (data.getRemainTime(instance) < 0) {
                data.resetStartTime(instance);
                for (Player player : data.getWorld(instance).getPlayers()) {
                    pdata.teleportToLocation(player);
                }
            }
        }
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
