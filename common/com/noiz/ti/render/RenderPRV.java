package com.noiz.ti.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import com.noiz.ti.TerraIndustrialisBlocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderPRV implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		if (modelId == TerraIndustrialisBlocks.blockPRVRenderId) {
			renderer.setRenderBounds(1.0F / 16.0F, 0.0F, 1.0F / 16.0F, 1.0F - (1.0F / 16.0F), 1.0F, 1.0F - (1.0F / 16.0F));
			RenderingHelper.renderInvBlock(block, metadata, renderer);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (modelId == TerraIndustrialisBlocks.blockPRVRenderId) {
			doRender(x, y, z, block, renderer);
		}
		return false;
	}

	private void doRender(int x, int y, int z, Block block, RenderBlocks renderer) {
		float pxsz = 1.0F / 16.0F;

		renderer.setRenderBounds(pxsz, 0f, pxsz, 15 * pxsz, pxsz, 15 * pxsz);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.setRenderBounds(6 * pxsz, pxsz, 6 * pxsz, 10 * pxsz, 14 * pxsz, 10 * pxsz);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.setRenderBounds(7 * pxsz, 14 * pxsz, 7 * pxsz, 9 * pxsz, 16 * pxsz, 9 * pxsz);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.setRenderBounds(3 * pxsz, 6 * pxsz, 7 * pxsz, 13 * pxsz, 8 * pxsz, 9 * pxsz);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.setRenderBounds(7 * pxsz, 6 * pxsz, 3 * pxsz, 9 * pxsz, 8 * pxsz, 13 * pxsz);
		renderer.renderStandardBlock(block, x, y, z);
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
