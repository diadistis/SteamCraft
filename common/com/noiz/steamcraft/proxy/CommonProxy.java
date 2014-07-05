package com.noiz.steamcraft.proxy;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.entities.tiles.TEBoiler;

import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy {

	public void registerBlocks() {
		GameRegistry.registerBlock(SteamCraft.blockBoiler, "BoilerBlock");
		GameRegistry.registerBlock(SteamCraft.blockBoilerTank, "BoilerTankBlock");
	}

	public void registerEntities() {
		GameRegistry.registerTileEntity(TEBoiler.class, "boiler");
	}

	public abstract void registerGUI();
}
