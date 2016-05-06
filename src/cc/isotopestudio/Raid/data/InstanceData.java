package cc.isotopestudio.Raid.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import cc.isotopestudio.Raid.Raid;

public class InstanceData {
	private final Raid plugin;
	public static HashMap<Integer, HashMap<String, Long>> playerEnter = new HashMap<Integer, HashMap<String, Long>>();

	public InstanceData(Raid plugin) {
		this.plugin = plugin;
	}

	public void setPos1(int instance, Location loc) {
		plugin.getInstanceData().set(instance + ".pos1.x", loc.getBlockX());
		plugin.getInstanceData().set(instance + ".pos1.y", loc.getBlockY());
		plugin.getInstanceData().set(instance + ".pos1.z", loc.getBlockZ());
		plugin.getInstanceData().set(instance + ".world", loc.getWorld().getName());
		if (getTitle(instance) == null) {
			plugin.getInstanceData().set(instance + ".title", "null");
		}
		if (getSubtitle(instance) == null) {
			plugin.getInstanceData().set(instance + ".subtitle", "null");
		}
		if (getLvLimit(instance) < 0) {
			plugin.getInstanceData().set(instance + ".level", -1);
		}
		if (getEnterInterval(instance) < 0) {
			plugin.getInstanceData().set(instance + ".interval", -1);
		}
		if (getDayLimit(instance) < 0) {
			plugin.getInstanceData().set(instance + ".daylimit", -1);
		}
		if (getTimeLimit(instance) == 10) {
			plugin.getInstanceData().set(instance + ".time", 10);
		}
		plugin.saveInstanceData();
	}

	public Location getPos1(int instance) {
		double x = getValue(instance, "pos1.x");
		double y = getValue(instance, "pos1.y");
		double z = getValue(instance, "pos1.z");
		Location loc = new Location(getWorld(instance), x, y, z);
		return loc;
	}

	public void setPos2(int instance, Location loc) {
		plugin.getInstanceData().set(instance + ".pos2.x", loc.getBlockX());
		plugin.getInstanceData().set(instance + ".pos2.y", loc.getBlockY());
		plugin.getInstanceData().set(instance + ".pos2.z", loc.getBlockZ());
		plugin.getInstanceData().set(instance + ".world", loc.getWorld().getName());
		plugin.saveInstanceData();
	}

	public Location getPos2(int instance) {
		double x = getValue(instance, "pos2.x");
		double y = getValue(instance, "pos2.y");
		double z = getValue(instance, "pos2.z");
		Location loc = new Location(getWorld(instance), x, y, z);
		return loc;
	}

	public void settp(int instance, Location loc) {
		plugin.getInstanceData().set(instance + ".tp.x", loc.getBlockX());
		plugin.getInstanceData().set(instance + ".tp.y", loc.getBlockY());
		plugin.getInstanceData().set(instance + ".tp.z", loc.getBlockZ());
		plugin.getInstanceData().set(instance + ".tp.yaw", loc.getYaw());
		plugin.saveInstanceData();
	}

	public Location gettp(int instance) {
		double x = getValue(instance, "tp.x");
		double y = getValue(instance, "tp.y");
		double z = getValue(instance, "tp.z");
		float yaw = (float) plugin.getInstanceData().getDouble(instance + ".tp.yaw");
		Location loc = new Location(getWorld(instance), x + 0.5, y, z + 0.5, yaw, 0);
		return loc;
	}

	public String getName(int instance) {
		return plugin.getInstanceData().getString(instance + ".name");
	}

	public int getLimit(int instance) {
		return plugin.getInstanceData().getInt(instance + ".limit", Integer.MAX_VALUE);
	}

	public void setLimit(int instance, int limit) {
		plugin.getInstanceData().set(instance + ".limit", limit);
		plugin.saveInstanceData();
	}

