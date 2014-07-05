package com.noiz.steamcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.noiz.steamcraft.containers.ContainerBoiler;
import com.noiz.steamcraft.entities.tiles.TEBoiler;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandlerServer implements IGuiHandler {

	public static final int GUI_BoilerID = 12;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

		TileEntity te = world.getBlockTileEntity(x, y, z);
		switch (ID) {
		case GUI_BoilerID:
			return new ContainerBoiler(player.inventory, ((TEBoiler) te),
					world, x, y, z);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

}
