package com.noiz.steamcraft.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.SteamCraftBlocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderHeater implements ISimpleBlockRenderingHandler {

	public static boolean renderHeater(Block block, int i, int j, int k, RenderBlocks renderblocks)
	{
		renderblocks.setRenderBounds(1.0F/8.0F, 0.0F, 1.0F/8.0F, 1.0F - (1.0F/8.0F), 1.0F, 1.0F - (1.0F/8.0F));
		renderblocks.renderStandardBlock(block, i, j, k);

		renderblocks.setRenderBounds(1.0F/16.0F, 0.0F, 1.0F/16.0F, 1.0F - (1.0F/16.0F), 1.0F/8.0F, 1.0F - (1.0F/16.0F));
		renderblocks.renderStandardBlock(block, i, j, k);

		renderblocks.setRenderBounds(1.0F/16.0F, 1 - (1.0F/8.0F), 1.0F/16.0F, 1.0F - (1.0F/16.0F), 1.0F, 1.0F - (1.0F/16.0F));
		renderblocks.renderStandardBlock(block, i, j, k);

		return true;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) 
	{
		if(modelId == SteamCraftBlocks.blockHeaterRenderId)
		{
			renderer.setRenderBounds(1.0F/8.0F, 0.0F, 1.0F/8.0F, 1.0F - (1.0F/8.0F), 1.0F, 1.0F - (1.0F/8.0F));
			RenderingHelper.renderInvBlock(block, metadata, renderer);

			renderer.setRenderBounds(1.0F/16.0F, 0.0F, 1.0F/16.0F, 1.0F - (1.0F/16.0F), 1.0F/8.0F, 1.0F - (1.0F/16.0F));
			RenderingHelper.renderInvBlock(block, metadata, renderer);

			renderer.setRenderBounds(1.0F/16.0F, 1 - (1.0F/8.0F), 1.0F/16.0F, 1.0F - (1.0F/16.0F), 1.0F, 1.0F - (1.0F/16.0F));
			RenderingHelper.renderInvBlock(block, metadata, renderer);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		if(modelId == SteamCraftBlocks.blockHeaterRenderId)
		{
			return renderHeater(block, x, y, z, renderer);
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
