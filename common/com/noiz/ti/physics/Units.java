package com.noiz.ti.physics;

public class Units {

	// http://minecraft.gamepedia.com/Day-night_cycle
	public static final float SecondsPerTick = 50 / 1000f;

	public static float joule2btu(float joules) {
		// http://en.wikipedia.org/wiki/British_thermal_unit
		return joules / 1055.056f;
	}

	public static float pascal2atmosphere(float pascal) {
		return pascal / 101325f;
	}

	public static float pascal2psi(float pascal) {
		return pascal / 6894.757f;
	}

	public static float psi2pascal(float psi) {
		return psi * 6894.757f;
	}

	public static float Mega(float mj) {
		return mj * 1000000;
	}

	public static float Kilo(float mj) {
		return mj * 1000;
	}
}
