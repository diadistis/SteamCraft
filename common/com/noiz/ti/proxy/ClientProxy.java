package com.noiz.ti.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.noiz.ti.TerraIndustrialisBlocks;
import com.noiz.ti.handlers.client.GuiHandler;
import com.noiz.ti.render.RenderHeater;
import com.noiz.ti.render.RenderTank;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerGUI() {
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new GuiHandler());
	}

	@Override
	public void registerRenderInformation() {
		RenderingRegistry.registerBlockHandler(TerraIndustrialisBlocks.blockHeaterRenderId = RenderingRegistry.getNextAvailableRenderId(), new RenderHeater());		
		RenderingRegistry.registerBlockHandler(TerraIndustrialisBlocks.blockTankRenderId = RenderingRegistry.getNextAvailableRenderId(), new RenderTank());		
	}
}
