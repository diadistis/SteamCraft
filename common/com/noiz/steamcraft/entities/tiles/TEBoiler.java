package com.noiz.steamcraft.entities.tiles;

import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TEBoiler extends TileEntity implements IInventory {

	private ItemStack storedStack = null;

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int pos) {
		return storedStack;
	}

	@Override
	public ItemStack decrStackSize(int pos, int count) {
		if (storedStack == null) //
			return null;

		ItemStack itemstack = null;

		if (storedStack.stackSize <= count) {
			itemstack = storedStack;
			storedStack = null;
			this.onInventoryChanged();
			return itemstack;
		}

		itemstack = storedStack.splitStack(count);

		if (storedStack.stackSize == 0)
			storedStack = null;

		this.onInventoryChanged();
		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int pos) {
		if (storedStack == null) //
			return null;

		ItemStack itemstack = storedStack;
		storedStack = null;
		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int pos, ItemStack itemstack) {
		storedStack = itemstack;

		if (itemstack != null
				&& itemstack.stackSize > this.getInventoryStackLimit())
			itemstack.stackSize = this.getInventoryStackLimit();

		this.onInventoryChanged();
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
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType().blockID, 1, 1);
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
	}

	@Override
	public void closeChest() {
        if (getBlockType() != null && getBlockType() instanceof BlockChest)
        {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType().blockID, 1, 0);
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
        }
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack.itemID == Item.coal.itemID;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		boolean empty = par1NBTTagCompound.getBoolean("Empty");
		if( !empty ) {
			NBTTagCompound fuel = (NBTTagCompound) par1NBTTagCompound.getTag("Fuel");
			storedStack = ItemStack.loadItemStackFromNBT(fuel);
		}
		else storedStack = null;
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Empty", storedStack == null);
		if( storedStack != null )
		{
			NBTTagCompound fuel = new NBTTagCompound();
			storedStack.writeToNBT(fuel);
			par1NBTTagCompound.setCompoundTag("Fuel", fuel);
		}
	}
}
