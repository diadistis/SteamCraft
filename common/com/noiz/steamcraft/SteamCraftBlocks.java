package com.noiz.steamcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import com.noiz.steamcraft.blocks.BlockSteelHeater;
import com.noiz.steamcraft.blocks.BlockSteelTank;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class SteamCraftBlocks {
	
	public static int blockHeaterRenderId;
	public static int blockTankRenderId;

	public static Block blockHeater;
	public static Block blockTank;
	
	public static void LoadBlocks() {
		blockHeater = new BlockSteelHeater(Material.iron).setHardness(0.5F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("Steel Heater").setCreativeTab(CreativeTabs.tabBlock);
		blockTank = new BlockSteelTank(Material.iron).setHardness(0.5F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("Steel Tank").setCreativeTab(CreativeTabs.tabBlock);
	}
	
	public static void RegisterBlocks() {
		GameRegistry.registerBlock(blockHeater, "HeaterBlock");
		GameRegistry.registerBlock(blockTank, "TankBlock");

		LanguageRegistry.addName(blockHeater, "Steel Heater");
		LanguageRegistry.addName(blockTank, "Steel Tank");
	}
}
