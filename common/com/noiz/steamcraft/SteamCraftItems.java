package com.noiz.steamcraft;

import net.minecraft.item.Item;

import com.noiz.steamcraft.items.ItemHatch;
import com.noiz.steamcraft.items.ItemLeadPipe;
import com.noiz.steamcraft.items.ItemPotteryMold;

public class SteamCraftItems {
	
	public static Item Hatch;
	public static Item LeadPipe;
	public static Item ClayMoldPipe;
	
	public static void Setup() {
		Hatch = new ItemHatch(5555).setUnlocalizedName("Hatch");
		ClayMoldPipe = new ItemPotteryMold(5556).setMetaNames(new String[]{"Clay Mold Pipe","Ceramic Mold Pipe",
				"Ceramic Mold Pipe Lead"}).setUnlocalizedName("Pick Mold");
		LeadPipe = new ItemLeadPipe(5557).setUnlocalizedName("Lead Pipe");
	}
}
