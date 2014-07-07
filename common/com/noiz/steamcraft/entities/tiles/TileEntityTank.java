package com.noiz.steamcraft.entities.tiles;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import TFC.Core.TFC_Time;

import com.noiz.steamcraft.SteamCraftBlocks;
import com.noiz.steamcraft.entities.tiles.multiblock.TileEntityRectMultiblock;
import com.noiz.steamcraft.handlers.client.GuiHandler;

public class TileEntityTank extends TileEntityRectMultiblock {

	public static final int PressureUpdatePeriodTicks = 40;
	public static final float TicksPerLitre = 40;

	public static final float LiquidPerBucket = 10f;
	public static final float CapacityPerBlock = 100f;

	public static final float MaxPressure = 1000f;
	public static final float PressureDecay = 10f;
	public static final float MinTemperatureBoiling = 100f;
	public static final float MaxTemperature = 500f;

	public static final float HeatTransferFactor = .04f;
	public static final float BoilAmountFactor = .005f;
	public static final float PressureAmountFactor = 20f;

	private float waterAmount = 0;
	private float pressure = 0;
	private float temperature = 0;
	private long pressureNextUpdate = 0;

	public int quantizedTemperature = 0;
	public int quantizedWater = 0;
	public int quantizedPressure = 0;

	private int[] heaterLocation = null;

	public boolean isFull() {
		return waterAmount >= structureBlockCount() * CapacityPerBlock;
	}

	public void addBucket() {
		waterAmount = Math.min(structureBlockCount() * CapacityPerBlock, waterAmount + LiquidPerBucket);

		quantizedWater = (int) (waterAmount * GuiHandler.GUI_GaugeScale / (structureBlockCount() * CapacityPerBlock));
		onInventoryChanged();
	}

	@Override
	protected void mergeThisMasterToNextOne(TileEntityRectMultiblock nextMaster) {
		TileEntityTank masterTank = (TileEntityTank) nextMaster;
		masterTank.waterAmount += waterAmount;

		masterTank.quantizeUIGaugeValues();
	}

	@Override
	protected void onStructureDismantle() {
		pressure = temperature = 0;

		List<TileEntityRectMultiblock> members = structureMembers(SteamCraftBlocks.blockTank.blockID);
		if (members.size() == 0)
			return;

		float waterPerMember = waterAmount / members.size();
		for (TileEntityRectMultiblock member : members) {
			TileEntityTank tank = (TileEntityTank) member;
			tank.waterAmount = waterPerMember;
			tank.temperature = 0;
			tank.pressure = 0;
			
			tank.quantizeUIGaugeValues();
		}
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote)
			return;

		if (TFC_Time.getTotalTicks() < pressureNextUpdate)
			return;
		pressureNextUpdate = TFC_Time.getTotalTicks() + PressureUpdatePeriodTicks;

		final float p = pressure;
		final float w = waterAmount;
		final float t = temperature;

		try {
			pressure = Math.max(0, pressure - PressureDecay);
			waterAmount = Math.max(0, waterAmount);
			if (waterAmount < 1)
				return;

			if (heaterLocation == null)
				detectHeater();

			TileEntityHeater heater = null;
			if (heaterLocation != null) {
				TileEntity entity = worldObj.getBlockTileEntity(heaterLocation[0], heaterLocation[1], heaterLocation[2]);
				if (entity == null || !(entity instanceof TileEntityHeater)) {
					heaterLocation = null;
					return;
				}
				heater = (TileEntityHeater) entity;
			}

			if (heater == null)
				return;

			float deltaT = (heater.temperature() - temperature) * HeatTransferFactor;
			temperature = Math.max(0, Math.min(MaxTemperature, temperature + deltaT));

			float maxWater = temperature > MinTemperatureBoiling ? BoilAmountFactor * temperature : 0;
			float delta = Math.min(waterAmount, maxWater);
			waterAmount -= delta;
			pressure = Math.min(MaxPressure, pressure + delta * PressureAmountFactor);
		} finally {
			if (p != pressure || w != waterAmount || t != temperature) {
				quantizeUIGaugeValues();
				onInventoryChanged();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
	}

	private void detectHeater() {
		int id = worldObj.getBlockId(xCoord, yCoord - 1, zCoord);
		if (id == SteamCraftBlocks.blockHeater.blockID)
			heaterLocation = new int[] { xCoord, yCoord - 1, zCoord };
		else
			heaterLocation = null;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);

		if (!isMaster())
			return;

		waterAmount = par1nbtTagCompound.getFloat("Water");
		pressure = par1nbtTagCompound.getFloat("Pressure");
		temperature = par1nbtTagCompound.getFloat("Temperature");

		boolean hasHeater = par1nbtTagCompound.getBoolean("HasHeater");
		if (hasHeater)
			heaterLocation = par1nbtTagCompound.getIntArray("HeaterLoc");
		else
			heaterLocation = null;

		quantizeUIGaugeValues();
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);

		if (!isMaster())
			return;

		par1nbtTagCompound.setFloat("Water", waterAmount);
		par1nbtTagCompound.setFloat("Pressure", pressure);
		par1nbtTagCompound.setFloat("Temperature", temperature);

		par1nbtTagCompound.setBoolean("HasHeater", heaterLocation != null);
		if (heaterLocation != null)
			par1nbtTagCompound.setIntArray("HeaterLoc", heaterLocation);
	}

	private void quantizeUIGaugeValues() {
		quantizedTemperature = (int) (temperature * GuiHandler.GUI_GaugeScale / MaxTemperature);
		quantizedWater = (int) (waterAmount * GuiHandler.GUI_GaugeScale / (structureBlockCount() * CapacityPerBlock));
		quantizedPressure = (int) (pressure * GuiHandler.GUI_GaugeScale / MaxPressure);
	}
}
