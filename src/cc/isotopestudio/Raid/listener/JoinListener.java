package cc.isotopestudio.Raid.listener;

import cc.isotopestudio.Raid.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) { // Add Permission
        Player player = event.getPlayer();
        PlayerData.teleportToLocation(player);
    }
}
