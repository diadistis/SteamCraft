package com.noiz.ti.entities.tiles;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import TFC.Core.TFC_Time;

import com.noiz.ti.TerraIndustrialisBlocks;
import com.noiz.ti.entities.tiles.multiblock.TileEntityRectMultiblock;
import com.noiz.ti.handlers.client.GuiHandler;
import com.noiz.ti.physics.IHeatSource;
import com.noiz.ti.physics.IHeatable;
import com.noiz.ti.physics.SolidMaterial;
import com.noiz.ti.physics.Thermal;

public class TileEntityTank extends TileEntityRectMultiblock implements IHeatable {

	public static final int UpdatePeriodTicks = 40;
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
	private float deltaT = 0;
	private long nextUpdate = 0;

	public int quantizedTemperature = 0;
	public int quantizedWater = 0;
	public int quantizedPressure = 0;

	public int capacity() {
		return (int) (structureBlockCount() * CapacityPerBlock);
	}

	@Override
	public void setTemperatureAfterHeatTransfer(float temperature) {
		this.temperature = temperature;
		quantizeUIGaugeValues();
	}

	@Override
	public float temperature() {
		return temperature;
	}

	@Override
	public int area(IHeatSource touchingSurface) {
		return touchingHorizontalArea((TileEntityRectMultiblock) touchingSurface);
	}

	public String status() {
		if (temperature > MinTemperatureBoiling)
			return waterAmount > 0 ? "Boiling" : "Heating";
		return "Idle";
	}

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

		List<TileEntityRectMultiblock> members = structureMembers(TerraIndustrialisBlocks.blockTank.blockID);
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
	protected void structureCreatedWithThisAsMaster() {
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote || !isMaster())
			return;

		if (TFC_Time.getTotalTicks() < nextUpdate)
			return;
		nextUpdate = TFC_Time.getTotalTicks() + UpdatePeriodTicks;

		final float p = pressure;
		final float w = waterAmount;
		final float t = temperature;

		try {
			pressure = Math.max(0, pressure - PressureDecay);
			waterAmount = Math.max(0, waterAmount);

			temperature = Math.max(0, Math.min(MaxTemperature, temperature + deltaT));
			deltaT = Thermal.airEnergyAbsorption(.02f, temperature, structureBlockCount(), SolidMaterial.Steel, xCoord, zCoord);

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

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);

		if (!isMaster())
			return;

		waterAmount = par1nbtTagCompound.getFloat("Water");
		pressure = par1nbtTagCompound.getFloat("Pressure");
		temperature = par1nbtTagCompound.getFloat("Temperature");

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
	}

	private void quantizeUIGaugeValues() {
		quantizedTemperature = (int) (temperature * GuiHandler.GUI_GaugeScale / MaxTemperature);
		quantizedWater = (int) (waterAmount * GuiHandler.GUI_GaugeScale / (structureBlockCount() * CapacityPerBlock));
		quantizedPressure = (int) (pressure * GuiHandler.GUI_GaugeScale / MaxPressure);
	}
}
