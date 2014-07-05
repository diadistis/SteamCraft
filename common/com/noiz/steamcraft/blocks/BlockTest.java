package com.noiz.steamcraft.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.entities.tiles.TEBoiler;
import com.noiz.steamcraft.gui.GuiHandlerServer;

public class BlockTest extends BlockContainer implements ITileEntityProvider {

	public BlockTest(Material material) {
		super(1234, material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TEBoiler();
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int par6, float par7, float par8,
			float par9) {

		if (world.isRemote) //
			return true;

		if ((TEBoiler) world.getBlockTileEntity(i, j, k) != null) {
			// TEBoiler boiler = (TEBoiler) world.getBlockTileEntity(i, j, k);
			// ItemStack is = entityplayer.getCurrentEquippedItem();

			entityplayer.openGui(SteamCraft.instance, GuiHandlerServer.GUI_BoilerID, world, i, j, k);
		}
		return true;
	}
}
