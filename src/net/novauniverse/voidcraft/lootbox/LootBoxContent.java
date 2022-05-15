package net.novauniverse.voidcraft.lootbox;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.novacore.spigot.utils.ItemBuilder;

public class LootBoxContent {
	private static final List<ItemStack> LOOTBOX_CONTENT = new ArrayList<>();

	public static List<ItemStack> getLootBoxContent() {
		return LOOTBOX_CONTENT;
	}

	static {
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.SAND).setAmount(64).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.DIAMOND).setAmount(5).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.OBSIDIAN).setAmount(2).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.CACTUS).setAmount(4).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.LAVA_BUCKET).setAmount(2).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.POTATO).setAmount(1).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.CARROT).setAmount(1).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.TNT).setAmount(2).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.SUGAR_CANE).setAmount(6).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.IRON_INGOT).setAmount(16).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.NETHER_WART).setAmount(2).build());
		LOOTBOX_CONTENT.add(new ItemBuilder(Material.BLAZE_ROD).setAmount(2).build());
	}
}