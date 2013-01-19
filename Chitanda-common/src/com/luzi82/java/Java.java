package com.luzi82.java;

import java.util.Arrays;

public class Java {

	private static int mStackTraceLevel = -1;

	private static int getStackTraceLevel() {
		if (mStackTraceLevel != -1)
			return mStackTraceLevel;
		StackTraceElement[] stev = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stev.length; ++i) {
			if (stev[i].getMethodName() == "getStackTraceLevel") {
				mStackTraceLevel = i;
				return mStackTraceLevel;
			}
		}
		throw new Error();
	}

	public static StackTraceElement[] getStackTrace() {
		return getStackTrace(1);
	}

	public static StackTraceElement[] getStackTrace(int aLevel) {
		int level = getStackTraceLevel();
		StackTraceElement[] ary = Thread.currentThread().getStackTrace();
		StackTraceElement[] ret = Arrays.copyOfRange(ary, level + 1 + aLevel, ary.length);
		return ret;
	}
	
	public static String mark(){
		StackTraceElement[] stev=getStackTrace(1);
		StackTraceElement ste=stev[0];
		return (ste.getFileName()+"."+ste.getLineNumber());
	}

}
