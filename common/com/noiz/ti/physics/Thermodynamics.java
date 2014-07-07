package com.noiz.ti.physics;

import TFC.Core.TFC_Climate;

public class Thermodynamics {
	// Reference:
	// http://en.wikipedia.org/wiki/Newton%27s_law_of_cooling#Newton.27s_law_of_cooling

	public static float airEnergyAbsorption(float time, float temperature, float area, SolidMaterial material, int x, int z) {
		return .1f * material.thermalConductivity * area * (temperature - TFC_Climate.getBioTemperature(x, z)) * time;
	}

	public static float doEnergyTransfer(float deltaTime, IHeatSource source, IHeatable[] heatables, SolidMaterial conductor) {
		if (heatables.length == 0)
			return 0;

		float[] transfers = new float[heatables.length];
		float totalTransfer = 0;

		float srcTemp = source.temperature();
		for (int i = 0; i < heatables.length; ++i) {
			transfers[i] = deltaTime * conductor.thermalConductivity * .5f * heatables[i].area(source) * (srcTemp - heatables[i].temperature());
			totalTransfer += Math.max(0, transfers[i]);
		}
		totalTransfer /= heatables.length;

		if (totalTransfer > source.energy()) {
			float ratio = source.energy() / totalTransfer;
			for (int i = 0; i < heatables.length; ++i)
				if (transfers[i] > 0)
					transfers[i] *= ratio;
			totalTransfer = source.energy();
		}

		for (int i = 0; i < heatables.length; ++i)
			heatables[i].doHeatTransfer(transfers[i]);

		return totalTransfer;
	}
}
