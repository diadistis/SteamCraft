package com.noiz.ti.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.noiz.ti.entities.tiles.TileEntityHeater;

public class CoalFuelSlot extends Slot {

	private final TileEntityHeater heater;

	public CoalFuelSlot(IInventory par1iInventory, int par2, int par3, int par4, TileEntityHeater heater) {
		super(par1iInventory, par2, par3, par4);
		this.heater = heater;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		ItemStack fuel = heater.getStackInSlot(TileEntityHeater.FuelSlot);
		if (fuel != null && fuel.stackSize > 0 && fuel.getItemDamage() != itemstack.getItemDamage())
			return false;

		return itemstack.itemID == Item.coal.itemID;
	}

	@Override
	public int getSlotStackLimit() {
		return heater.getMaxItemCount(TileEntityHeater.FuelSlot);
	}
}
