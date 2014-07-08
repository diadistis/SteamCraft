package com.noiz.ti.entities.tiles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import TFC.Core.TFC_Climate;
import TFC.Core.TFC_Time;

import com.noiz.ti.TerraIndustrialisBlocks;
import com.noiz.ti.entities.tiles.multiblock.TileEntityRectMultiblock;
import com.noiz.ti.gui.GUITools;
import com.noiz.ti.physics.IHeatSource;
import com.noiz.ti.physics.IHeatable;
import com.noiz.ti.physics.SolidMaterial;
import com.noiz.ti.physics.Thermodynamics;
import com.noiz.ti.physics.Units;
import com.noiz.ti.physics.Water;

public class TileEntityTank extends TileEntityRectMultiblock implements IHeatable {

	public static final boolean TanksExplode = true;
	public static final int UpdatePeriodTicks = 40;

	public static final float LiquidPerBucket = 10f;
	public static final float CapacityPerBlock = 100f;

	public static final float MaxPressure = Units.psi2pascal(1000);
	private static final float MaxTemperature = Water.vaporTemperature(MaxPressure);

	public static final float LossAreaCoefficient = .01f;

	private float waterAmount = 0;
	private float temperature = 0;
	private float realTemperatureTarget = 0;
	// amount of energy taken since last update (paired w/ the associated
	// maximum temperature).
	private final List<float[]> influx = new ArrayList<>();

	private long lastUpdate = 0;

	public int quantizedTemperature = 0;
	public int quantizedWater = 0;

	public float pressure() {
		return Water.vaporPressure(temperature);
	}

	public int capacity() {
		return (int) (structureBlockCount() * CapacityPerBlock);
	}

	public int water() {
		return (int) waterAmount;
	}

	@Override
	public void doHeatTransfer(float energy, float maxTemperature) {
		influx.add(new float[] { energy, maxTemperature });
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
		if (temperature > 100f)
			return waterAmount > 0 ? "Boiling" : "Heating";
		return (waterAmount > 0 && temperature > 40) ? "Heating" : "Idle";
	}

	public boolean isFull() {
		return waterAmount >= structureBlockCount() * CapacityPerBlock;
	}

	public float addWater(float amount) {
		float delta = Math.min(structureBlockCount() * CapacityPerBlock - waterAmount, amount);
		if (delta == 0)
			return 0;

		float existingMassSpecific = waterAmount * Water.SpecificHeat;
		float addedMassSpecific = delta * Water.SpecificHeat;

		temperature = (existingMassSpecific * temperature + addedMassSpecific * TFC_Climate.getBioTemperature(xCoord, zCoord)) / (existingMassSpecific + addedMassSpecific);
		waterAmount += delta;

		quantizeUIGaugeValues();
		onInventoryChanged();
		return delta;
	}

	@Override
	protected void mergeThisMasterToNextOne(TileEntityRectMultiblock nextMaster) {
		TileEntityTank masterTank = (TileEntityTank) nextMaster;
		masterTank.waterAmount += waterAmount;

		masterTank.quantizeUIGaugeValues();
	}

	@Override
	protected void onStructureDismantle() {
		temperature = 0;

		List<TileEntityRectMultiblock> members = structureMembers(TerraIndustrialisBlocks.blockTank.blockID);
		if (members.size() == 0)
			return;

		float waterPerMember = waterAmount / members.size();
		for (TileEntityRectMultiblock member : members) {
			TileEntityTank tank = (TileEntityTank) member;
			tank.waterAmount = waterPerMember;
			tank.temperature = 0;

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

		long now = TFC_Time.getTotalTicks();
		float deltaTime = timeSinceLastUpdate(now);
		if (deltaTime == 0)
			return;

		final float w = waterAmount;
		final float t = temperature;
		try {
			float minTemperature = TFC_Climate.getBioTemperature(xCoord, zCoord);

			if (waterAmount > 0) {
				for (float[] et : influx)
					if (temperature < et[1]) {
						float delta = et[0] / (waterAmount * Water.SpecificHeat);
						if (realTemperatureTarget < 0) {
							if (delta < -realTemperatureTarget) {
								realTemperatureTarget += delta;
								delta = 0;
							} else {
								delta += realTemperatureTarget;
								realTemperatureTarget = 0;
							}
						}
						if (delta > 0)
							temperature = Math.min(et[1], temperature + delta);
					}
			}
			influx.clear();

			float area = LossAreaCoefficient * structureBlockCount();
			temperature -= Thermodynamics.airEnergyAbsorption(deltaTime, temperature, area, SolidMaterial.Steel, xCoord, zCoord);
			if (temperature < 0) //
				realTemperatureTarget = temperature;
			else
				realTemperatureTarget = 0;
			temperature = Math.max(minTemperature, temperature);

			if (TanksExplode && pressure() > MaxPressure) {
				int[] center = centerCoordinates();
				worldObj.createExplosion(null, (double) center[0], (double) center[1], (double) center[2], 1 + 1.2f * structureRadius(), true);
			}
		} finally {
			if (w != waterAmount || t != temperature) {
				quantizeUIGaugeValues();
				onInventoryChanged();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);

		if (!isMaster())
			return;

		waterAmount = par1nbtTagCompound.getFloat("Water");
		temperature = par1nbtTagCompound.getFloat("Temperature");

		quantizeUIGaugeValues();
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);

		if (!isMaster())
			return;

		par1nbtTagCompound.setFloat("Water", waterAmount);
		par1nbtTagCompound.setFloat("Temperature", temperature);
	}

	private void quantizeUIGaugeValues() {
		quantizedTemperature = GUITools.quantize(temperature, MaxTemperature);
		quantizedWater = GUITools.quantize(waterAmount, (structureBlockCount() * CapacityPerBlock));
	}
}
