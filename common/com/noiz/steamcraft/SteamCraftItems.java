package com.noiz.steamcraft;

import com.noiz.steamcraft.items.ItemBoilerHatch;

import net.minecraft.item.Item;

public class SteamCraftItems {
	public static Item BoilerHatch;
	
	public static void Setup()
	{
		BoilerHatch = new ItemBoilerHatch(5555).setUnlocalizedName("Boiler Hatch");
	}
}
