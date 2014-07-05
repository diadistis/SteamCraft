package com.noiz.steamcraft.proxy;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.entities.tiles.TEBoiler;
import com.noiz.steamcraft.entities.tiles.TEBoilerTank;

import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy {

	public void registerBlocks() {
		GameRegistry.registerBlock(SteamCraft.blockBoiler, "BoilerBlock");
		GameRegistry.registerBlock(SteamCraft.blockBoilerTank, "BoilerTankBlock");
	}

	public void registerEntities() {
		GameRegistry.registerTileEntity(TEBoiler.class, "boiler");
		GameRegistry.registerTileEntity(TEBoilerTank.class, "boiler.tank");
	}

	public abstract void registerGUI();
}
