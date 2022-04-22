package net.novauniverse.voidcraft.command.voidcraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import net.novauniverse.voidcraft.VoidCraft;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaCommand;

// Aleksa requested this
public class Nothing extends NovaCommand {
	public Nothing() {
		super("friday", VoidCraft.getInstance());
		setAllowedSenders(AllowedSenders.PLAYERS);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.RED + "Lasagna");
		return true;
	}
}