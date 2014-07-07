package com.noiz.steamcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import com.noiz.steamcraft.blocks.BlockSteelHeater;
import com.noiz.steamcraft.blocks.BlockSteelTank;
import com.noiz.steamcraft.entities.tiles.multiblock.TileEntityRectMultiblock;

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
		
		TileEntityRectMultiblock.registerStructureLimits(blockHeater.blockID, 10, 1, 10);
		TileEntityRectMultiblock.registerStructureLimits(blockTank.blockID, 40, 40, 40);
	}
	
	public static void RegisterBlocks() {
		GameRegistry.registerBlock(blockHeater, "HeaterBlock");
		GameRegistry.registerBlock(blockTank, "TankBlock");

		LanguageRegistry.addName(blockHeater, "Steel Heater");
		LanguageRegistry.addName(blockTank, "Steel Tank");
	}
}
