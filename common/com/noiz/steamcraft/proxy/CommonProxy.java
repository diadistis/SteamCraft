package com.noiz.steamcraft.proxy;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.entities.tiles.TEBoiler;

import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {

	public void registerBlocks() {
		GameRegistry.registerBlock(SteamCraft.blockTest, "testBlock");
	}

	public void registerEntities() {
		GameRegistry.registerTileEntity(TEBoiler.class, "boiler");
	}
	
	public abstract void registerGUI();
}
