package com.noiz.ti.physics;

public enum SolidMaterial {
	//
	Steel(45, 2),
	//
	StainlessSteel(18, 2),
	//
	;

	public final float internalThermalConductivity;
	public final float thermalConductivityToAir;

	private SolidMaterial(float internalThermalConductivity, float thermalConductivityToAir) {
		this.internalThermalConductivity = internalThermalConductivity;
		this.thermalConductivityToAir = thermalConductivityToAir;
	}
}
