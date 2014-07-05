package com.noiz.steamcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.noiz.steamcraft.SteamCraftConstants;
import com.noiz.steamcraft.entities.tiles.TEBoilerTank;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBoilerTank extends Block implements ITileEntityProvider {

	@SideOnly(Side.CLIENT)
	public static Icon icon;

	public BlockBoilerTank(Material material) {
		super(1235, material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TEBoilerTank();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		BlockBoilerTank.icon = icon.registerIcon(SteamCraftConstants.ModId
				.toLowerCase() + ":boiler_sides");
	};

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		return icon;
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int metadata) {

		// TEBoilerTank controller = (TEBoilerTank) world.getBlockTileEntity(x,
		// y,
		// z);

		return metadata;
	}
}
