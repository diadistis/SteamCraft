package com.noiz.ti.gui;

public class GUITools {

	public static final int GUI_GaugeScale = 49;
	public static final String[] UnitPrefices = { "", "k", "M", "G" };

	public static int quantize(float value, float max) {
		return (int) (Math.min(max, value) * GUITools.GUI_GaugeScale / max);
	}
}
