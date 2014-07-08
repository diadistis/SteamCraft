package com.noiz.ti.entities.tiles;

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

import com.noiz.ti.TerraIndustrialisBlocks;
import com.noiz.ti.entities.tiles.multiblock.TileEntityRectMultiblock;
import com.noiz.ti.gui.GUITools;
import com.noiz.ti.physics.FuelMaterial;
import com.noiz.ti.physics.IHeatSource;
import com.noiz.ti.physics.IHeatable;
import com.noiz.ti.physics.SolidMaterial;
import com.noiz.ti.physics.Thermodynamics;
import com.noiz.ti.physics.Units;

public class TileEntityHeater extends TileEntityRectMultiblock implements IInventory, IHeatSource {

	public static final int UpdatePeriodTicks = 20;
	public static final int HeatablesScanPeriod = 100;

	public static final int FuelSlot = 0;
	public static final int AshesSlot = 1;

	public static final int FuelPerBlock = 64;
	public static final int AshesPerBlock = 64;
	public static final int ItemsPerInventorySlot = 32;

	public static final float Efficiency = .86f;
	public static final float MaxEnergyPerBlock = 40000000; // ~ 40k BTU
	public static final float AshPropability = .1f;

	public static final FuelMaterial Fuel = FuelMaterial.Coal;
	public static final SolidMaterial StructureMaterial = SolidMaterial.Steel;

	private static final int[] MaxItemsPerBlock = { FuelPerBlock, AshesPerBlock };
	private final int itemCounts[] = { 0, 0 };
	private final ItemStack items[] = { new ItemStack(Item.coal, 0), new ItemStack(TFCBlocks.Dirt, 0) };

	private IHeatable[] heatables = null;
	private int heatTargetsOverride = 0;

	private boolean turnedOn = true;
	private int cachedBlockCount = 1;

	private float burningFuelMass = .0f;
	private float thermalEnergyContent = 0;

	private long lastScan = 0;
	private long lastUpdate = 0;

	public int quantizedEnergy = 0;
	public int quantizedOutput_Wh = 0;

	public int structureSize() {
		return cachedBlockCount;
	}

	public int heatTargets() {
		return heatables == null ? heatTargetsOverride : heatables.length;
	}

	public void setHeatTargets(int no) {
		heatTargetsOverride = no;
	}

	public int getItemCount(int pos) {
		return itemCounts[pos];
	}

	public void setItemCount(int pos, int count) {
		itemCounts[pos] = count;
		items[pos].stackSize = Math.min(ItemsPerInventorySlot, itemCounts[pos]);
	}

	public int getMaxItemCount(int pos) {
		return MaxItemsPerBlock[pos] * cachedBlockCount;
	}

	@Override
	public float energy() {
		return thermalEnergyContent;
	}

	@Override
	public float temperature() {
		return isHeaterActive() ? Fuel.burningTemperature : 0;
	}

	public int addItems(int pos, int count, ItemStack stack2update) {
		count = Math.min(count, cachedBlockCount * MaxItemsPerBlock[pos] - itemCounts[pos]);
		itemCounts[pos] += count;
		items[pos].stackSize = Math.min(itemCounts[pos], ItemsPerInventorySlot);
		if (stack2update != null)
			stack2update.stackSize = items[pos].stackSize;
		return count;
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int pos) {
		if (pos < 0 || pos > 1 || itemCounts[pos] == 0) //
			return null;
		items[pos].stackSize = Math.min(itemCounts[pos], ItemsPerInventorySlot);
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

		int maxCount = cachedBlockCount * MaxItemsPerBlock[pos];
		int amount = Math.min(maxCount, itemstack.stackSize);

		itemCounts[pos] += amount;
		itemstack.stackSize -= amount;

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

		masterHeater.addItems(0, itemCounts[0], null);
		masterHeater.addItems(1, itemCounts[1], null);

		masterHeater.quantizeUIGaugeValues();
	}