	public void setTimeLimit(int instance, int minute) {
		plugin.getInstanceData().set(instance + ".time", minute);
		plugin.saveInstanceData();
	}

	public int getTimeLimit(int instance) {
		return plugin.getInstanceData().getInt(instance + ".time", 10);
	}

	public void setStartTime(int instance) {
		plugin.getInstanceData().set(instance + ".start", new Date().getTime());
		plugin.saveInstanceData();
	}

	public void resetStartTime(int instance) {
		plugin.getInstanceData().set(instance + ".start", -1);
		plugin.saveInstanceData();
	}

	public int getRemainTime(int instance) {
		long start = plugin.getInstanceData().getLong(instance + ".start");
		if (start == -1) {
			return -1;
		}
		long now = new Date().getTime();
		int sec = (int) (start / 1000 - now / 1000) + getTimeLimit(instance) * 60;
		return sec;
	}

	public String getTitle(int instance) {
		String title = plugin.getInstanceData().getString(instance + ".title", "null");
		if (title.equals("null")) {
			return null;
		}
		return title;
	}

	public String getSubtitle(int instance) {
		String subtitle = plugin.getInstanceData().getString(instance + ".subtitle", "null");
		if (subtitle.equals("null")) {
			return null;
		}
		return subtitle;
	}

	public int getLvLimit(int instance) {
		return plugin.getInstanceData().getInt(instance + ".level", -1);
	}

	public int getEnterInterval(int instance) {
		return plugin.getInstanceData().getInt(instance + ".interval", -1);
	}

	public int getDayLimit(int instance) {
		return plugin.getInstanceData().getInt(instance + ".daylimit", -1);
	}

	public World getWorld(int instance) {
		World world = plugin.getServer().getWorld(plugin.getInstanceData().getString(instance + ".world"));
		return world;
	}

	private int getValue(int instance, String value) {
		return plugin.getInstanceData().getInt(instance + "." + value, -1);
	}

	public int getNumPlayers(int instance) {
		Location pos1 = getPos1(instance);
		Location pos2 = getPos2(instance);
		int num = 0;
		int[][] bound = new int[2][3];
		if (pos1.getBlockX() < pos2.getBlockX()) {
			bound[0][0] = pos1.getBlockX();
			bound[1][0] = pos2.getBlockX();
		} else {
			bound[1][0] = pos1.getBlockX();
			bound[0][0] = pos2.getBlockX();
		}
		if (pos1.getBlockY() < pos2.getBlockY()) {
			bound[0][1] = pos1.getBlockY();
			bound[1][1] = pos2.getBlockY();
		} else {
			bound[1][1] = pos1.getBlockY();
			bound[0][1] = pos2.getBlockY();
		}
		if (pos1.getBlockZ() < pos2.getBlockZ()) {
			bound[0][2] = pos1.getBlockZ();
			bound[1][2] = pos2.getBlockZ();
		} else {
			bound[1][2] = pos1.getBlockZ();
			bound[0][2] = pos2.getBlockZ();
		}
		List<Player> players = getWorld(instance).getPlayers();
		for (Player player : players) {
			Location loc = player.getLocation();
			// System.out.println(loc.getBlockX() + " " + bound[0][0] + " " +
			// loc.getBlockX() + " " + bound[1][0]);
			if (loc.getBlockX() < bound[0][0] || loc.getBlockX() > bound[1][0])
				continue;
			// System.out.println(loc.getBlockY() + " " + bound[0][1] + " " +
			// loc.getBlockY() + " " + bound[1][1]);
			if (loc.getBlockY() < bound[0][1] || loc.getBlockY() > bound[1][1])
				continue;
			// System.out.println(loc.getBlockZ() + " " + bound[0][2] + " " +
			// loc.getBlockZ() + " " + bound[1][2]);
			if (loc.getBlockZ() < bound[0][2] || loc.getBlockZ() > bound[1][2])
				continue;
			num++;
		}
		return num;
	}
}
