package cc.isotopestudio.Raid.data;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import cc.isotopestudio.Raid.Raid;

public class FileUtil {
	private final Raid plugin;

	public FileUtil(Raid plugin) {
		this.plugin = plugin;
	}

	public Location getInstancePos1(int instance) {
		double x = getInstanceValue(instance, "pos1.x");
		double y = getInstanceValue(instance, "pos1.y");
		double z = getInstanceValue(instance, "pos1.z");
		Location loc = new Location(getInstanceWorld(instance), x, y, z);
		return loc;
	}

	public Location getInstancePos2(int instance) {
		double x = getInstanceValue(instance, "pos2.x");
		double y = getInstanceValue(instance, "pos2.y");
		double z = getInstanceValue(instance, "pos2.z");
		Location loc = new Location(getInstanceWorld(instance), x, y, z);
		return loc;
	}

	public String getInstanceName(int instance) {
		return plugin.getInstanceData().getString(instance + ".name");
	}

	public World getInstanceWorld(int instance) {
		World world = plugin.getServer().getWorld(plugin.getInstanceData().getString(instance + ".world"));
		return world;
	}

	private int getInstanceValue(int instance, String value) {
		return plugin.getInstanceData().getInt(instance + "." + value, -1);
	}

	public int getNumPlayers(int instance) {
		Location pos1 = getInstancePos1(instance);
		Location pos2 = getInstancePos1(instance);
		int num = 0;
		int[][] bound = new int[2][3];
		bound[0][0] = pos1.getBlockX();
		bound[0][1] = pos1.getBlockY();
		bound[0][2] = pos1.getBlockZ();
		bound[1][0] = pos2.getBlockX();
		bound[1][1] = pos2.getBlockY();
		bound[1][2] = pos2.getBlockZ();
		List<Player> players = getInstanceWorld(instance).getPlayers();
		for (Player player : players) {
			Location loc = player.getLocation();
			if (loc.getBlockX() <= bound[0][0] || loc.getBlockX() >= bound[1][0])
				continue;
			if (loc.getBlockY() <= bound[0][1] || loc.getBlockY() >= bound[1][1])
				continue;
			if (loc.getBlockZ() <= bound[0][2] || loc.getBlockZ() >= bound[1][2])
				continue;
			num++;
		}
		return num;
	}
}
