package com.noiz.steamcraft.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.blocks.BlockSteelTank;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderTank implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
		if (modelId == SteamCraft.blockTankRenderId) {
			renderer.setRenderBounds(1.0F / 16.0F, 0.0F, 1.0F / 16.0F, 1.0F - (1.0F / 16.0F),
					1.0F, 1.0F - (1.0F / 16.0F));
			RenderingHelper.renderInvBlock(block, metadata, renderer);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		if (modelId == SteamCraft.blockTankRenderId) {
			
			float pixelSize = 1.0F / 16.0F;
			float width = 1.0F - (pixelSize * 2);
		    float length = 1.0F - (pixelSize * 2);
			float west = pixelSize;
			float north = pixelSize;
			
			ForgeDirection[] connectedSides = ((BlockSteelTank)block).getConnectedSides();
			
			for(int i = 0; i < connectedSides.length; i++) {
				if(connectedSides[i] == ForgeDirection.EAST) {
					width += pixelSize;
				} else if(connectedSides[i] == ForgeDirection.WEST) {
					west -= pixelSize;
					width += pixelSize;
				} else if(connectedSides[i] == ForgeDirection.NORTH) {
					north -= pixelSize;
					length += pixelSize;
				} else if(connectedSides[i] == ForgeDirection.SOUTH) {
					length += pixelSize;
				}
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
