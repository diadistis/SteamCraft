package com.noiz.steamcraft.gui.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.noiz.steamcraft.entities.tiles.TEBoiler;
import com.noiz.steamcraft.gui.GuiHandlerServer;

public class GuiHandler extends GuiHandlerServer {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		switch (ID) {
		case GUI_BoilerID:
			return new GuiBoiler(player.inventory, ((TEBoiler) te), world, x,
					y, z);
		}
		return null;
	}
}
