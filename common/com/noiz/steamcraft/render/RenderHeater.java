package com.noiz.steamcraft.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import com.noiz.steamcraft.SteamCraft;

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
		if(modelId == SteamCraft.blockHeaterRenderId)
		{
			renderer.setRenderBounds(1.0F/8.0F, 0.0F, 1.0F/8.0F, 1.0F - (1.0F/8.0F), 1.0F, 1.0F - (1.0F/8.0F));
			renderInvBlock(block, metadata, renderer);

			renderer.setRenderBounds(1.0F/16.0F, 0.0F, 1.0F/16.0F, 1.0F - (1.0F/16.0F), 1.0F/8.0F, 1.0F - (1.0F/16.0F));
			renderInvBlock(block, metadata, renderer);

			renderer.setRenderBounds(1.0F/16.0F, 1 - (1.0F/8.0F), 1.0F/16.0F, 1.0F - (1.0F/16.0F), 1.0F, 1.0F - (1.0F/16.0F));
			renderInvBlock(block, metadata, renderer);
		}
	}

	public static void renderInvBlock(Block block, int m, RenderBlocks renderer)
	{
		Tessellator var14 = Tessellator.instance;
		int meta = m;
		if(meta >=8)
		{
			meta-=8;
		}
		var14.startDrawingQuads();
		var14.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
		var14.draw();
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		if(modelId == SteamCraft.blockHeaterRenderId)
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
