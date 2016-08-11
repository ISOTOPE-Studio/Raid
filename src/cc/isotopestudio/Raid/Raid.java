package cc.isotopestudio.Raid;

import cc.isotopestudio.Raid.command.CommandRaid;
import cc.isotopestudio.Raid.command.CommandRaidadmin;
import cc.isotopestudio.Raid.listener.JoinListener;
import cc.isotopestudio.Raid.task.UpdateGUI;
import cc.isotopestudio.Raid.task.UpdatePlayerData;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Raid extends JavaPlugin {

    public static final String prefix = (new StringBuilder()).append(ChatColor.RED).append("[����ϵͳ]")
            .append(ChatColor.GREEN).toString();

    private void createFile() {

        File file;
        file = new File(getDataFolder(), "config" + ".yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }
    }

    String FileVersion = "1";

    @Override
    public void onEnable() {
        getLogger().info("���������ļ���");

        createFile();

        try {
            getGUIData().save(GUIDataFile);
        } catch (IOException e) {
            getLogger().info("GUI�ļ�����");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            getInstanceData().save(instanceDataFile);
        } catch (IOException e) {
            getLogger().info("�����ļ�����");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            getPlayerData().save(playerDataFile);
        } catch (IOException e) {
            getLogger().info("����ļ�����");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new JoinListener(this), this);

        this.getCommand("raid").setExecutor(new CommandRaid());
        this.getCommand("raidadmin").setExecutor(new CommandRaidadmin(this));

        int freq = getConfig().getInt("update", 20);
        new UpdateGUI(this).runTaskTimer(this, 20, freq);
        new UpdatePlayerData(this).runTaskTimer(this, 100, 20 * 60 * 60);

        getLogger().info("Raid �ɹ�����!");
        getLogger().info("Raid ��ISOTOPE Studio����!");
        getLogger().info("http://isotopestudio.cc");
    }

    public void onReload() {
        reloadGUIData();
        reloadInstanceData();
        reloadPlayerData();
        //new UpdateGUI(this).runTaskTimer(this, 20, freq);
        getLogger().info("Raid �ɹ�����!");
        getLogger().info("Raid ��ISOTOPE Studio����!");
        getLogger().info("http://isotopestudio.cc");
    }

    @Override
    public void onDisable() {
        getLogger().info("Raid �ɹ�ж��!");
    }

    private File GUIDataFile = null;
    private FileConfiguration GUIData = null;

    private void reloadGUIData() {
        if (GUIDataFile == null) {
            this.saveResource("GUI.yml", false);
        }
        GUIDataFile = new File(getDataFolder(), "GUI.yml");
        GUIData = YamlConfiguration.loadConfiguration(GUIDataFile);
    }

    public FileConfiguration getGUIData() {
        if (GUIData == null) {
            reloadGUIData();
        }
        return GUIData;
    }

    private File instanceDataFile = null;
    private FileConfiguration instanceData = null;

    private void reloadInstanceData() {
        if (instanceDataFile == null) {
            instanceDataFile = new File(getDataFolder(), "instanceData.yml");
        }
        instanceData = YamlConfiguration.loadConfiguration(instanceDataFile);
    }

    public FileConfiguration getInstanceData() {
        if (instanceData == null) {
            reloadInstanceData();
        }
        return instanceData;
    }

    public void saveInstanceData() {
        if (instanceData == null || instanceDataFile == null) {
            return;
        }
        try {
            getInstanceData().save(instanceDataFile);
        } catch (IOException ex) {
            getLogger().info("�����ļ�����ʧ�ܣ�");
        }
    }

    private File playerDataFile = null;
    private FileConfiguration playerData = null;

    private void reloadPlayerData() {
        if (playerDataFile == null) {
            playerDataFile = new File(getDataFolder(), "playerData.yml");
        }
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    public FileConfiguration getPlayerData() {
        if (playerData == null) {
            reloadPlayerData();
        }
        return playerData;
    }

    public void savePlayerData() {
        if (playerData == null || playerDataFile == null) {
            return;
        }
        try {
            getPlayerData().save(playerDataFile);
        } catch (IOException ex) {
            getLogger().info("����ļ�����ʧ�ܣ�");
        }
    }

    public void resetPlayerData() {
        for (String key : getPlayerData().getKeys(false)) {
            getPlayerData().set(key, null);
        }
        savePlayerData();
    }

}
