package com.noiz.steamcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;

import com.noiz.steamcraft.entities.tiles.TEBoiler;

public class ContainerBoiler extends Container {
	private TEBoiler boiler;

	public ContainerBoiler(InventoryPlayer inventoryplayer, TEBoiler boiler,
			World world, int x, int y, int z) {

		this.boiler = boiler;
		// Input slot
		addSlotToContainer(new Slot(boiler, 0, 80, 29));

		bindPlayerInventory(inventoryplayer);

		// barrel.updateGui();
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}
}
