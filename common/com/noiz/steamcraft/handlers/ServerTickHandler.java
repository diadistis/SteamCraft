package com.noiz.steamcraft.handlers;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.world.World;

import com.noiz.steamcraft.Recipes;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ServerTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		
		if(type.contains(TickType.WORLDLOAD))
		{
			System.out.println("ServerTickHandler::WORLDLOAD");
			
			World world = (World)tickData[0];
			Random R = new Random(world.getSeed());
			if(world.provider.dimensionId == 0)
			{
				Recipes.registerAnvilRecipes(R, world);
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		// TODO Auto-generated method stub

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD, TickType.WORLDLOAD, TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "SteamCraft Server";
	}

}
