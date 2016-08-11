package cc.isotopestudio.Raid.gui;

import cc.isotopestudio.Raid.Raid;
import cc.isotopestudio.Raid.data.InstanceData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassGUI implements Listener {

    // From: https://bukkit.org/threads/icon-menu.108342

    private final String name;
    private final int size;
    private OptionClickEventHandler[] handler;
    private Raid plugin;
    private String[] optionNames;
    private ItemStack[] optionIcons;
    private final boolean willDestory;
    private ArrayList<Integer> playerList;

    public ClassGUI(String name, int size, Raid plugin) {
        this.name = name;
        this.size = size;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        willDestory = false;
    }

    public ClassGUI setOption(int position, ItemStack icon, String name, String... info) {
        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
        return this;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(player, size, name);
        updateLore();
        for (int i = 0; i < optionIcons.length; i++) {
            if (optionIcons[i] != null) {
                inventory.setItem(i, optionIcons[i]);
            }
        }
        player.openInventory(inventory);
    }

    public void Destory() {
        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
        optionNames = null;
        optionIcons = null;
    }

    public void setPleyerList(ArrayList<Integer> list) {
        this.playerList = list;
    }

    public void setHandlerList(OptionClickEventHandler[] handler) {
        this.handler = handler;
    }

    private void updateLore() {
        for (int i : playerList) {
            ItemMeta meta = optionIcons[i].getItemMeta();
            List<String> lore = meta.getLore();
            for (int line = 0; line < lore.size(); line++) {
                String temp = lore.get(line);
                if (temp.contains("{") && temp.contains("}")) {
                    int start = -1, end = -1;
                    for (int pos = 0; pos < temp.length(); pos++) {
                        if (temp.charAt(pos) == '{') {
                            start = pos + 1;
                        }
                        if (temp.charAt(pos) == '}') {
                            end = pos;
                        }
                    }
                    String substring = temp.substring(start, end);
                    int instance;
                    if (substring.contains("|time")) {
                        end = substring.indexOf("|");
                        try {
                            instance = Integer.parseInt(substring.substring(0, end));
                        } catch (Exception e) {
                            continue;
                        }
                        InstanceData data = new InstanceData(plugin);

                        temp = temp.replace("{" + substring + "}",
                                "" + (data.getRemainTime(instance) < 0 ? "Î´¿ªÊ¼" : data.getRemainTime(instance) + "Ãë"));
                    } else {
                        try {
                            instance = Integer.parseInt(substring);
                        } catch (Exception e) {
                            continue;
                        }
                        InstanceData data = new InstanceData(plugin);
                        temp = temp.replace("{" + substring + "}", "" + data.getNumPlayers(instance));
                    }
                    lore.set(line, temp);
                }
            }
            meta.setLore(lore);
            optionIcons[i].setItemMeta(meta);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name)) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot < 0 || slot >= size) {
                return;
            }
            if (handler[slot] != null && optionNames[slot] != null) {
                Plugin plugin = this.plugin;
                OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot]);
                handler[slot].onOptionClick(e);
                if (e.willClose()) {
                    final Player p = (Player) event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            p.closeInventory();
                        }
                    }, 1);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equals(name)) {
            if (willDestory) {
                Destory();
            }
        }
    }

    public interface OptionClickEventHandler {
        void onOptionClick(OptionClickEvent event);
    }

    public class OptionClickEvent {
        private final Player player;
        private int position;
        private String name;
        private boolean close;
        private boolean Destory;

        public OptionClickEvent(Player player, int position, String name) {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.Destory = false;
        }

        public OptionClickEvent(Player player) {
            this.player = player;
        }

        public Player getPlayer() {
            return player;
        }

        public int getPosition() {
            return position;
        }

        public String getName() {
            return name;
        }

        public boolean willClose() {
            return close;
        }

        public boolean willDestroy() {
            return Destory;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public void setWillDestroy(boolean Destory) {
            this.Destory = Destory;
        }
    }

    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }
}