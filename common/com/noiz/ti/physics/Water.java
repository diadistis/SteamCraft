package com.noiz.ti.physics;

public class Water {
	public static final float SpecificHeat = 4.19f;

	// http://en.wikipedia.org/wiki/Antoine_equation
	// http://en.wikipedia.org/wiki/MmHg

	private static final float A = 8.14019f;
	private static final float B = 1810.94f;
	private static final float C = 244.485f;

	public static float vaporPressure(float temperature) {

		float mmHg = (float) Math.pow(10, A - B / (C + temperature));
		return 133.322387415f * mmHg;
	}

	public static float vaporTemperature(float pressure) {
		pressure /= 133.322387415f;

		return (float) (-C + B / (A - Math.log10(pressure)));
	}
}
