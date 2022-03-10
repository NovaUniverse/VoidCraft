package net.novauniverse.voidcraft.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.module.NovaModule;

public class JNDIFix extends NovaModule implements Listener {
	@Override
	public String getName() {
		return "devilsmp.JNDIFix";
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		if (e.getMessage().contains("${")) {
			e.setCancelled(true);
			e.getPlayer().kickPlayer("Illegal message in chat");
			Log.warn("JNDIFix", e.getPlayer().getDisplayName() + " sent a message that could be potentially harmful. This does not mean that you should ban the player but they have been disconnected to keep the server safe");
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().contains("${")) {
			e.setCancelled(true);
			e.getPlayer().kickPlayer("Illegal message in chat");
			Log.warn("JNDIFix", e.getPlayer().getDisplayName() + " sent a command that could be potentially harmful. This does not mean that you should ban the player but they have been disconnected to keep the server safe");
		}
	}
}
