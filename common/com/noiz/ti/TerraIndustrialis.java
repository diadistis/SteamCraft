package com.noiz.ti;

import com.noiz.ti.handlers.ServerTickHandler;
import com.noiz.ti.handlers.client.ClientTickHandler;
import com.noiz.ti.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = TerraIndustrialisConstants.ModId, name = TerraIndustrialisConstants.ModName, version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class TerraIndustrialis {
	@Mod.Instance(TerraIndustrialisConstants.ModId)
	public static TerraIndustrialis instance;

	@SidedProxy(clientSide = "com.noiz.ti.proxy.ClientProxy", serverSide = "com.noiz.ti.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		instance = this;

		TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
		TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);

		TerraIndustrialisBlocks.LoadBlocks();
		TerraIndustrialisBlocks.RegisterBlocks();

		proxy.registerEntities();

		TerraIndustrialisItems.Setup();
	}

	@Mod.EventHandler
	public void initialize(FMLInitializationEvent event) {

		Recipes.registerRecipes();
		Recipes.registerMoldRecipes();
		Recipes.registerKilnRecipes();

		proxy.registerGUI();

		proxy.registerRenderInformation();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}
