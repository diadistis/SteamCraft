package com.noiz.steamcraft.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import TFC.Core.Player.PlayerInventory;

import com.noiz.steamcraft.entities.tiles.TEBoiler;

public class ContainerBoiler extends Container {
	private TEBoiler boiler;

	public ContainerBoiler(InventoryPlayer inventoryplayer, TEBoiler boiler,
			World world, int x, int y, int z) {
		this.boiler = boiler;
		// Input slot
		addSlotToContainer(new Slot(boiler, 0, 80, 29));

		PlayerInventory.buildInventoryLayout(this, inventoryplayer, 8, 90,
				false, true);

		// barrel.updateGui();
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
}
