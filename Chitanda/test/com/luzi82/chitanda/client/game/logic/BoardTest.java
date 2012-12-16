package com.luzi82.chitanda.client.game.logic;

import static org.junit.Assert.*;
import org.junit.Test;

public class BoardTest {

	@Test
	public void testUpdate1Remote() {
		int i, ii, iii;

		Board b = new Board();
		byte[] pixmapBuf = new byte[Board.PIXMAP_BLOCK_SIZE * Board.PIXMAP_BLOCK_SIZE * 4];
		byte[] data = new byte[Board.UPDATE_BLOCK_SIZE * Board.UPDATE_BLOCK_SIZE / 2];

		// //

		b.setAll(true);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0xff, pixmapBuf[3]);

		for (i = 0; i < data.length; ++i) {
			data[i] = (byte) 0xff;
		}

		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0xff, pixmapBuf[3]);
		assertEquals(0xf, b.get1(0, 0));
		assertEquals(0xff, b.get2(0, 0));

		// //

		data[0] = (byte) 0xf4;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x44, pixmapBuf[3]);
		assertEquals(0xf, b.get1(0, 0));
		assertEquals(0xff, b.get2(0, 0));

		data[0] = (byte) 0xf0;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x00, pixmapBuf[3]);
		for (i = 0; i < 100; ++i) {
			for (ii = 0; ii < 100; ++ii) {
				boolean expect = !(i < 4 && ii < 4);
				assertEquals(expect, b.get0(i, ii));
			}
		}
		assertEquals(0, b.get1(0, 0));
		assertEquals(0xef, b.get2(0, 0));

		// //

		b.setAll(true);
		for (i = 0; i < data.length; ++i) {
			data[i] = (byte) 0xff;
		}
		data[0] = (byte) 0x5f;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x55, pixmapBuf[7]);
		assertEquals(0xf, b.get1(1, 0));
		assertEquals(0xff, b.get2(0, 0));

		data[0] = (byte) 0x0f;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x00, pixmapBuf[7]);
		for (i = 0; i < 100; ++i) {
			for (ii = 0; ii < 100; ++ii) {
				boolean expect = !(i >= 4 && i < 8 && ii < 4);
				assertEquals(expect, b.get0(i, ii));
			}
		}
		assertEquals(0, b.get1(1, 0));
		assertEquals(0xef, b.get2(0, 0));

		// //

		b.setAll(true);
		for (i = 0; i < data.length; ++i) {
			data[i] = (byte) 0xff;
		}
		data[1] = (byte) 0xf1;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x11, pixmapBuf[11]);
		assertEquals(0xf, b.get1(2, 0));
		assertEquals(0xff, b.get2(0, 0));

		data[1] = (byte) 0xf0;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x00, pixmapBuf[11]);
		for (i = 0; i < 100; ++i) {
			for (ii = 0; ii < 100; ++ii) {
				boolean expect = !(i >= 8 && i < 12 && ii < 4);
				assertEquals(expect, b.get0(i, ii));
			}
		}
		assertEquals(0, b.get1(2, 0));
		assertEquals(0xef, b.get2(0, 0));

		// //

		b.setAll(true);
		for (i = 0; i < data.length; ++i) {
			data[i] = (byte) 0xff;
		}
		data[Board.UPDATE_BLOCK_SIZE / 2] = (byte) 0xf2;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x22, pixmapBuf[Board.PIXMAP_BLOCK_SIZE * 4 + 3]);
		assertEquals(0xf, b.get1(0, 1));
		assertEquals(0xff, b.get2(0, 0));

		data[Board.UPDATE_BLOCK_SIZE / 2] = (byte) 0xf0;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x00, pixmapBuf[Board.PIXMAP_BLOCK_SIZE * 4 + 3]);
		for (i = 0; i < 100; ++i) {
			for (ii = 0; ii < 100; ++ii) {
				boolean expect = !(i >= 0 && i < 4 && ii >= 4 && ii < 8);
				assertEquals(expect, b.get0(i, ii));
			}
		}
		assertEquals(0, b.get1(0, 1));
		assertEquals(0xef, b.get2(0, 0));

		// //

		int x1 = 11;
		int y1 = 13;
		int i1 = (y1 * Board.UPDATE_BLOCK_SIZE + x1) / 2;
		int p1 = y1 * Board.PIXMAP_BLOCK_SIZE + x1;
		b.setAll(true);
		for (i = 0; i < data.length; ++i) {
			data[i] = (byte) 0xff;
		}
		data[i1] = (byte) 0x3f;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x33, pixmapBuf[p1 * 4 + 3]);
		assertEquals(0xf, b.get1(x1, y1));
		assertEquals(0xff, b.get2(x1 / 4, y1 / 4));

		data[i1] = (byte) 0x0f;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x00, pixmapBuf[p1 * 4 + 3]);
		for (i = (x1 * 4 - 5); i < (x1 * 4 + 10); ++i) {
			for (ii = (y1 * 4 - 5); ii < (y1 * 4 + 10); ++ii) {
				boolean expect = !((i >= x1 * 4) && (i < x1 * 4 + 4) && (ii >= y1 * 4) && (ii < y1 * 4 + 4));
				assertEquals(expect, b.get0(i, ii));
			}
		}
		assertEquals(0, b.get1(x1, y1));
		assertEquals(0xef, b.get2(x1 / 4, y1 / 4));

		// //

		x1 = 15;
		y1 = 15;
		i1 = (y1 * Board.UPDATE_BLOCK_SIZE + x1) / 2;
		p1 = y1 * Board.PIXMAP_BLOCK_SIZE + x1;
		assertEquals(data.length - 1, i1); // special check
		b.setAll(true);
		for (i = 0; i < data.length; ++i) {
			data[i] = (byte) 0xff;
		}
		data[i1] = (byte) 0x3f;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x33, pixmapBuf[p1 * 4 + 3]);
		assertEquals(0xf, b.get1(x1, y1));
		assertEquals(0xff, b.get2(x1 / 4, y1 / 4));

		data[i1] = (byte) 0x0f;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x00, pixmapBuf[p1 * 4 + 3]);
		for (i = (x1 * 4 - 5); i < (x1 * 4 + 10); ++i) {
			for (ii = (y1 * 4 - 5); ii < (y1 * 4 + 10); ++ii) {
				boolean expect = !((i >= x1 * 4) && (i < x1 * 4 + 4) && (ii >= y1 * 4) && (ii < y1 * 4 + 4));
				assertEquals(expect, b.get0(i, ii));
			}
		}
		assertEquals(0, b.get1(x1, y1));
		assertEquals(0xef, b.get2(x1 / 4, y1 / 4));
	}

	// TODO ori-data / remote-data overlap
}
