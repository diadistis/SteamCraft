package com.noiz.steamcraft;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import TFC.TFCItems;
import TFC.API.Crafting.AnvilManager;
import TFC.API.Crafting.AnvilRecipe;
import TFC.API.Crafting.AnvilReq;
import TFC.API.Crafting.PlanRecipe;
import TFC.API.Enums.RuleEnum;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes 
{
	public static void registerRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(SteamCraftBlocks.blockHeater, 1), new Object[] { "PPP","PCP","PPP", Character.valueOf('P'), new ItemStack(TFCItems.SteelSheet2x, 1),Character.valueOf('C'), new ItemStack(SteamCraftItems.Hatch, 1)});
		GameRegistry.addRecipe(new ItemStack(SteamCraftBlocks.blockTank, 1), new Object[] { "PPP","P P","PPP", Character.valueOf('P'), new ItemStack(TFCItems.SteelSheet2x, 1)});
	}
	
	public static void registerAnvilRecipes(Random R, World world)
	{
		System.out.println("registerAnvilRecipes");
		
		AnvilManager manager = AnvilManager.getInstance();
		
		manager.addPlan("hatch", new PlanRecipe(
				new RuleEnum[] {RuleEnum.HITLAST, RuleEnum.HITSECONDFROMLAST, RuleEnum.HITTHIRDFROMLAST}));

		manager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.SteelSheet2x), null, "hatch", false, AnvilReq.STEEL, new ItemStack(SteamCraftItems.Hatch)));
	}
}
