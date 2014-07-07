package com.noiz.ti.physics;

public enum FuelMaterial {
	//
	Charcoal(perHour2perSec(1), MJ(30), 500, .001f),
	//
	Coal(perHour2perSec(1), MJ(31), 600, .001f),
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

	static float MJ(float mj) {
		return mj * 1000000;
	}

	static float perHour2perSec(float rate) {
		return rate / 3600;
	}
}
