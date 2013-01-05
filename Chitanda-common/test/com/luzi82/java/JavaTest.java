package com.luzi82.java;

import org.junit.Assert;
import org.junit.Test;

public class JavaTest {

	@Test
	public void testGetStackTraceElements() {
		StackTraceElement[] ret=Java.getStackTraceElements();
		Assert.assertEquals("testGetStackTraceElements", ret[0].getMethodName());
	}

}
