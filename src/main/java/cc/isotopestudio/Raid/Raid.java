package cc.isotopestudio.Raid;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import cc.isotopestudio.Raid.command.CommandRaid;
import cc.isotopestudio.Raid.command.CommandRaidadmin;
import cc.isotopestudio.Raid.task.UpdateGUI;

public class Raid extends JavaPlugin {

	public static final String prefix = (new StringBuilder()).append(ChatColor.RED).append("[副本系统]")
			.append(ChatColor.GREEN).toString();

	public void createFile(String name) {

		File file;
		file = new File(getDataFolder(), name + ".yml");
		if (!file.exists()) {
			saveDefaultConfig();
		}
	}

	String FileVersion = "1";

	@Override
	public void onEnable() {
		getLogger().info("加载配置文件中");

		createFile("config");

		try {
			getGUIData().save(GUIDataFile);
		} catch (IOException e) {
			getLogger().info("GUI文件出错！");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		try {
			getInstanceData().save(instanceDataFile);
		} catch (IOException e) {
			getLogger().info("副本文件出错！");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		this.getCommand("raid").setExecutor(new CommandRaid());
		this.getCommand("raidadmin").setExecutor(new CommandRaidadmin(this));
		int freq = getConfig().getInt("update", 20);
		BukkitTask task = new UpdateGUI(this).runTaskTimer(this, 20, freq);

		getLogger().info("Raid 成功加载!");
		getLogger().info("Raid 由ISOTOPE Studio制作!");
		getLogger().info("http://isotopestudio.cc");
	}

	public void onReload() {
		reloadGUIData();
		reloadInstanceData();
		BukkitTask task = new UpdateGUI(this).runTaskTimer(this, 20, 20);
	}

	@Override
	public void onDisable() {
		getLogger().info("Raid 成功卸载!");
	}

	private File GUIDataFile = null;
	private FileConfiguration GUIData = null;

	public void reloadGUIData() {
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

	public void saveGUIData() {
		if (GUIData == null || GUIDataFile == null) {
			return;
		}
		try {
			getGUIData().save(GUIDataFile);
		} catch (IOException ex) {
			getLogger().info("GUI文件保存失败！");
		}
	}

	private File instanceDataFile = null;
	private FileConfiguration instanceData = null;

	public void reloadInstanceData() {
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
			getLogger().info("副本文件保存失败！");
		}
	}

}
