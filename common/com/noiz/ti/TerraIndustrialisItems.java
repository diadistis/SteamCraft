package com.noiz.ti;

import net.minecraft.item.Item;

import com.noiz.ti.items.ItemHatch;
import com.noiz.ti.items.ItemLeadPipe;
import com.noiz.ti.items.ItemPipeMold;

public class TerraIndustrialisItems {
	
	public static Item Hatch;
	public static Item LeadPipe;
	public static Item ClayMoldPipe;
	
	public static void Setup() {
		Hatch = new ItemHatch(5555).setUnlocalizedName("Hatch");
		ClayMoldPipe = new ItemPipeMold(5556).setMetaNames(new String[]{"Clay Mold Pipe","Ceramic Mold Pipe",
				"Ceramic Mold Pipe Lead"}).setUnlocalizedName("Pick Mold");
		LeadPipe = new ItemLeadPipe(5557).setUnlocalizedName("Lead Pipe");
	}
}
