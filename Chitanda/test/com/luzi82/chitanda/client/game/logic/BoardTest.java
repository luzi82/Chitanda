package com.luzi82.chitanda.client.game.logic;

import static org.junit.Assert.*;
import org.junit.Test;

public class BoardTest {

	@Test
	public void testUpdate1Remote() {
		int i, ii, iii;

		Board b = new Board();
		byte[] pixmapBuf = new byte[64 * 64 * 4];
		byte[] data = new byte[128];

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

		b.setAll(true);
		for (i = 0; i < data.length; ++i) {
			data[i] = (byte) 0xff;
		}
		data[0] = (byte) 0x5f;
		b.update1Remote(0, 0, data);
		b.fillPixmapBuf1(pixmapBuf, 0, 0);
		assertEquals((byte) 0x55, pixmapBuf[7]);

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
	}
}
