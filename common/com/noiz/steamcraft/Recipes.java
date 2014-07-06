package com.noiz.steamcraft;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import TFC.TFCItems;
import TFC.API.Crafting.AnvilManager;
import TFC.API.Crafting.AnvilRecipe;
import TFC.API.Crafting.AnvilReq;
import TFC.API.Crafting.CraftingManagerTFC;
import TFC.API.Crafting.KilnCraftingManager;
import TFC.API.Crafting.KilnRecipe;
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
	
	public static void registerMoldRecipes() 
	{
		CraftingManagerTFC manager = CraftingManagerTFC.getInstance();
		
		manager.addRecipe(new ItemStack(SteamCraftItems.ClayMoldPipe, 1), new Object[] { " # # "," # # "," # # "," # # ","     ", Character.valueOf('#'), new ItemStack(TFCItems.FlatClay, 1, 1)});

		GameRegistry.addShapelessRecipe(new ItemStack(SteamCraftItems.LeadPipe), 
				new Object[] {getStackNoTemp(new ItemStack(SteamCraftItems.ClayMoldPipe, 1, 2))});
		
		CraftingManagerTFC.getInstance().addRecipe(new ItemStack(SteamCraftItems.ClayMoldPipe, 1, 2), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.LeadUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(SteamCraftItems.ClayMoldPipe, 1, 1)});
	}
	
	public static ItemStack	getStackTemp(ItemStack is)
	{
		NBTTagCompound Temp = new NBTTagCompound();
		Temp.setBoolean("temp", true);
		is.setTagCompound(Temp);
		return is;
	}
	
	public static ItemStack	getStackNoTemp(ItemStack is)
	{
		NBTTagCompound noTemp = new NBTTagCompound();
		noTemp.setBoolean("noTemp", true);
		is.setTagCompound(noTemp);
		return is;
	}
	
	public static void registerKilnRecipes()
	{
		KilnCraftingManager manager = KilnCraftingManager.getInstance();

		manager.addRecipe(
				new KilnRecipe(
						new ItemStack(SteamCraftItems.ClayMoldPipe, 1, 0),
						0, 
						new ItemStack(SteamCraftItems.ClayMoldPipe, 1, 1)));
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
