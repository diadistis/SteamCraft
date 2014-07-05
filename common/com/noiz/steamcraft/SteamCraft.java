package com.noiz.steamcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import com.noiz.steamcraft.blocks.BlockBoiler;
import com.noiz.steamcraft.blocks.BlockBoilerTank;
import com.noiz.steamcraft.handlers.ServerTickHandler;
import com.noiz.steamcraft.handlers.client.ClientTickHandler;
import com.noiz.steamcraft.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = SteamCraftConstants.ModId, name = "SteamCraft", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class SteamCraft {
	@Mod.Instance(SteamCraftConstants.ModId)
	public static SteamCraft instance;

	@SidedProxy(clientSide = "com.noiz.steamcraft.proxy.ClientProxy", serverSide = "com.noiz.steamcraft.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static final Block blockBoiler = new BlockBoiler(Material.iron)
		.setHardness(0.5F).setStepSound(Block.soundMetalFootstep)
		.setUnlocalizedName("Boiler")
		.setCreativeTab(CreativeTabs.tabBlock);

	public static final Block blockBoilerTank = new BlockBoilerTank(Material.iron)
		.setHardness(0.5F).setStepSound(Block.soundMetalFootstep)
		.setUnlocalizedName("Boiler Tank")
		.setCreativeTab(CreativeTabs.tabBlock);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		instance = this;
		
		System.out.println("SteamCraft::preInit");
		
		TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
		TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
		
		proxy.registerBlocks();
		proxy.registerEntities();
		
		SteamCraftItems.Setup();
	}

	@Mod.EventHandler
	public void initialize(FMLInitializationEvent event) {
		
		Recipes.registerRecipes();
		
		proxy.registerGUI();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}
