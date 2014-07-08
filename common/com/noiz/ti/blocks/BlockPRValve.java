package com.noiz.ti.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

import com.noiz.ti.TerraIndustrialisBlocks;
import com.noiz.ti.TerraIndustrialisConstants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPRValve extends Block {

	@SideOnly(Side.CLIENT)
	public static Icon icon;
	@SideOnly(Side.CLIENT)
	public static Icon top;

	public BlockPRValve(Material material) {
		super(1237, material);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return TerraIndustrialisBlocks.blockPRVRenderId;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		BlockPRValve.icon = icon.registerIcon(TerraIndustrialisConstants.ModId.toLowerCase() + ":rpv");
		BlockPRValve.top = icon.registerIcon(TerraIndustrialisConstants.ModId.toLowerCase() + ":rpv_top");
	};

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		return side < 2 ? top : icon;
	}
}
