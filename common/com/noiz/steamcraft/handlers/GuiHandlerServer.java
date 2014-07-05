package com.noiz.steamcraft.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.noiz.steamcraft.containers.ContainerHeater;
import com.noiz.steamcraft.containers.ContainerTank;
import com.noiz.steamcraft.entities.tiles.TileEntityHeater;
import com.noiz.steamcraft.entities.tiles.TileEntityTank;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandlerServer implements IGuiHandler {

	public static final int GUI_HeaterID = 12;
	public static final int GUI_TankID = 14;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity te = world.getBlockTileEntity(x, y, z);
		switch (ID) {
		case GUI_HeaterID:
			return new ContainerHeater(player.inventory, ((TileEntityHeater) te), world, x, y, z);
		case GUI_TankID:
			return new ContainerTank(player.inventory, ((TileEntityTank) te), world, x, y, z);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

}
