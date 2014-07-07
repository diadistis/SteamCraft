package com.noiz.ti;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import com.noiz.ti.blocks.BlockPipe;
import com.noiz.ti.blocks.BlockSteelHeater;
import com.noiz.ti.blocks.BlockSteelTank;
import com.noiz.ti.entities.tiles.multiblock.TileEntityRectMultiblock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class TerraIndustrialisBlocks {
	
	public static int blockHeaterRenderId;
	public static int blockTankRenderId;
	public static int blockPipeRenderId;
	
	public static Block blockHeater;
	public static Block blockTank;
	public static Block blockPipe;
	
	public static void LoadBlocks() {
		blockHeater = new BlockSteelHeater(Material.iron).setHardness(0.5F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("Steel Heater").setCreativeTab(CreativeTabs.tabBlock);
		blockTank = new BlockSteelTank(Material.iron).setHardness(0.5F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("Steel Tank").setCreativeTab(CreativeTabs.tabBlock);
		blockPipe = new BlockPipe(Material.iron).setHardness(0.5F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("Pipe").setCreativeTab(CreativeTabs.tabBlock);
		
		TileEntityRectMultiblock.registerStructureLimits(blockHeater.blockID, 10, 1, 10);
		TileEntityRectMultiblock.registerStructureLimits(blockTank.blockID, 40, 40, 40);
	}
	
	public static void RegisterBlocks() {
		GameRegistry.registerBlock(blockHeater, "HeaterBlock");
		GameRegistry.registerBlock(blockTank, "TankBlock");
		GameRegistry.registerBlock(blockPipe, "PipeBlock");

		LanguageRegistry.addName(blockHeater, "Steel Heater");
		LanguageRegistry.addName(blockTank, "Steel Tank");
		LanguageRegistry.addName(blockPipe, "Pipe");
	}
}
