package com.noiz.ti.physics;

public interface IHeatable {
	public void doHeatTransfer(float energy);

	public float temperature();

	public int area(IHeatSource touchingSurface);
}
