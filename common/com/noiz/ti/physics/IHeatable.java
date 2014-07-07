package com.noiz.ti.physics;

public interface IHeatable {
	public void setTemperatureAfterHeatTransfer(float temperature);

	public float temperature();

	public int area(IHeatSource touchingSurface);
}
