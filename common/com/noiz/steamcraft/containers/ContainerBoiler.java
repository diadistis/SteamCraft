package com.noiz.steamcraft.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.noiz.steamcraft.entities.tiles.TEBoiler;

public class ContainerBoiler extends Container {
	public ContainerBoiler(InventoryPlayer inventoryplayer, TEBoiler boiler,
			World world, int x, int y, int z) {

		addSlotToContainer(new CoalFuelSlot(boiler, 0, 84, 25));
		addSlotToContainer(new OutputOnlySlot(boiler, 1, 84, 46));

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
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slot) {
		Slot userSlot = (Slot) inventorySlots.get(slot);
		Slot boilerSlot = getSlot(TEBoiler.FuelSlot);

		if (userSlot == null || boilerSlot == null || !userSlot.getHasStack()
				|| userSlot.getStack().stackSize == 0
				|| !boilerSlot.isItemValid(userSlot.getStack()))
			return null;

		if (!boilerSlot.getHasStack()) {
			boilerSlot.putStack(userSlot.decrStackSize(Math.min(64,
					userSlot.getStack().stackSize)));
			return null;
		}

		if (boilerSlot.getStack().isItemEqual(userSlot.getStack())) {
			int max = 64 - boilerSlot.getStack().stackSize;
			int delta = Math.min(max, userSlot.getStack().stackSize);

			boilerSlot.getStack().stackSize += delta;
			userSlot.getStack().stackSize -= delta;

			if (userSlot.getStack().stackSize == 0)
				userSlot.putStack(null);

			boilerSlot.onSlotChanged();
			userSlot.onSlotChanged();
		}

		return null;
	}
}
