package com.noiz.steamcraft.items;

import com.noiz.steamcraft.SteamCraftConstants;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import TFC.Items.Pottery.ItemPotteryBase;

public class ItemPotteryMold extends ItemPotteryBase 
{
	Icon leadIcon;
	
	public ItemPotteryMold(int id) {
		super(id);
	}
	
	@Override
	public void registerIcons(IconRegister registerer) {
		ClayIcon = registerer.registerIcon(SteamCraftConstants.ModId + ":" + "clay-mold-pipe");
		CeramicIcon = registerer.registerIcon(SteamCraftConstants.ModId + ":" + "ceramic-mold-pipe");
		leadIcon = registerer.registerIcon(SteamCraftConstants.ModId + ":" + "lead-mold-pipe");
	}
	
	@Override
	public Icon getIconFromDamage(int damage) {
		
		if (damage == 0)
			return ClayIcon;
		else if (damage == 1)
			return CeramicIcon;
		else if (damage == 2)
			return leadIcon;
		
		return this.ClayIcon; 
	}
}
