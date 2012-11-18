package com.luzi82.chitanda.game.logic;

public class Board {

	public static final int WIDTH = 2048 * 4;
	public static final int HEIGHT = 1024 * 4;
	public static final int TOTAL_CELL = WIDTH * HEIGHT;
	public static final int DATA_SIZE = (TOTAL_CELL + (Byte.SIZE - 1)) / Byte.SIZE;

	private final byte[] mData = new byte[DATA_SIZE];

	public boolean get(int aX, int aY) {
		int index = xyToIndex(aX, aY);
		int offset = xyToOffset(aX, aY);
		return (mData[index] & (1 << offset)) != 0;
	}

	public void set(int aX, int aY, boolean aV) {
		int index = xyToIndex(aX, aY);
		int offset = xyToOffset(aX, aY);
		if (aV) {
			mData[index] |= 1 << offset;
		} else {
			mData[index] &= ~(1 << offset);
		}
	}

	public void setAll(boolean aValue) {
		byte v = (byte) (aValue ? 0xff : 0x00);
		for (int i = 0; i < DATA_SIZE; ++i) {
			mData[i] = v;
		}
	}

	private static int xyToIndex(int aX, int aY) {
		return (aX + aY * WIDTH) / Byte.SIZE;
	}

	private static int xyToOffset(int aX, int aY) {
		return (aX + aY * WIDTH) % Byte.SIZE;
	}

}
