package cc.isotopestudio.Raid.task;

import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;

import static cc.isotopestudio.Raid.Raid.plugin;

public class UpdatePlayerData extends BukkitRunnable {

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
