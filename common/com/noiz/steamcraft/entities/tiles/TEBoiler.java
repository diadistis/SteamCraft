package com.noiz.steamcraft.entities.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TEBoiler extends TileEntity implements IInventory {

	public static final int FuelSlot = 0;
	public static final int AshesSlot = 1;

	private ItemStack items[] = { null, null };

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int pos) {
		return pos < 0 || pos > 1 ? null : items[pos];
	}

	@Override
	public ItemStack decrStackSize(int pos, int count) {
		if (pos < 0 || pos > 1)
			return null;
		if (items[pos] == null) //
			return null;

		ItemStack itemstack = null;

		if (items[pos].stackSize <= count) {
			itemstack = items[pos];
			items[pos] = null;
			onInventoryChanged();
			return itemstack;
		}

		itemstack = items[pos].splitStack(count);

		if (items[pos].stackSize == 0)
			items[pos] = null;

		onInventoryChanged();
		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int pos) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int pos, ItemStack itemstack) {
		System.out.println("setInventorySlotContents");

		if (pos < 0 || pos > 1) //
			return;

		items[pos] = itemstack;

		if (itemstack != null
				&& itemstack.stackSize > this.getInventoryStackLimit())
			itemstack.stackSize = this.getInventoryStackLimit();

		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "Fuel";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public boolean isItemValidForSlot(int pos, ItemStack itemstack) {
		System.out.println("isItemValidForSlot");
		return pos == 0 && itemstack.getItem().itemID == Item.coal.itemID;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);

		boolean empty = par1NBTTagCompound.getBoolean("NoAshes");

		if (!empty) {
			NBTTagCompound ashes = (NBTTagCompound) par1NBTTagCompound
					.getTag("Ashes");
			items[AshesSlot] = ItemStack.loadItemStackFromNBT(ashes);
		}

		empty = par1NBTTagCompound.getBoolean("Empty");

		if (!empty) {
			NBTTagCompound fuel = (NBTTagCompound) par1NBTTagCompound
					.getTag("Fuel");
			items[FuelSlot] = ItemStack.loadItemStackFromNBT(fuel);
		} else
			items[FuelSlot] = null;
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setBoolean("NoAshes", items[AshesSlot] == null);

		if (items[AshesSlot] != null) {
			NBTTagCompound ashes = new NBTTagCompound();
			items[AshesSlot].writeToNBT(ashes);
			par1NBTTagCompound.setCompoundTag("Ashes", ashes);
		}

		par1NBTTagCompound.setBoolean("Empty", items[FuelSlot] == null);

		if (items[FuelSlot] != null) {
			NBTTagCompound fuel = new NBTTagCompound();
			items[FuelSlot].writeToNBT(fuel);
			par1NBTTagCompound.setCompoundTag("Fuel", fuel);
		}
	}
}
