package net.novauniverse.voidcraft;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.novauniverse.voidcraft.command.TransferLifeCommand;
import net.novauniverse.voidcraft.command.voidcraft.VoidcraftCommand;
import net.novauniverse.voidcraft.config.VoidCraftConfig;
import net.novauniverse.voidcraft.customitems.VoidCraftLootBox;
import net.novauniverse.voidcraft.modules.JNDIFix;
import net.novauniverse.voidcraft.modules.SessionManager;
import net.novauniverse.voidcraft.modules.SpectatorVoidProtection;
import net.novauniverse.voidcraft.modules.VoidCraftManager;
import net.novauniverse.voidcraft.playerdata.PlayerDataManager;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.command.CommandRegistry;
import net.zeeraa.novacore.spigot.module.ModuleManager;
import net.zeeraa.novacore.spigot.module.modules.customitems.CustomItemManager;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.NetherBoardScoreboard;

public class VoidCraft extends JavaPlugin {
	private static VoidCraft instance;
	private VoidCraftConfig voidCraftConfig;

	public static VoidCraft getInstance() {
		return instance;
	}

	public VoidCraftConfig getVoidCraftConfig() {
		return voidCraftConfig;
	}

	@Override
	public void onLoad() {
		VoidCraft.instance = this;
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		this.voidCraftConfig = new VoidCraftConfig(getConfig());

		ModuleManager.require(NetherBoardScoreboard.class);
		ModuleManager.require(CustomItemManager.class);

		int scoreboardLines = 2;
		NetherBoardScoreboard.getInstance().setDefaultTitle(ChatColor.AQUA + "" + ChatColor.BOLD + "VoidCraft Season 2");
		NetherBoardScoreboard.getInstance().setLineCount(scoreboardLines);

		try {
			CustomItemManager.getInstance().addCustomItem(VoidCraftLootBox.class);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			Log.fatal(getName(), "Failed to register custom items. " + e.getClass().getName() + " " + e.getMessage());
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		ModuleManager.loadModule(this, PlayerDataManager.class, true);
		ModuleManager.loadModule(this, SessionManager.class, true);
		ModuleManager.loadModule(this, SpectatorVoidProtection.class, true);
		ModuleManager.loadModule(this, VoidCraftManager.class, true);
		ModuleManager.loadModule(this, JNDIFix.class, true);

		CommandRegistry.registerCommand(new TransferLifeCommand());
		CommandRegistry.registerCommand(new VoidcraftCommand());
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll((Plugin) this);
		Bukkit.getScheduler().cancelTasks(this);
	}
}
