package com.noiz.steamcraft.proxy;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.entities.tiles.TEHeater;
import com.noiz.steamcraft.entities.tiles.TETank;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class CommonProxy {

	public void registerBlocks() {
		GameRegistry.registerBlock(SteamCraft.blockHeater, "HeaterBlock");
		GameRegistry.registerBlock(SteamCraft.blockTank, "TankBlock");

		LanguageRegistry.addName(SteamCraft.blockHeater, "Steel Heater");
		LanguageRegistry.addName(SteamCraft.blockTank, "Steel Tank");
	}

	public void registerEntities() {
		GameRegistry.registerTileEntity(TEHeater.class, "heater");
		GameRegistry.registerTileEntity(TETank.class, "tank");
	}

	public abstract void registerGUI();

	public abstract void registerRenderInformation();
}
