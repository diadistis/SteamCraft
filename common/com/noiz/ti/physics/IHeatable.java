package com.noiz.ti.physics;

public interface IHeatable {
	public void doHeatTransfer(float energy, float maxTemperature);

	public float temperature();

	public int area(IHeatSource touchingSurface);
}
