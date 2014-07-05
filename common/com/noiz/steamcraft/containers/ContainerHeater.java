package com.noiz.steamcraft.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.noiz.steamcraft.entities.tiles.TEHeater;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerHeater extends Container {
	private TEHeater heater;
	private int quantizedTemperature;

	public ContainerHeater(InventoryPlayer inventoryplayer, TEHeater heater,
			World world, int x, int y, int z) {

		this.heater = heater;

		addSlotToContainer(new CoalFuelSlot(heater, 0, 84, 25));
		addSlotToContainer(new OutputOnlySlot(heater, 1, 84, 46));

		bindPlayerInventory(inventoryplayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						7 + j * 18, 90 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 7 + i * 18, 148));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting par1iCrafting) {
		super.addCraftingToCrafters(par1iCrafting);
		par1iCrafting.sendProgressBarUpdate(this, 0,
				heater.quantizedTemperature);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);

			if (quantizedTemperature != heater.quantizedTemperature)
				icrafting.sendProgressBarUpdate(this, 0,
						heater.quantizedTemperature);
		}

		quantizedTemperature = heater.quantizedTemperature;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0)
			this.heater.quantizedTemperature = par2;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slot) {
		Slot userSlot = (Slot) inventorySlots.get(slot);
		Slot heaterSlot = getSlot(TEHeater.FuelSlot);

		if (userSlot == null || heaterSlot == null || !userSlot.getHasStack()
				|| userSlot.getStack().stackSize == 0
				|| !heaterSlot.isItemValid(userSlot.getStack()))
			return null;

		if (!heaterSlot.getHasStack()) {
			heaterSlot.putStack(userSlot.decrStackSize(Math.min(64,
					userSlot.getStack().stackSize)));
			return null;
		}

		if (heaterSlot.getStack().isItemEqual(userSlot.getStack())) {
			int max = 64 - heaterSlot.getStack().stackSize;
			int delta = Math.min(max, userSlot.getStack().stackSize);

			heaterSlot.getStack().stackSize += delta;
			userSlot.getStack().stackSize -= delta;

			if (userSlot.getStack().stackSize == 0)
				userSlot.putStack(null);

			heaterSlot.onSlotChanged();
			userSlot.onSlotChanged();
		}

		return null;
	}
}
