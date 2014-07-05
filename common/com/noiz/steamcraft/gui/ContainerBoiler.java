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
		addSlotToContainer(new Slot(boiler, 0, 84, 25));
		addSlotToContainer(new Slot(boiler, 1, 84, 46));

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
}
