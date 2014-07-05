package com.noiz.steamcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import com.noiz.steamcraft.blocks.BlockTest;
import com.noiz.steamcraft.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = SteamCraftConstants.ModId, name = "SteamCraft", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class SteamCraft {
	@Mod.Instance(SteamCraftConstants.ModId)
	public static SteamCraft instance;

	@SidedProxy(clientSide = "com.noiz.steamcraft.proxy.ClientProxy", serverSide = "com.noiz.steamcraft.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static final Block blockTest = new BlockTest(Material.ground)
			.setHardness(0.5F).setStepSound(Block.soundGravelFootstep)
			.setUnlocalizedName("testBlock")
			.setCreativeTab(CreativeTabs.tabBlock);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		instance = this;
		proxy.registerBlocks();
		proxy.registerEntities();
	}

	@Mod.EventHandler
	public void initialize(FMLInitializationEvent event) {
		proxy.registerGUI();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}
