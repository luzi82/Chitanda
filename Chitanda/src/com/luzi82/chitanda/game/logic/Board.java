package com.luzi82.chitanda.game.logic;

import java.nio.ByteBuffer;

import com.badlogic.gdx.graphics.Pixmap;

public class Board {

	public static final int WIDTH = 2048 * 4;
	public static final int HEIGHT = 1024 * 4;

	public static final int CELL0_COUNT = WIDTH * HEIGHT;
	public static final int DATA0_SIZE = CELL0_COUNT / Byte.SIZE;
	public static final int YSTEP0 = WIDTH / Byte.SIZE;
	private final byte[] mData0 = new byte[DATA0_SIZE];

	public static final int CELL1_COUNT = CELL0_COUNT / 16;
	public static final int DATA1_SIZE = CELL1_COUNT * 4 / Byte.SIZE;
	public static final int YSTEP1 = (WIDTH >> 2) * 4 / Byte.SIZE;
	private final byte[] mData1 = new byte[DATA1_SIZE];

	public static final int CELL2_COUNT = CELL0_COUNT / 256;
	public static final int DATA2_SIZE = CELL2_COUNT;
	public static final int YSTEP2 = WIDTH >> 4;
	private final byte[] mData2 = new byte[DATA2_SIZE];

	private int mVersion = 0;

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

	public void set(int aX, int aY, boolean aV) {
		int i0 = xyToIndex0(aX, aY);
		int o0 = xyToOffset0(aX, aY);
		boolean v = (mData0[i0] & (1 << o0)) != 0;
		if (v == aV)
			return;
		int i1 = xyToIndex1(aX >> 2, aY >> 2);
		int o1 = xyToOffset1(aX >> 2, aY >> 2);
		int i2 = xyToIndex2(aX >> 4, aY >> 4);
		byte d1 = mData1[i1];
		int v1 = (d1 >> o1) & 0xf;
		int v2 = mData2[i2];
		if (aV) {
			mData0[i0] |= 1 << o0;

			if (v1 == 7) {
				v1 = layerTotal1(xyToIndex0(aX, aY & (~3)), o0 & (~3));
				v1 -= (v1 >> 3);
			} else {
				++v1;
			}

			if (v2 == 0x7f) {
				v2 = layerTotal2(xyToIndex0(aX & (~0xf), aY & (~0xf)));
				v2 -= (v2 >> 7);
			} else {
				++v2;
			}
		} else {
			mData0[i0] &= ~(1 << o0);

			if (v1 == 7) {
				v1 = layerTotal1(xyToIndex0(aX, aY & (~3)), o0 & (~3));
				v1 -= (v1 >> 3);
			} else {
				--v1;
			}

			if (v2 == 0x7f) {
				v2 = layerTotal2(xyToIndex0(aX & (~0xf), aY & (~0xf)));
				v2 -= (v2 >> 7);
			} else {
				--v2;
			}
		}
		d1 &= ~(0xf << o1);
		d1 |= v1 << o1;
		mData1[i1] = d1;
		mData2[i2] = (byte) v2;
		++mVersion;
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

	static byte[] BB = new byte[64 * 64 * 4];

	public void writePixmap0(Pixmap aPixmap, int aCX0, int aCY0) {
		byte c = (byte) ((((aCX0 + aCY0) & 1) == 0) ? 0x00 : 0x0f);

		ByteBuffer bb = aPixmap.getPixels();
		bb.rewind();

		int offset0 = xyToIndex0(aCX0 << 6, aCY0 << 6);
		int out = 0;

		byte b;
		for (int i = 0; i < 64; ++i) {
			int j0 = offset0;
			int j1 = offset0 + 8;
			for (int j = j0; j < j1; ++j) {
				b = mData0[j];
				for (int k = 0; k < Byte.SIZE; ++k) {
					BB[out++] = c;
					BB[out++] = c;
					BB[out++] = c;
					if ((b & 1) == 1) {
						BB[out++] = (byte) 0xff;
					} else {
						BB[out++] = (byte) 0x00;
					}
					b >>= 1;
				}
			}
			offset0 += YSTEP0;
		}

		bb.put(BB);
		bb.rewind();
	}

	public void writePixmap1(Pixmap aPixmap, int aCX1, int aCY1) {
		byte c = (byte) ((((aCX1 + aCY1) & 1) == 0) ? 0x00 : 0x0f);

		ByteBuffer bb = aPixmap.getPixels();
		bb.rewind();

		int i1 = xyToIndex1(aCX1 << 6, aCY1 << 6);
		int out = 0;

		byte b;
		for (int i = 0; i < 64; ++i) {
			int j0 = i1;
			int j1 = i1 + 32;
			for (int j = j0; j < j1; ++j) {
				b = mData1[j];
				for (int k = 0; k < 2; ++k) {
					int v = b & 0xf;
					BB[out++] = c;
					BB[out++] = c;
					BB[out++] = c;
					BB[out++] = (byte) (v | (v << 4));
					b >>= 4;
				}
			}
			i1 += YSTEP1;
		}

		bb.put(BB);
		bb.rewind();
	}

	public void writePixmap2(Pixmap aPixmap, int aCX2, int aCY2) {
		byte c = (byte) ((((aCX2 + aCY2) & 1) == 0) ? 0x00 : 0x0f);

		ByteBuffer bb = aPixmap.getPixels();
		bb.rewind();

		int i2 = xyToIndex2(aCX2 << 6, aCY2 << 6);
		int out = 0;

		for (int i = 0; i < 64; ++i) {
			int ii0 = i2;
			int ii1 = i2 + 64;
			for (int ii = ii0; ii < ii1; ++ii) {
				BB[out++] = c;
				BB[out++] = c;
				BB[out++] = c;
				BB[out++] = mData2[ii];
			}
			i2 += YSTEP2;
		}

		bb.put(BB);
		bb.rewind();
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
