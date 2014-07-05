package com.noiz.steamcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.noiz.steamcraft.SteamCraftConstants;
import com.noiz.steamcraft.entities.tiles.TileEntityTank;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSteelTank extends Block implements ITileEntityProvider {

	@SideOnly(Side.CLIENT)
	public static Icon icon;

	public BlockSteelTank(Material material) {
		super(1235, material);
		setBlockBounds(1.0F / 16.0F, 0.0F, 1.0F / 16.0F, 1.0F - (1.0F / 16.0F),
				1.0F, 1.0F - (1.0F / 16.0F));
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityTank();
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
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		BlockSteelTank.icon = icon.registerIcon(SteamCraftConstants.ModId
				.toLowerCase() + ":heater_sides");
	};

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		return icon;
	}
}
