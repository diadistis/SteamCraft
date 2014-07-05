package com.noiz.steamcraft.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.noiz.steamcraft.containers.ContainerHeater;
import com.noiz.steamcraft.entities.tiles.TEHeater;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandlerServer implements IGuiHandler {

	public static final int GUI_SteelHeaterID = 12;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

		TileEntity te = world.getBlockTileEntity(x, y, z);
		switch (ID) {
		case GUI_SteelHeaterID:
			return new ContainerHeater(player.inventory, ((TEHeater) te),
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
