package com.luzi82.chitanda.client.game.logic;

import java.nio.ByteBuffer;
import java.util.TreeSet;

import com.badlogic.gdx.graphics.Pixmap;

public class Board extends com.luzi82.chitanda.common.game.Board {

	static public final int PIXMAP_BLOCK_SIZE = 64;

	static private byte[] BB = new byte[64 * 64 * 4];

	protected final byte[] mData1Remote = new byte[DATA1_SIZE];
	protected final byte[] mData1View = new byte[DATA1_SIZE];

	protected final byte[] mData2Remote = new byte[DATA2_SIZE];
	protected final byte[] mData2View = new byte[DATA2_SIZE];

	public int get1View(int aX1, int aY1) {
		int i1 = xyToIndex1(aX1, aY1);
		int o1 = xyToOffset1(aX1, aY1);
		return (mData1View[i1] >> o1) & 0xf;
	}

	public int get2View(int aX2, int aY2) {
		int i2 = xyToIndex2(aX2, aY2);
		return ((int) (mData2View[i2])) & 0xff;
	}

	public void update1Remote(int aX1, int aY1, byte[] aData1) {
		final int i1 = xyToIndex1(aX1, aY1);
		int ii1 = i1;
		int iii1;
		int di = 0;
		TreeSet<Integer> l2Update = new TreeSet<Integer>();
		for (int y1 = 0; y1 < UPDATE_BLOCK_SIZE; ++y1) {
			iii1 = ii1;
			for (int xi1 = 0; xi1 < 8; ++xi1) {
				// replace mData1Remote
				mData1Remote[iii1] = aData1[di];

				for (int xo1 = 0; xo1 < 8; xo1 += 4) {
					int dr = (mData1Remote[iii1] >> xo1) & 0xf;
					int d = (mData1[iii1] >> xo1) & 0xf;

					// replace mData1View
					if (dr < d) {
						int dMin = Math.min(dr, d);
						byte dv = mData1View[iii1];
						dv &= ~(0xf << xo1);
						dv |= (dMin << xo1);
						mData1View[iii1] = dv;
					}

					// update mData0/mData1
					if ((dr == 0 && d != 0)) {
						// mData1
						mData1[iii1] &= ~(0xf << xo1);

						// mData0
						int x0 = (((iii1 % YSTEP1) << 1) | (xo1 >> 2)) << 2;
						int y0 = (iii1 / YSTEP1) << 2;
						int i0 = xyToIndex0(x0, y0);
						int o0 = xyToOffset0(x0, y0);
						int mask = ~(0xf << o0);
						for (int ii0 = 0; ii0 < 4; ++ii0) {
							mData0[i0] &= mask;
							i0 += YSTEP0;
						}

						// l2Update
						l2Update.add(((x0 >> 4) << 16) | (y0 >> 4));
					}
				}

				++iii1;
				++di;
			}
			ii1 += YSTEP1;
		}

		for (int xy2 : l2Update) {
			int x2 = (xy2 >> 16) & 0xffff;
			int y2 = xy2 & 0xffff;
			int i2 = xyToIndex2(x2, y2);
			int i0 = xyToIndex0(x2 << 4, y2 << 4);
			int v2 = layerTotal2(i0);
			v2 -= (v2 >> 7);
			mData2[i2] = (byte) v2;
		}
		++mVersion;
	}

	public void update2Remote(int aX2, int aY2, byte[] aData) {
		final int i2 = xyToIndex2(aX2, aY2);
		int ii2 = i2;
		int iii2;
		int di = 0;
		int d;
		for (int y2 = 0; y2 < UPDATE_BLOCK_SIZE; ++y2) {
			iii2 = ii2;
			for (int xi2 = 0; xi2 < UPDATE_BLOCK_SIZE; ++xi2) {
				d = aData[di] & 0xff;
				mData2Remote[iii2] = (byte) d;

				// update mData2View
				if (d < (mData2View[iii2] & 0xff)) {
					mData2View[iii2] = (byte) d;
				}

				// when zero, update mData, mDataView
				if ((d == 0) && (mData2[iii2] != 0)) {
					mData2[iii2] = 0;
					int x1 = (aX2 + xi2) << 2;
					int y1 = (aY2 + y2) << 2;
					zeroRect1(mData1Remote, x1, y1, 4, 4);
					zeroRect1(mData1, x1, y1, 4, 4);
					zeroRect1(mData1View, x1, y1, 4, 4);
					zeroRect0(mData0, x1 << 2, y1 << 2, 16, 16);
				}

				++iii2;
				++di;
			}
			ii2 += YSTEP2;
		}
	}

	public void writePixmap0(Pixmap aPixmap, int aX0P, int aY0P) {
		fillPixmapBuf0(BB, aX0P, aY0P);

		ByteBuffer bb = aPixmap.getPixels();
		bb.rewind();
		bb.put(BB);
		bb.rewind();
	}

	public void fillPixmapBuf0(byte[] aBuffer, int aX0P, int aY0P) {
		byte c = (byte) ((((aX0P + aY0P) & 1) == 0) ? 0x00 : 0x0f);

		int offset0 = xyToIndex0(aX0P << 6, aY0P << 6);
		int out = 0;

		byte b;
		for (int i = 0; i < 64; ++i) {
			int j0 = offset0;
			int j1 = offset0 + 8;
			for (int j = j0; j < j1; ++j) {
				b = mData0[j];
				for (int k = 0; k < Byte.SIZE; ++k) {
					aBuffer[out++] = c;
					aBuffer[out++] = c;
					aBuffer[out++] = c;
					if ((b & 1) == 1) {
						aBuffer[out++] = (byte) 0xff;
					} else {
						aBuffer[out++] = (byte) 0x00;
					}
					b >>= 1;
				}
			}
			offset0 += YSTEP0;
		}
	}

