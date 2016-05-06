package cc.isotopestudio.Raid.data;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import cc.isotopestudio.Raid.Raid;

public class PlayerData {
	private final Raid plugin;

	public PlayerData(Raid plugin) {
		this.plugin = plugin;
	}

	public void storePlayerLocation(Player player) {
		plugin.getPlayerData().set(player.getName() + ".location.X", player.getLocation().getBlockX());
		plugin.getPlayerData().set(player.getName() + ".location.Y", player.getLocation().getBlockY());
		plugin.getPlayerData().set(player.getName() + ".location.Z", player.getLocation().getBlockZ());
		plugin.getPlayerData().set(player.getName() + ".location.world", player.getLocation().getWorld().getName());
		plugin.savePlayerData();
	}

	@SuppressWarnings("deprecation")
	public void teleportToLocation(Player player) {
		int x = plugin.getPlayerData().getInt(player.getName() + ".location.X", -1);
		int y = plugin.getPlayerData().getInt(player.getName() + ".location.Y", -1);
		int z = plugin.getPlayerData().getInt(player.getName() + ".location.Z", -1);
		if (x == -1 && y == -1 && z == -1) {
			return;
		}
		World world = Bukkit.getWorld(plugin.getPlayerData().getString(player.getName() + ".location.world"));
		player.teleport(new Location(world, x, y, z));
		player.sendTitle("§c时间到", "§a传送到原始位置");
		plugin.getPlayerData().set(player.getName() + ".location", null);
		plugin.savePlayerData();
	}
}
