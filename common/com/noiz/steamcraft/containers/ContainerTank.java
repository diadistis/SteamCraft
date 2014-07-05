package com.noiz.steamcraft.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.noiz.steamcraft.entities.tiles.TileEntityTank;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerTank extends Container {
	private TileEntityTank tank;

	private int quantizedTemperature;
	private int quantizedWater;
	private int quantizedPressure;

	public ContainerTank(InventoryPlayer inventoryplayer, TileEntityTank tank, World world, int x, int y, int z) {

		this.tank = tank;

		bindPlayerInventory(inventoryplayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 7 + j * 18, 90 + i * 18));

		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 7 + i * 18, 148));
	}

	@Override
	public void addCraftingToCrafters(ICrafting par1iCrafting) {
		super.addCraftingToCrafters(par1iCrafting);
		par1iCrafting.sendProgressBarUpdate(this, 0, tank.quantizedTemperature);
		par1iCrafting.sendProgressBarUpdate(this, 1, tank.quantizedWater);
		par1iCrafting.sendProgressBarUpdate(this, 2, tank.quantizedPressure);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);

			if (quantizedTemperature != tank.quantizedTemperature)
				icrafting.sendProgressBarUpdate(this, 0, tank.quantizedTemperature);

			if (quantizedWater != tank.quantizedWater)
				icrafting.sendProgressBarUpdate(this, 1, tank.quantizedWater);

			if (quantizedPressure != tank.quantizedPressure)
				icrafting.sendProgressBarUpdate(this, 2, tank.quantizedPressure);
		}

		quantizedTemperature = tank.quantizedTemperature;
		quantizedWater = tank.quantizedWater;
		quantizedPressure = tank.quantizedPressure;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0)
			tank.quantizedTemperature = par2;
		else if (par1 == 1)
			tank.quantizedWater = par2;
		else if (par1 == 2)
			tank.quantizedPressure = par2;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slot) {
		return null;
	}
}
