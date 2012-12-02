package com.luzi82.chitanda;

public class Const {

	public static final float SMOOTH_REDUCE = 1f / 256;
	public static final float DIV_LN_SMOOTH_REDUCE = (float) (1 / Math.log(SMOOTH_REDUCE));
	public static final float PHI = (float) (1 + Math.sqrt(5)) / 2;
	
	public static int minMax(int aMin, int aV, int aMax) {
		aV = Math.max(aMin, aV);
		aV = Math.min(aMax, aV);
		return aV;
	}

}
