package com.noiz.steamcraft.entities.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import TFC.API.Constant.TFCBlockID;
import TFC.Core.TFC_Time;

public class TEBoiler extends TileEntity implements IInventory {

	public static final int AshItemId = TFCBlockID.Dirt;

	public static final int FuelSlot = 0;
	public static final int AshesSlot = 1;

	public static final int TicksPerFuelItem = 30;
	public static final int TicksToTempIncr = 50;
	public static final float TempIncrStep = 150;
	public static final float MaxTemperature = 4000f;

	private ItemStack items[] = { null, null };

	private boolean hasFuel = false;
	private long fuelExpirationTime = 0;
	private long temperatureStepTime = 0;
	private float temperature = 0;

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
	public void updateEntity() {
		if (TFC_Time.getTotalTicks() > fuelExpirationTime)
			hasFuel = false;

		boolean updateInventory = false;

		if (!hasFuel && items[FuelSlot] != null) {
			boolean ashConsumed = true;
			if (temperature > 0) {
				if (items[AshesSlot] == null)
					items[AshesSlot] = new ItemStack(AshItemId, 1, 0);
				else if (items[AshesSlot].stackSize < 64)
					++items[AshesSlot].stackSize;
				else
					ashConsumed = false;
			}
			if (ashConsumed) {
				decrStackSize(FuelSlot, 1);
				fuelExpirationTime = TFC_Time.getTotalTicks()
						+ TicksPerFuelItem;
				hasFuel = true;
				updateInventory = true;
			}
		}

		if (TFC_Time.getTotalTicks() > temperatureStepTime) {
			temperature += hasFuel ? TempIncrStep : -.8 * TempIncrStep;
			double t = temperature;
			temperature = Math.max(0, Math.min(temperature, MaxTemperature));
			if (t != temperature)
				updateInventory = true;
			temperatureStepTime = TFC_Time.getTotalTicks() + TicksToTempIncr;
		}

		if (updateInventory)
			onInventoryChanged();

		if (!worldObj.isRemote) //
			updateFireIcon();
	}

	private void updateFireIcon() {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int side = 7 & metadata;

		metadata = side | (temperature > 0 ? 8 : 0);
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 3);
	}

	public int getTemperatureScaled(int s) {
		return (int) (temperature * s / MaxTemperature);
	}

	@Override
	public boolean isItemValidForSlot(int pos, ItemStack itemstack) {
		return pos == 0 && itemstack.getItem().itemID == Item.coal.itemID;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);

		temperature = par1NBTTagCompound.getFloat("Temperature");

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

		par1NBTTagCompound.setFloat("Temperature", temperature);

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

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1,
				tagCompound);
	}
}
