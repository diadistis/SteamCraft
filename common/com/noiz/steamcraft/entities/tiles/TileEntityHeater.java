package com.noiz.steamcraft.entities.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import TFC.TFCBlocks;
import TFC.Core.TFC_Time;

import com.noiz.steamcraft.entities.tiles.multiblock.TileEntityRectMultiblock;
import com.noiz.steamcraft.handlers.client.GuiHandler;

public class TileEntityHeater extends TileEntityRectMultiblock implements IInventory {

	public static final int FuelSlot = 0;
	public static final int AshesSlot = 1;

	public static final int FuelPerBlock = 64;
	public static final int AshesPerBlock = 64;
	public static final int ItemsPerInvSlot = 32;

	public static final int TicksPerFuelItem = 30;
	public static final int TicksToTempIncr = 50;
	public static final float TempIncrStep = 150;
	public static final float MaxTemperature = 4000f;
	public static final float AshPropability = .1f;

	private static final int[] MaxItemsPerBlock = { FuelPerBlock, AshesPerBlock };

	private final ItemStack items[] = { new ItemStack(Item.coal, 0), new ItemStack(TFCBlocks.Dirt, 0) };
	private final int itemCounts[] = { 0, 0 };

	/**
	 * when <code>true</code> the heater consumes a fuel item that has been
	 * removed from its inventory.
	 */
	private boolean consumingExistingFuel = false;
	private long fuelExpirationTime = 0;

	private float temperature = 0;
	private long temperatureStepTime = 0;

	public int quantizedTemperature = 0;

	public int getItemCount(int pos) {
		return itemCounts[pos];
	}

	public void setItemCount(int pos, int count) {
		itemCounts[pos] = count;
		items[pos].stackSize = Math.min(ItemsPerInvSlot, itemCounts[pos]);
	}

	public int getMaxItemCount(int pos) {
		return MaxItemsPerBlock[pos];
	}

	public float temperature() {
		return temperature;
	}

	public void addFuelItems(int count) {
		count = Math.min(count, MaxItemsPerBlock[FuelSlot] - itemCounts[FuelSlot]);
		itemCounts[FuelSlot] += count;
		items[FuelSlot].stackSize = Math.min(itemCounts[FuelSlot], ItemsPerInvSlot);
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int pos) {
		if (pos < 0 || pos > 1 || itemCounts[pos] == 0) //
			return null;
		items[pos].stackSize = Math.min(itemCounts[pos], ItemsPerInvSlot);
		return items[pos];
	}

	@Override
	public ItemStack decrStackSize(int pos, int count) {
		if (pos < 0 || pos > 1)
			return null;
		if (items[pos] == null) //
			return null;

		ItemStack itemstack = null;

		int decreaseAmount = Math.min(count, itemCounts[pos]);

		if (decreaseAmount > 0) {
			itemCounts[pos] -= decreaseAmount;
			itemstack = new ItemStack(items[pos].itemID, decreaseAmount, 0);
			onInventoryChanged();
		}

		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int pos) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int pos, ItemStack itemstack) {
		if (pos < 0 || pos > 1 || itemstack == null) //
			return;

		itemCounts[pos] += itemstack.stackSize;
		int maxCount = structureBlockCount() * MaxItemsPerBlock[pos];
		if (itemCounts[pos] > maxCount)
			itemCounts[pos] = maxCount;

		itemstack.stackSize -= Math.min(itemstack.stackSize, maxCount);

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
	protected void mergeThisMasterToNextOne(TileEntityRectMultiblock nextMaster) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStructureDismantle() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void structureCreatedWithThisAsMaster() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote)
			return;

		if (TFC_Time.getTotalTicks() > fuelExpirationTime)
			consumingExistingFuel = false;

		float t = temperature;
		int a = itemCounts[AshesSlot];
		int f = itemCounts[FuelSlot];
		try {
			if (!consumingExistingFuel && itemCounts[FuelSlot] > 0) {
				boolean ashProduced = true;
				if (temperature > 0 && AshPropability > Math.random()) {
					if (itemCounts[AshesSlot] >= structureBlockCount() * AshesPerBlock)
						ashProduced = false;
					else
						++itemCounts[AshesSlot];
				}
				if (ashProduced) {
					--itemCounts[FuelSlot];
					fuelExpirationTime = TFC_Time.getTotalTicks() + TicksPerFuelItem;
					consumingExistingFuel = true;
				}
			}

			if (TFC_Time.getTotalTicks() > temperatureStepTime) {
				temperature += consumingExistingFuel ? TempIncrStep : -.8 * TempIncrStep;
				temperature = Math.max(0, Math.min(temperature, MaxTemperature));
				temperatureStepTime = TFC_Time.getTotalTicks() + TicksToTempIncr;
			}

			updateFireIcon();
		} finally {
			if (t != temperature || a != itemCounts[AshesSlot] || f != fuelExpirationTime) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				onInventoryChanged();
				quantizeUIGaugeValues();
			}
		}
	}

	private void updateFireIcon() {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int side = 7 & metadata;

		metadata = side | (temperature > 0 ? 8 : 0);
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 3);
	}

	@Override
	public boolean isItemValidForSlot(int pos, ItemStack itemstack) {
		return pos == 0 && itemstack.getItem().itemID == Item.coal.itemID;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);

		if (!isMaster())
			return;

		temperature = par1NBTTagCompound.getFloat("Temperature");
		itemCounts[FuelSlot] = par1NBTTagCompound.getInteger("Fuel");
		itemCounts[AshesSlot] = par1NBTTagCompound.getInteger("Ashes");

		quantizeUIGaugeValues();
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);

		if (!isMaster())
			return;

		par1NBTTagCompound.setFloat("Temperature", temperature);
		par1NBTTagCompound.setInteger("Fuel", itemCounts[FuelSlot]);
		par1NBTTagCompound.setInteger("Ashes", itemCounts[AshesSlot]);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNBT(pkt.data);
		updateFireIcon();
	}

	private void quantizeUIGaugeValues() {
		quantizedTemperature = (int) (temperature * GuiHandler.GUI_GaugeScale / MaxTemperature);
	}
}
