package com.noiz.steamcraft.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class OutputOnlySlot extends Slot {

	public OutputOnlySlot(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	public int getSlotStackLimit() {
		return 1;
	}
}
