package com.noiz.steamcraft.blocks;

import com.noiz.steamcraft.entities.tiles.TEBoiler;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTest extends Block implements ITileEntityProvider {

	public BlockTest(Material material) {
		super(1234, material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TEBoiler();
	}
}