	@Override
	protected void onStructureDismantle() {
		thermalEnergyContent = 0;

		List<TileEntityRectMultiblock> members = structureMembers(TerraIndustrialisBlocks.blockHeater.blockID);
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

			heater.cachedBlockCount = 1;
			heater.quantizeUIGaugeValues();
		}
	}

	@Override
	protected void structureCreatedWithThisAsMaster() {
		cachedBlockCount = structureBlockCount();
		scanForHeatables();
	}

	@Override
	public void updateEntity() {
		if (!isMaster()) {
			TileEntityHeater master = master();
			if (master != null)
				updateFireIcon(master);
			return;
		}

		if (worldObj.isRemote)
			return;

		long now = TFC_Time.getTotalTicks();
		float deltaTime = timeSinceLastUpdate(now);
		if (deltaTime == 0)
			return;

		float e = thermalEnergyContent;
		int a = itemCounts[AshesSlot];
		int f = itemCounts[FuelSlot];
		try {
			boolean onFire = burnFuelUpdate(deltaTime);
			updateFireIcon(this);

			if (thermalEnergyContent > 0) {
				if (lastScan == 0 || (now - lastScan) > HeatablesScanPeriod) {
					lastScan = now;
					scanForHeatables();
				}

				boolean ok = heatables != null && heatables.length > 0;
				if (ok)
					for (IHeatable target : heatables) {
						TileEntity entity = (TileEntity) target;
						if (entity.isInvalid() || (entity instanceof TileEntityRectMultiblock && !((TileEntityRectMultiblock) entity).isMaster())) {
							lastScan = now - HeatablesScanPeriod - 2;
							ok = false;
						}
					}

				if (ok) {
					float out = Thermodynamics.doEnergyTransfer(deltaTime, this, heatables, StructureMaterial);
					thermalEnergyContent -= out;
					quantizedOutput_Wh = (int) (deltaTime * out);
				} else
					quantizedOutput_Wh = 0;
			} else
				quantizedOutput_Wh = 0;

			thermalEnergyContent -= Thermodynamics.airEnergyAbsorption(deltaTime, Fuel.burningTemperature, cachedBlockCount / 5f, StructureMaterial, xCoord, zCoord);
			thermalEnergyContent = Math.min(cachedBlockCount * MaxEnergyPerBlock, thermalEnergyContent);
			thermalEnergyContent = Math.max(onFire ? .0001f : 0, thermalEnergyContent);
		} finally {
			if (e != thermalEnergyContent || a != itemCounts[AshesSlot] || f != itemCounts[FuelSlot]) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				onInventoryChanged();
				quantizeUIGaugeValues();
			}
		}
	}

	private float timeSinceLastUpdate(long now) {
		if (lastUpdate == 0) {
			lastUpdate = now;
			return 0;
		}

		long delta = now - lastUpdate;
		if (delta < UpdatePeriodTicks)
			return 0;

		lastUpdate = now;
		return delta * Units.SecondsPerTick;
	}

	private boolean isHeaterActive() {
		return thermalEnergyContent > 0 || burningFuelMass > 0 || (itemCounts[AshesSlot] < cachedBlockCount * AshesPerBlock && itemCounts[FuelSlot] > 0);
	}

	private boolean burnFuelUpdate(float deltaTime) {
		if (!turnedOn || itemCounts[AshesSlot] >= cachedBlockCount * AshesPerBlock)
			return false;

		float maxBurnAmount = cachedBlockCount * Fuel.amountBurning(deltaTime);
		if (burningFuelMass < maxBurnAmount && itemCounts[FuelSlot] > 0) {
			int maxItems = Math.max(1, (int) Math.floor((maxBurnAmount - burningFuelMass) / Fuel.kilosPerItem));
			int nItems = Math.min(maxItems, itemCounts[FuelSlot]);

			for (int i = 0; i < nItems; ++i)
				if (AshPropability > Math.random())
					if (!incrementAshes())
						nItems = i + 1;

			itemCounts[FuelSlot] -= nItems;
			burningFuelMass += nItems * Fuel.kilosPerItem;
		}

		float burnAmount = Math.min(burningFuelMass, maxBurnAmount);
		burningFuelMass -= burnAmount;

		float energy = Efficiency * Fuel.energyBurning(burnAmount);
		thermalEnergyContent += energy;
		if (energy == 0) // internal cooldown
			thermalEnergyContent *= .97f;

		return burnAmount > 0 && energy > 0;
	}

	private boolean incrementAshes() {
		if (itemCounts[AshesSlot] >= cachedBlockCount * AshesPerBlock)
			return false;
		++itemCounts[AshesSlot];
		return true;
	}

	private void updateFireIcon(TileEntityHeater heater) {
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int side = 7 & metadata;

		metadata = side | (heater.isHeaterActive() ? 8 : 0);
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 3);
	}

	private void scanForHeatables() {
		int[] min = { 0, 0, 0 };
		int[] max = { 0, 0, 0 };
		fetchStructureCoordinates(min, max);

		Set<IHeatable> heatables = new HashSet<>();
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

		this.heatables = heatables.toArray(new IHeatable[heatables.size()]);
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

		thermalEnergyContent = par1NBTTagCompound.getFloat("NRG");
		turnedOn = par1NBTTagCompound.hasKey("On") ? par1NBTTagCompound.getBoolean("On") : true;
		itemCounts[FuelSlot] = par1NBTTagCompound.getInteger("Fuel");
		itemCounts[AshesSlot] = par1NBTTagCompound.getInteger("Ashes");
		cachedBlockCount = par1NBTTagCompound.getInteger("Sz");

		quantizeUIGaugeValues();
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);

		if (!isMaster())
			return;

		par1NBTTagCompound.setFloat("NRG", thermalEnergyContent);
		par1NBTTagCompound.setBoolean("On", turnedOn);
		par1NBTTagCompound.setInteger("Fuel", itemCounts[FuelSlot]);
		par1NBTTagCompound.setInteger("Ashes", itemCounts[AshesSlot]);
		par1NBTTagCompound.setInteger("Sz", cachedBlockCount);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNBT(pkt.data);
		updateFireIcon(this);
	}

	private void quantizeUIGaugeValues() {
		quantizedEnergy = GUITools.quantize(thermalEnergyContent, cachedBlockCount * MaxEnergyPerBlock);
	}
}
