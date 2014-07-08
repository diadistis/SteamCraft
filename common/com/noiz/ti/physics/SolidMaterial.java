package com.noiz.ti.physics;

public enum SolidMaterial {
	//
	Steel(8),
	//
	StainlessSteel(10),
	//
	;

	public final float thermalConductivity;

	private SolidMaterial(float thermalConductivity) {
		this.thermalConductivity = thermalConductivity;
	}
}
