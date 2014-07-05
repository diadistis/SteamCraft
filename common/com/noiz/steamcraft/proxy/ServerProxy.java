package com.noiz.steamcraft.proxy;

import com.noiz.steamcraft.gui.GuiHandlerServer;

import cpw.mods.fml.common.network.NetworkRegistry;

public class ServerProxy extends CommonProxy {

	@Override
	public void registerGUI() {
		NetworkRegistry.instance().registerGuiHandler(this,
				new GuiHandlerServer());
	}
}
