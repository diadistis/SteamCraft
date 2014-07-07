package com.noiz.ti.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

import com.noiz.ti.TerraIndustrialisConstants;

public class ItemHatch extends Item {

	Icon icon;

	public ItemHatch(int i) {
		super(i);
	}

	@Override
	public Icon getIconFromDamage(int par1) {
		return icon;
	}

	@Override
	public void registerIcons(IconRegister registerer) {
		icon = registerer.registerIcon(TerraIndustrialisConstants.ModId + ":"
				+ "hatch");
	}
}
