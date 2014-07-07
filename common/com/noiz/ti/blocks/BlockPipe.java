package com.noiz.ti.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.noiz.ti.TerraIndustrialisBlocks;
import com.noiz.ti.TerraIndustrialisConstants;
import com.noiz.ti.entities.tiles.TileEntityPipe;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPipe extends Block implements ITileEntityProvider {
	
	@SideOnly(Side.CLIENT)
	public static Icon icon = null;

	public BlockPipe(Material material) {
		super(1236, material);
		setBlockBounds(0.4F, 0.4F, 0.4F, 0.6F, 0.6F, 0.6F);
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityPipe();
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		if (!world.isRemote) {
			TileEntityPipe te = (TileEntityPipe)world.getBlockTileEntity(x, y, z);
			if (te != null) {
				te.updateConnections();
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return TerraIndustrialisBlocks.blockPipeRenderId;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegistry) {
		icon = iconRegistry.registerIcon(TerraIndustrialisConstants.ModId.toLowerCase() + ":lead_pipe");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2) {
		return icon;
	}
}
