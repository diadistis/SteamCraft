package com.noiz.ti.physics;

public enum SolidMaterial {
	//
	Steel(1),
	//
	StainlessSteel(1),
	//
	;

	public final float thermalConductivity;

	private SolidMaterial(float thermalConductivity) {
		this.thermalConductivity = thermalConductivity;
	}
}
