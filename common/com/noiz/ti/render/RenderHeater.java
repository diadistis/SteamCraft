package com.noiz.ti.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

import com.noiz.ti.TerraIndustrialisBlocks;
import com.noiz.ti.entities.tiles.TileEntityHeater;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderHeater implements ISimpleBlockRenderingHandler {

	public static boolean renderHeater(IBlockAccess world, Block block, int i, int j, int k, RenderBlocks renderblocks) {
		TileEntityHeater heater = (TileEntityHeater) world.getBlockTileEntity(i, j, k);

		float minX = 1.0F / 8.0F;
		float maxX = 1.0F - (1.0F / 8.0F);
		float minZ = 1.0F / 8.0F;
		float maxZ = 1.0F - (1.0F / 8.0F);

		if (heater.multiblockInternalDirections.contains(ForgeDirection.EAST)) {
			maxX = 1;
		}
		if (heater.multiblockInternalDirections.contains(ForgeDirection.WEST)) {
			minX = 0;
		}
		if (heater.multiblockInternalDirections.contains(ForgeDirection.NORTH)) {
			minZ = 0;
		}
		if (heater.multiblockInternalDirections.contains(ForgeDirection.SOUTH)) {
			maxZ = 1;
		}

		renderblocks.setRenderBounds(minX, 0.0F, minZ, maxX, 1.0F, maxZ);
		renderblocks.renderStandardBlock(block, i, j, k);

		renderblocks.setRenderBounds(1.0F / 16.0F, 0.0F, 1.0F / 16.0F, 1.0F - (1.0F / 16.0F), 1.0F / 8.0F, 1.0F - (1.0F / 16.0F));
		renderblocks.renderStandardBlock(block, i, j, k);

		renderblocks.setRenderBounds(1.0F / 16.0F, 1 - (1.0F / 8.0F), 1.0F / 16.0F, 1.0F - (1.0F / 16.0F), 1.0F, 1.0F - (1.0F / 16.0F));
		renderblocks.renderStandardBlock(block, i, j, k);

		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		if (modelId == TerraIndustrialisBlocks.blockHeaterRenderId) {
			renderer.setRenderBounds(1.0F / 8.0F, 0.0F, 1.0F / 8.0F, 1.0F - (1.0F / 8.0F), 1.0F, 1.0F - (1.0F / 8.0F));
			RenderingHelper.renderInvBlock(block, metadata, renderer);

			renderer.setRenderBounds(1.0F / 16.0F, 0.0F, 1.0F / 16.0F, 1.0F - (1.0F / 16.0F), 1.0F / 8.0F, 1.0F - (1.0F / 16.0F));
			RenderingHelper.renderInvBlock(block, metadata, renderer);

			renderer.setRenderBounds(1.0F / 16.0F, 1 - (1.0F / 8.0F), 1.0F / 16.0F, 1.0F - (1.0F / 16.0F), 1.0F, 1.0F - (1.0F / 16.0F));
			RenderingHelper.renderInvBlock(block, metadata, renderer);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (modelId == TerraIndustrialisBlocks.blockHeaterRenderId) {
			return renderHeater(world, block, x, y, z, renderer);
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
