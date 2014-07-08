package com.noiz.ti.proxy;

import com.noiz.ti.TerraIndustrialis;
import com.noiz.ti.handlers.GuiHandlerServer;

import cpw.mods.fml.common.network.NetworkRegistry;

public class ServerProxy extends CommonProxy {

	@Override
	public void registerGUI() {
		NetworkRegistry.instance().registerGuiHandler(TerraIndustrialis.instance, new GuiHandlerServer());
	}

	@Override
	public void registerRenderInformation() {
	}
}
