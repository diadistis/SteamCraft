package com.noiz.steamcraft;

import com.noiz.steamcraft.items.ItemHatch;

import net.minecraft.item.Item;

public class SteamCraftItems {
	public static Item Hatch;

	public static void Setup() {
		Hatch = new ItemHatch(5555).setUnlocalizedName("Hatch");
	}
}
