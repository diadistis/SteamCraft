package com.noiz.steamcraft.entities.tiles;

import com.noiz.steamcraft.handlers.client.GuiHandler;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import TFC.Core.TFC_Time;

public class TileEntityTank extends TileEntity {

	public static final int PressureUpdatePeriodTicks = 40;
	public static final float TicksPerLitre = 40;

	public static final float LiquidPerBucket = 10f;
	public static final float CapacityPerBlock = 100f;

	public static final float MaxPressure = 1000f;
	public static final float PressureDecay = 5f;
	public static final float MinTemperatureBoiling = 100f;
	public static final float MaxTemperature = 500f;

	public static final float HeatTransferFactor = .01f;
	public static final float BoilAmountFactor = 0;
	public static final float PressureAmountFactor = 0;

	private int blockCount = 1;
	private float waterAmount = 0;
	private float pressure = 0;
	private float temperature = 0;
	private long pressureNextUpdate = 0;

	public int quantizedTemperature = 0;
	public int quantizedWater = 0;
	public int quantizedPressure = 0;

	private int[] heaterLocation = null;

	public boolean isFull() {
		return waterAmount >= blockCount * CapacityPerBlock;
	}

	public void addBucket() {
		waterAmount = Math.min(blockCount * CapacityPerBlock, waterAmount + LiquidPerBucket);

		quantizedWater = (int) (waterAmount * GuiHandler.GUI_GaugeScale / (blockCount * CapacityPerBlock));
		onInventoryChanged();
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

			TileEntityHeater heater = null;
			if (heaterLocation != null)
				heater = (TileEntityHeater) worldObj.getBlockTileEntity(heaterLocation[0], heaterLocation[1], heaterLocation[2]);

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
				quantizedTemperature = (int) (temperature * GuiHandler.GUI_GaugeScale / MaxTemperature);
				quantizedWater = (int) (waterAmount * GuiHandler.GUI_GaugeScale / (blockCount * CapacityPerBlock));
				quantizedPressure = (int) (pressure * GuiHandler.GUI_GaugeScale / MaxPressure);

				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				onInventoryChanged();
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);

		blockCount = par1nbtTagCompound.getInteger("BlockCount");
		waterAmount = par1nbtTagCompound.getFloat("Water");
		pressure = par1nbtTagCompound.getFloat("Pressure");
		temperature = par1nbtTagCompound.getFloat("Temperature");

		boolean hasHeater = par1nbtTagCompound.getBoolean("HasHeater");
		if (hasHeater)
			heaterLocation = par1nbtTagCompound.getIntArray("HeaterLoc");
		else
			heaterLocation = null;

		quantizedTemperature = (int) (temperature * GuiHandler.GUI_GaugeScale / MaxTemperature);
		quantizedWater = (int) (waterAmount * GuiHandler.GUI_GaugeScale / (blockCount * CapacityPerBlock));
		quantizedPressure = (int) (pressure * GuiHandler.GUI_GaugeScale / MaxPressure);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setInteger("BlockCount", blockCount);
		par1nbtTagCompound.setFloat("Water", waterAmount);
		par1nbtTagCompound.setFloat("Pressure", pressure);
		par1nbtTagCompound.setFloat("Temperature", temperature);

		par1nbtTagCompound.setBoolean("HasHeater", heaterLocation != null);
		if (heaterLocation != null)
			par1nbtTagCompound.setIntArray("HeaterLoc", heaterLocation);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tagCompound);
	}
}
