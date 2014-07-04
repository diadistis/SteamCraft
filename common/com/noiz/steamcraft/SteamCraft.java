package com.noiz.steamcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import com.noiz.steamcraft.blocks.BlockTest;
import com.noiz.steamcraft.proxy.IProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "SteamCraft", name = "SteamCraft", version="0.0.1")
public class SteamCraft 
{
	
	@Mod.Instance("SteamCraft")
	public static SteamCraft instance;
	
	@SidedProxy(clientSide = "com.diad.steamcraft.proxy.ClientProxy", serverSide = "com.diad.steamcraft.proxy.ServerProxy")
	public static IProxy proxy;
	
	public final static Block blockTest = 
			new BlockTest(Material.ground)
				.setHardness(0.5F)
				.setStepSound(Block.soundGravelFootstep)
				.setUnlocalizedName("testBlock")
				.setCreativeTab(CreativeTabs.tabBlock);
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		GameRegistry.registerBlock(blockTest, "testBlock");
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) 
	{
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) 
	{	
	}
}
