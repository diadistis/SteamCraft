package com.noiz.ti.physics;

public enum FuelMaterial {
	//
	Charcoal(.05f, Units.Mega(30), 350, 1f),
	//
	Coal(.05f, Units.Mega(31), 400, 1f),
	//
	;

	// mass / time ~ kg / sec
	public final float burnSpeed;
	// energy / mass ~ joule / kg
	public final float burningEnergyContent;
	public final float burningTemperature;
	public final float kilosPerItem;

	private FuelMaterial(float burnSpeed, float burningEnergyContent, float burningTemperature, float kilosPerItem) {
		this.burnSpeed = burnSpeed;
		this.burningEnergyContent = burningEnergyContent;
		this.burningTemperature = burningTemperature;
		this.kilosPerItem = kilosPerItem;
	}

	public float amountBurning(float seconds) {
		return burnSpeed * seconds;
	}

	public float energyBurning(float kilos) {
		return burningEnergyContent * kilos;
	}
}
