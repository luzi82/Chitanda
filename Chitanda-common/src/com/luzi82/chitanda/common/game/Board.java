package com.luzi82.chitanda.common.game;

public class Board {

	static public final int UPDATE_BLOCK_SIZE = 16;

	public static final int WIDTH = 2048 * 4;
	public static final int HEIGHT = 1024 * 4;

	public static final int CELL0_COUNT = WIDTH * HEIGHT;
	public static final int DATA0_SIZE = CELL0_COUNT / Byte.SIZE;
	public static final int YSTEP0 = WIDTH / Byte.SIZE;
	protected final byte[] mData0 = new byte[DATA0_SIZE];

	public static final int CELL1_COUNT = CELL0_COUNT / 16;
	public static final int DATA1_SIZE = CELL1_COUNT * 4 / Byte.SIZE;
	public static final int YSTEP1 = (WIDTH >> 2) * 4 / Byte.SIZE;
	protected final byte[] mData1 = new byte[DATA1_SIZE];

	public static final int CELL2_COUNT = CELL0_COUNT / 256;
	public static final int DATA2_SIZE = CELL2_COUNT;
	public static final int YSTEP2 = WIDTH >> 4;
	protected final byte[] mData2 = new byte[DATA2_SIZE];

	protected int mVersion = 0;

	public boolean get0(int aX, int aY) {
		int i0 = xyToIndex0(aX, aY);
		int o0 = xyToOffset0(aX, aY);
		return (mData0[i0] & (1 << o0)) != 0;
	}

	public int get1(int aX1, int aY1) {
		int i1 = xyToIndex1(aX1, aY1);
		int o1 = xyToOffset1(aX1, aY1);
		return (mData1[i1] >> o1) & 0xf;
	}

	public int get2(int aX2, int aY2) {
		int i2 = xyToIndex2(aX2, aY2);
		return ((int) (mData2[i2])) & 0xff;
	}

	public boolean set(int aX, int aY, boolean aV) {
		int i0 = xyToIndex0(aX, aY);
		int o0 = xyToOffset0(aX, aY);
		boolean v = (mData0[i0] & (1 << o0)) != 0;
		if (v == aV)
			return false;
		int i1 = xyToIndex1(aX >> 2, aY >> 2);
		int o1 = xyToOffset1(aX >> 2, aY >> 2);
		int i2 = xyToIndex2(aX >> 4, aY >> 4);
		byte d1 = mData1[i1];
		int v1 = (d1 >> o1) & 0xf;
		int v2 = mData2[i2];
		int vd = aV ? 1 : -1;
		if (aV) {
			mData0[i0] |= 1 << o0;
		} else {
			mData0[i0] &= ~(1 << o0);
		}
		if (v1 == 7) {
			v1 = layerTotal1(xyToIndex0(aX, aY & (~3)), o0 & (~3));
			v1 -= (v1 >> 3);
		} else {
			v1 += vd;
		}
		if (v2 == 0x7f) {
			v2 = layerTotal2(xyToIndex0(aX & (~0xf), aY & (~0xf)));
			v2 -= (v2 >> 7);
		} else {
			v2 += vd;
		}
		d1 &= ~(0xf << o1);
		d1 |= v1 << o1;
		mData1[i1] = d1;
		mData2[i2] = (byte) v2;
		++mVersion;
		return true;
	}

	public boolean update(int aX0, int aY0, byte[] aData) {
		boolean ret = false;
		final int i0 = xyToIndex0(aX0, aY0);
		int ii0 = i0;
		int iii0;
		int di = 0;
		byte d, dd, ddd;
		for (int y0 = 0; y0 < UPDATE_BLOCK_SIZE; ++y0) {
			iii0 = ii0;
			for (int xi0 = 0; xi0 < 2; ++xi0) {
				dd = aData[di];
				d = mData0[iii0];
				ddd = (byte) (d & dd);
				if (ddd != d) {
					mData0[iii0] = ddd;
					ret = true;
				}
				++iii0;
				++di;
			}
			ii0 += YSTEP0;
		}
		if (ret) {
			ii0 = i0;
			int i1 = xyToIndex1(aX0 >> 2, aY0 >> 2);
			int ii1;
			byte d1;
			int v1;
			for (int y1 = 0; y1 < 4; ++y1) {
				ii1 = i1;
				iii0 = ii0;
				for (int xi1 = 0; xi1 < 2; ++xi1) {
					d1 = 0;
					for (int xo1 = 0; xo1 < 8; xo1 += 4) {
						v1 = layerTotal1(iii0, xo1);
						v1 -= (v1 >> 3);
						d1 |= v1 << xo1;
					}
					mData1[ii1] = d1;
					++ii1;
					++iii0;
				}
				i1 += YSTEP1;
				ii0 += YSTEP0 << 2;
			}
			int i2 = xyToIndex2(aX0 >> 4, aY0 >> 4);
			int v2;
			v2 = layerTotal2(i0);
			v2 -= (v2 >> 7);
			mData2[i2] = (byte) v2;
			++mVersion;
		}
		return ret;
	}

	public void setAll(boolean aValue) {
		byte v = (byte) (aValue ? 0xff : 0x00);
		for (int i = 0; i < DATA0_SIZE; ++i) {
			mData0[i] = v;
		}
		for (int i = 0; i < DATA1_SIZE; ++i) {
			mData1[i] = v;
		}
		for (int i = 0; i < DATA2_SIZE; ++i) {
			mData2[i] = v;
		}
		++mVersion;
	}

	public static int xyToIndex0(int aX, int aY) {
		return (aX + aY * WIDTH) / Byte.SIZE;
	}

	public static int xyToOffset0(int aX, int aY) {
		return (aX + aY * WIDTH) % Byte.SIZE;
	}

	public static int xyToIndex1(int aX1, int aY1) {
		return (aX1 + aY1 * (WIDTH / 4)) / 2;
	}

	public static int xyToOffset1(int aX1, int aY1) {
		return ((aX1 + aY1 * (WIDTH / 4)) & 1) * 4;
	}

	public static int xyToIndex2(int aX2, int aY2) {
		return aX2 + aY2 * (WIDTH / 16);
	}

	public int layerTotal1(int aI0, int aO0) {
		int ret = 0;
		byte d;
		for (int y = 0; y < 4; ++y) {
			d = mData0[aI0];
			d >>= aO0;
			for (int x = 0; x < 4; ++x) {
				if ((d & 1) == 1)
					++ret;
				d >>= 1;
			}
			aI0 += YSTEP0;
		}
		return ret;
	}

	public int layerTotal2(int aI0) {
		int ret = 0;
		int ii0;
		byte d;
		for (int y = 0; y < 16; ++y) {
			ii0 = aI0;
			for (int xi = 0; xi < 2; ++xi) {
				d = mData0[ii0];
				for (int xo = 0; xo < Byte.SIZE; ++xo) {
					if ((d & 1) == 1)
						++ret;
					d >>= 1;
				}
				++ii0;
			}
			aI0 += YSTEP0;
		}
		return ret;
	}

	public int getVersion() {
		return mVersion;
	}

	@SuppressWarnings("all")
	private static void check() {
		if ((WIDTH % 16) != 0)
			throw new RuntimeException();
		if ((HEIGHT % 16) != 0)
			throw new RuntimeException();
	}

	static {
		check();
	}

}
