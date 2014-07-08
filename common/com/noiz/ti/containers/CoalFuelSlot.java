package com.noiz.ti.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CoalFuelSlot extends Slot {

	private final int itemsLimit;

	public CoalFuelSlot(IInventory par1iInventory, int par2, int par3, int par4, int itemsLimit) {
		super(par1iInventory, par2, par3, par4);
		this.itemsLimit = itemsLimit;
	}

	public boolean isItemValid(ItemStack itemstack) {
		return itemstack.itemID == Item.coal.itemID;
	}

	public int getSlotStackLimit() {
		return itemsLimit;
	}
}
