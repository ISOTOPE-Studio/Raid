package cc.isotopestudio.Raid.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import cc.isotopestudio.Raid.Raid;
import cc.isotopestudio.Raid.data.PlayerData;

public class JoinListener implements Listener {

	private final Raid plugin;

	public JoinListener(Raid plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) { // Add Permission
		Player player = event.getPlayer();
		new PlayerData(plugin).teleportToLocation(player);
	}
}
