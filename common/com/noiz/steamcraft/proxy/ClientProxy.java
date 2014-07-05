package com.noiz.steamcraft.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.handlers.client.BlockRenderHandler;
import com.noiz.steamcraft.handlers.client.GuiHandler;

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
		RenderingRegistry.registerBlockHandler(SteamCraft.blockHeaterRenderId = RenderingRegistry.getNextAvailableRenderId(), new BlockRenderHandler());		
	}
}
