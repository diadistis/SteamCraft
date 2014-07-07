package com.noiz.ti.proxy;

import com.noiz.ti.entities.tiles.TileEntityHeater;
import com.noiz.ti.entities.tiles.TileEntityTank;

import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy {

	public void registerEntities() {
		GameRegistry.registerTileEntity(TileEntityHeater.class, "heater");
		GameRegistry.registerTileEntity(TileEntityTank.class, "tank");
	}

	public abstract void registerGUI();

	public abstract void registerRenderInformation();
}
