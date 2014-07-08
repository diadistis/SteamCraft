package com.noiz.ti.physics;

public class MathUtil {
	private static final double natLog_2 = Math.log(2);

	public static final double log2(double value) {
		return Math.log(value) / natLog_2;
	}
}
