package com.noiz.steamcraft.handlers.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.render.RenderBoiler;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRenderHandler implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {

		if (modelId == SteamCraft.blockBoilerRenderId)
		{
			return RenderBoiler.renderBoiler(block, x, y, z, renderer);
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
