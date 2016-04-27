package cc.isotopestudio.Raid.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;

import cc.isotopestudio.Raid.Raid;

public class UpdatePlayerData extends BukkitRunnable {
	private final Raid plugin;

	public UpdatePlayerData(Raid plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		Date todayDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String today = format.format(todayDate);
		String time = plugin.getPlayerData().getString("time");
		if (!(time != null && time.equals(today))) {
			plugin.resetPlayerData();
			plugin.getPlayerData().set("time", today);
			plugin.savePlayerData();
		}
	}

}
