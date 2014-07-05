package com.noiz.steamcraft.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import com.noiz.steamcraft.SteamCraft;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBoiler implements ISimpleBlockRenderingHandler {

	public static boolean renderBoiler(Block block, int i, int j, int k, RenderBlocks renderblocks)
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
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		if(modelId == SteamCraft.blockBoilerRenderId)
		{
			return renderBoiler(block, x, y, z, renderer);
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRenderId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
