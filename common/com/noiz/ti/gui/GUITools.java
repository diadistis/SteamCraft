package com.noiz.ti.gui;

public class GUITools {

	public static final int GUI_GaugeScale = 49;
	public static final String[] UnitPrefices = { "", "k", "M", "G" };

	public static int quantize(float value, float max) {
		return (int) (Math.min(max, value) * GUI_GaugeScale / max);
	}

	public static float reverseQuantize(int quantized, float max) {
		return quantized * max / GUI_GaugeScale;
	}
}
