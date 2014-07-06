package com.noiz.steamcraft.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.noiz.steamcraft.SteamCraftBlocks;
import com.noiz.steamcraft.handlers.client.GuiHandler;
import com.noiz.steamcraft.render.RenderHeater;
import com.noiz.steamcraft.render.RenderTank;

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
		RenderingRegistry.registerBlockHandler(SteamCraftBlocks.blockHeaterRenderId = RenderingRegistry.getNextAvailableRenderId(), new RenderHeater());		
		RenderingRegistry.registerBlockHandler(SteamCraftBlocks.blockTankRenderId = RenderingRegistry.getNextAvailableRenderId(), new RenderTank());		
	}
}
