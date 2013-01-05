package com.luzi82.java;

import java.util.Arrays;

public class Java {

	private static int mStackTraceElementLevel = -1;

	private static int getStackTraceElementLevel() {
		if (mStackTraceElementLevel != -1)
			return mStackTraceElementLevel;
		StackTraceElement[] stev = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stev.length; ++i) {
			if (stev[i].getMethodName() == "getStackTraceElementLevel") {
				mStackTraceElementLevel = i;
				return mStackTraceElementLevel;
			}
		}
		throw new Error();
	}

	public static StackTraceElement[] getStackTraceElements() {
		return getStackTraceElements(1);
	}

	public static StackTraceElement[] getStackTraceElements(int aLevel) {
		int level = getStackTraceElementLevel();
		StackTraceElement[] ary = Thread.currentThread().getStackTrace();
		StackTraceElement[] ret = Arrays.copyOfRange(ary, level + 1 + aLevel, ary.length);
		return ret;
	}

}
