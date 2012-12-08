package com.luzi82.chitanda.client.game.logic;

import java.nio.ByteBuffer;

import com.badlogic.gdx.graphics.Pixmap;

public class Board extends com.luzi82.chitanda.common.game.Board {

	static private byte[] BB = new byte[64 * 64 * 4];

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

}