	public void writePixmap1(Pixmap aPixmap, int aX1P, int aY1P) {
		// byte c = (byte) ((((aCX1 + aCY1) & 1) == 0) ? 0x00 : 0x0f);

		fillPixmapBuf1(BB, aX1P, aY1P);

		ByteBuffer bb = aPixmap.getPixels();
		bb.rewind();

		// int i1 = xyToIndex1(aCX1 << 6, aCY1 << 6);
		// int out = 0;
		//
		// byte b;
		// for (int i = 0; i < 64; ++i) {
		// int j0 = i1;
		// int j1 = i1 + 32;
		// for (int j = j0; j < j1; ++j) {
		// b = mData1[j];
		// for (int k = 0; k < 2; ++k) {
		// int v = b & 0xf;
		// BB[out++] = c;
		// BB[out++] = c;
		// BB[out++] = c;
		// BB[out++] = (byte) (v | (v << 4));
		// b >>= 4;
		// }
		// }
		// i1 += YSTEP1;
		// }

		bb.put(BB);
		bb.rewind();
	}

	public void fillPixmapBuf1(byte[] aBuffer, int aX1P, int aY1P) {
		byte c = (byte) ((((aX1P + aY1P) & 1) == 0) ? 0x00 : 0x0f);

		// ByteBuffer bb = aPixmap.getPixels();
		// bb.rewind();

		int i1 = xyToIndex1(aX1P << 6, aY1P << 6);
		int out = 0;

		byte b;
		for (int i = 0; i < 64; ++i) {
			int j0 = i1;
			int j1 = i1 + 32;
			for (int j = j0; j < j1; ++j) {
				// b = mData1[j];
				b = mData1View[j];
				for (int k = 0; k < 2; ++k) {
					int v = b & 0xf;
					aBuffer[out++] = c;
					aBuffer[out++] = c;
					aBuffer[out++] = c;
					aBuffer[out++] = (byte) (v | (v << 4));
					b >>= 4;
				}
			}
			i1 += YSTEP1;
		}

		// bb.put(BB);
		// bb.rewind();
	}

	public void writePixmap2(Pixmap aPixmap, int aX2P, int aY2P) {
		byte c = (byte) ((((aX2P + aY2P) & 1) == 0) ? 0x00 : 0x0f);

		ByteBuffer bb = aPixmap.getPixels();
		bb.rewind();

		int i2 = xyToIndex2(aX2P << 6, aY2P << 6);
		int out = 0;

		for (int i = 0; i < 64; ++i) {
			int ii0 = i2;
			int ii1 = i2 + 64;
			for (int ii = ii0; ii < ii1; ++ii) {
				BB[out++] = c;
				BB[out++] = c;
				BB[out++] = c;
				BB[out++] = mData2View[ii];
			}
			i2 += YSTEP2;
		}

		bb.put(BB);
		bb.rewind();
	}

	public boolean set(int aX, int aY, boolean aV) {
		boolean ret = super.set(aX, aY, aV);
		if (ret) {
			int i1 = xyToIndex1(aX >> 2, aY >> 2);
			int o1 = xyToOffset1(aX >> 2, aY >> 2);
			int d1 = ((mData1[i1]) >> o1) & 0xf;
			int vv1 = mData1View[i1];
			int v1 = (vv1 >> o1) & 0xf;
			v1 = Math.min(d1, v1);
			vv1 = (vv1 & (~(0xf << o1))) | (v1 << o1);
			mData1View[i1] = (byte) vv1;
			int i2 = xyToIndex2(aX >> 4, aY >> 4);
			mData2View[i2] = (byte) Math.min((mData2View[i2] & 0xff), (mData2[i2] & 0xff));
		}
		return ret;
	}

	public boolean update0(int aX0, int aY0, byte[] aData) {
		boolean ret = super.update0(aX0, aY0, aData);
		if (ret) {
			int i1 = xyToIndex1(aX0 >> 2, aY0 >> 2);
			int ii1;
			int d1, d1d, d1v;
			int v1d, v1v;
			for (int y1 = 0; y1 < 4; ++y1) {
				ii1 = i1;
				for (int xi1 = 0; xi1 < 2; ++xi1) {
					d1d = mData1[ii1];
					d1v = mData1View[ii1];
					d1 = 0;
					for (int xo1 = 0; xo1 < 8; xo1 += 4) {
						v1d = (d1d >> xo1) & 0xf;
						v1v = (d1v >> xo1) & 0xf;
						v1v = Math.min(v1d, v1v);
						d1 |= v1v << xo1;
					}
					mData1View[ii1] = (byte) d1;
					++ii1;
				}
				i1 += YSTEP1;
			}
			int i2 = xyToIndex2(aX0 >> 4, aY0 >> 4);
			mData2View[i2] = (byte) (Math.min(mData2[i2], mData2View[i2]));
		}
		return ret;
	}

	public void setAll(boolean aValue) {
		super.setAll(aValue);
		byte v = (byte) (aValue ? 0xff : 0x00);
		for (int i = 0; i < DATA1_SIZE; ++i) {
			mData1Remote[i] = v;
			mData1View[i] = v;
		}
		for (int i = 0; i < DATA2_SIZE; ++i) {
			mData2Remote[i] = v;
			mData2View[i] = v;
		}
	}

}
