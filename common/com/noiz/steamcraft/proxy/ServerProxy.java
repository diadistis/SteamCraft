package com.noiz.steamcraft.proxy;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.gui.GuiHandlerServer;

import cpw.mods.fml.common.network.NetworkRegistry;

public class ServerProxy extends CommonProxy {

	@Override
	public void registerGUI() {
		NetworkRegistry.instance().registerGuiHandler(SteamCraft.instance,
				new GuiHandlerServer());
	}
}
