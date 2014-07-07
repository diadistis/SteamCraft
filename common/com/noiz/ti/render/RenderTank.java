package com.noiz.ti.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

import com.noiz.ti.TerraIndustrialisBlocks;
import com.noiz.ti.entities.tiles.multiblock.TileEntityRectMultiblock;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderTank implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		if (modelId == TerraIndustrialisBlocks.blockTankRenderId) {
			renderer.setRenderBounds(1.0F / 16.0F, 0.0F, 1.0F / 16.0F, 1.0F - (1.0F / 16.0F), 1.0F, 1.0F - (1.0F / 16.0F));
			RenderingHelper.renderInvBlock(block, metadata, renderer);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (modelId == TerraIndustrialisBlocks.blockTankRenderId) {

			float pixelSize = 1.0F / 16.0F;
			float width = 1.0F - (pixelSize * 2);
			float length = 1.0F - (pixelSize * 2);
			float west = pixelSize;
			float north = pixelSize;

			TileEntityRectMultiblock tank = (TileEntityRectMultiblock) world.getBlockTileEntity(x, y, z);

			if (tank.multiblockInternalDirections.contains(ForgeDirection.EAST)) {
				width += pixelSize;
			}
			if (tank.multiblockInternalDirections.contains(ForgeDirection.WEST)) {
				west -= pixelSize;
				width += pixelSize;
			}
			if (tank.multiblockInternalDirections.contains(ForgeDirection.NORTH)) {
				north -= pixelSize;
				length += pixelSize;
			}
			if (tank.multiblockInternalDirections.contains(ForgeDirection.SOUTH)) {
				length += pixelSize;
			}

			renderer.setRenderBounds(west, 0.0F, north, west + width, 1.0F, north + length);
			renderer.renderStandardBlock(block, x, y, z);
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return 0;
	}
}
