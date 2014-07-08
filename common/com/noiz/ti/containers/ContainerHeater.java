package com.noiz.ti.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.noiz.ti.entities.tiles.TileEntityHeater;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerHeater extends Container {
	private final TileEntityHeater heater;

	private int quantizedEnergy;
	private int heatTargets;
	private int quantizedOutput_Wh;

	public ContainerHeater(InventoryPlayer inventoryplayer, TileEntityHeater heater, World world, int x, int y, int z) {

		this.heater = heater;

		addSlotToContainer(new CoalFuelSlot(heater, 0, 84, 25, heater));
		addSlotToContainer(new OutputOnlySlot(heater, 1, 84, 46));

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
		par1iCrafting.sendProgressBarUpdate(this, 0, heater.quantizedEnergy);
		par1iCrafting.sendProgressBarUpdate(this, 3, heater.heatTargets());
		par1iCrafting.sendProgressBarUpdate(this, 4, heater.quantizedOutput_Wh);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);

			if (quantizedEnergy != heater.quantizedEnergy)
				icrafting.sendProgressBarUpdate(this, 0, heater.quantizedEnergy);

			if (heatTargets != heater.heatTargets())
				icrafting.sendProgressBarUpdate(this, 3, heater.heatTargets());

			if (quantizedOutput_Wh != heater.quantizedOutput_Wh)
				icrafting.sendProgressBarUpdate(this, 4, heater.quantizedOutput_Wh);
		}

		quantizedEnergy = heater.quantizedEnergy;
		heatTargets = heater.heatTargets();
		quantizedOutput_Wh = heater.quantizedOutput_Wh;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0)
			heater.quantizedEnergy = par2;
		if (par1 == 3)
			heater.setHeatTargets(par2);
		if (par1 == 4)
			heater.quantizedOutput_Wh = par2;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slot) {
		if (slot < 2) // skip the heater's own inventory
			return null;

		Slot userSlot = (Slot) inventorySlots.get(slot);
		Slot heaterSlot = getSlot(TileEntityHeater.FuelSlot);

		if (userSlot == null || heaterSlot == null || !userSlot.getHasStack() || userSlot.getStack().stackSize == 0 || !heaterSlot.isItemValid(userSlot.getStack()))
			return null;

		if (!heaterSlot.getHasStack()) {
			heaterSlot.putStack(userSlot.decrStackSize(userSlot.getStack().stackSize));
			return null;
		}

		if (heaterSlot.getStack().isItemEqual(userSlot.getStack())) {
			int delta = Math.min(userSlot.getStack().stackSize, heater.getMaxItemCount(TileEntityHeater.FuelSlot) - heater.getItemCount(TileEntityHeater.FuelSlot));

			ItemStack newStack = new ItemStack(heaterSlot.getStack().itemID, heaterSlot.getStack().stackSize + delta, userSlot.getStack().getItemDamage());
			heaterSlot.putStack(newStack);

			userSlot.getStack().stackSize -= delta;

			if (userSlot.getStack().stackSize == 0)
				userSlot.putStack(null);

			if (delta != 0)
				heaterSlot.onSlotChanged();
			userSlot.onSlotChanged();
		}

		return null;
	}
}
