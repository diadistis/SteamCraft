package com.noiz.steamcraft.entities.tiles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import TFC.TFCBlocks;
import TFC.Core.TFC_Time;

import com.noiz.steamcraft.SteamCraftBlocks;
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

	public static final int HeatablesScanPeriod = 40;

	private static final int[] MaxItemsPerBlock = { FuelPerBlock, AshesPerBlock };

	private final ItemStack items[] = { new ItemStack(Item.coal, 0), new ItemStack(TFCBlocks.Dirt, 0) };
	private final int itemCounts[] = { 0, 0 };

	private final Set<IHeatable> heatables = new HashSet<>();
	private int heatTargetsOverride = 0;

	/**
	 * when <code>true</code> the heater consumes a fuel item that has been
	 * removed from its inventory.
	 */
	private boolean consumingExistingFuel = false;
	private long fuelExpirationTime = 0;

	private float temperature = 0;
	private long temperatureStepTime = 0;

	private int ticksSinceLastScan = 0;

	public int quantizedTemperature = 0;

	public int heatTargets() {
		return heatables.size() == 0 ? heatTargetsOverride : heatables.size();
	}

	public void setHeatTargets(int no) {
		heatTargetsOverride = no;
	}

	public int getItemCount(int pos) {
		return itemCounts[pos];
	}

	public void setItemCount(int pos, int count) {
		itemCounts[pos] = count;
		items[pos].stackSize = Math.min(ItemsPerInvSlot, itemCounts[pos]);
	}

	public int getMaxItemCount(int pos) {
		return MaxItemsPerBlock[pos] * structureBlockCount();
	}

	public float temperature() {
		return temperature;
	}

	public void addItems(int pos, int count) {
		count = Math.min(count, MaxItemsPerBlock[pos] - itemCounts[pos]);
		itemCounts[pos] += count;
		items[pos].stackSize = Math.min(itemCounts[pos], ItemsPerInvSlot);
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
		TileEntityHeater masterHeater = (TileEntityHeater) nextMaster;

		masterHeater.addItems(0, itemCounts[0]);
		masterHeater.addItems(1, itemCounts[1]);

		masterHeater.quantizeUIGaugeValues();
	}

	@Override
	protected void onStructureDismantle() {
		temperature = 0;

		List<TileEntityRectMultiblock> members = structureMembers(SteamCraftBlocks.blockHeater.blockID);
		if (members.size() == 0)
			return;

		int fuelPerMember = itemCounts[FuelSlot] / members.size();
		int ashesPerMember = itemCounts[AshesSlot] / members.size();

		int remainderFuel = itemCounts[FuelSlot] - fuelPerMember * members.size();
		int remainderAshes = itemCounts[AshesSlot] - ashesPerMember * members.size();

		boolean firstMember = true;
		for (TileEntityRectMultiblock member : members) {
			TileEntityHeater heater = (TileEntityHeater) member;

			if (firstMember) {
				firstMember = false;
				setItemCount(FuelSlot, fuelPerMember + remainderFuel);
				setItemCount(AshesSlot, ashesPerMember + remainderAshes);
			} else {
				setItemCount(FuelSlot, fuelPerMember);
				setItemCount(AshesSlot, ashesPerMember);
			}

			heater.quantizeUIGaugeValues();
		}
	}

	@Override
	protected void structureCreatedWithThisAsMaster() {
		scanForHeatables();
	}

	@Override
	public void updateEntity() {
		if (!isMaster()) {
			TileEntityHeater master = master();
			if (master != null)
				updateFireIcon(master.temperature > 0);
			return;
		}

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

			++ticksSinceLastScan;
			if (temperature > 0) {
				if (ticksSinceLastScan > HeatablesScanPeriod) {
					ticksSinceLastScan = 0;
					scanForHeatables();
				}

				float delta = 0;
				for (IHeatable target : heatables)
					delta -= target.transferHeat(structureBlockCount(), temperature);
				if (delta > 0)
					temperature -= Math.min(temperature / 2, delta / heatables.size());
			}

			updateFireIcon(temperature > 0);
		} finally {
			if (t != temperature || a != itemCounts[AshesSlot] || f != fuelExpirationTime) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				onInventoryChanged();
				quantizeUIGaugeValues();
			}
		}
	}

	private void updateFireIcon(boolean onFire) {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int side = 7 & metadata;

		metadata = side | (onFire ? 8 : 0);
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 3);
	}

	private void scanForHeatables() {
		int[] min = { 0, 0, 0 };
		int[] max = { 0, 0, 0 };
		fetchStructureCoordinates(min, max);

		heatables.clear();
		for (int x = min[0]; x <= max[0]; ++x)
			for (int z = min[2]; z <= max[2]; ++z) {
				TileEntity e = worldObj.getBlockTileEntity(x, yCoord + 1, z);
				if (e != null && e instanceof IHeatable) {
					if (e instanceof TileEntityRectMultiblock)
						heatables.add((IHeatable) ((TileEntityRectMultiblock) e).master());
					else
						heatables.add((IHeatable) e);
				}
			}
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
		updateFireIcon(temperature > 0);
	}

	private void quantizeUIGaugeValues() {
		quantizedTemperature = (int) (temperature * GuiHandler.GUI_GaugeScale / MaxTemperature);
	}
}
