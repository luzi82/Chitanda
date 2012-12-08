package com.luzi82.chitanda.game.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.luzi82.chitanda.client.game.logic.Board;

public class BoardTest {

	@Test
	public void testGetSet() {
		Board b = new Board();
		int x, y, xx, yy;
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				b.set(xx, yy, false);
				assertFalse(b.get0(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				b.set(xx, yy, false);
				assertFalse(b.get0(xx, yy));
			}
		}
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertFalse(b.get0(xx, yy));
				b.set(xx, yy, true);
				assertTrue(b.get0(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertFalse(b.get0(xx, yy));
				b.set(xx, yy, true);
				assertTrue(b.get0(xx, yy));
			}
		}
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertTrue(b.get0(xx, yy));
				b.set(xx, yy, false);
				assertFalse(b.get0(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertTrue(b.get0(xx, yy));
				b.set(xx, yy, false);
				assertFalse(b.get0(xx, yy));
			}
		}
	}

	@Test
	public void testSetAll() {
		Board b = new Board();
		int x, y, xx, yy;
		b.setAll(false);
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertFalse(b.get0(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertFalse(b.get0(xx, yy));
			}
		}
		b.setAll(true);
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertTrue(b.get0(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertTrue(b.get0(xx, yy));
			}
		}
		b.setAll(false);
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertFalse(b.get0(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertFalse(b.get0(xx, yy));
			}
		}
	}

	@Test
	public void testIndex() {
		int t0, t1;

		t0 = Board.xyToIndex0(0, 0);
		assertEquals(0, t0);

		t0 = Board.xyToIndex0(Board.WIDTH - 1, 0);
		t1 = Board.xyToIndex0(0, 1);
		assertEquals(t0 + 1, t1);
		assertEquals(t1, Board.YSTEP0);

		t0 = Board.xyToIndex1(0, 0);
		assertEquals(0, t0);

		t0 = Board.xyToIndex1(2, 0);
		assertEquals(1, t0);
		t0 = Board.xyToOffset1(2, 0);
		assertEquals(0, t0);

		t0 = Board.xyToIndex1((Board.WIDTH >> 2) - 1, 0);
		t1 = Board.xyToIndex1(0, 1);
		assertEquals(t0 + 1, t1);
		assertEquals(t1, Board.YSTEP1);

		t0 = Board.xyToIndex2(0, 0);
		assertEquals(0, t0);

		t0 = Board.xyToIndex2((Board.WIDTH >> 4) - 1, 0);
		t1 = Board.xyToIndex2(0, 1);
		assertEquals(t0 + 1, t1);
		assertEquals(t1, Board.YSTEP2);
	}

	@Test
	public void testSetLayer() {
		Board b = new Board();
		int all;
		int ans;
		int i0, o0;
		int x1, y1;
		int x2, y2;

		// layer 1 off
		b.setAll(true);

		x1 = 0;
		y1 = 0;
		i0 = Board.xyToIndex0(x1 * 4, y1 * 4);
		o0 = Board.xyToOffset0(x1 * 4, y1 * 4);
		all = 16;
		assertEquals(all, b.layerTotal1(i0, o0));
		assertEquals(15, b.get1(x1, y1));
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				b.set(x1 * 4 + i, y1 * 4 + j, false);
				--all;
				assertEquals(all, b.layerTotal1(i0, o0));
				ans = (all > 7) ? (all - 1) : all;
				assertEquals(ans, b.get1(x1, y1));
			}
		}

		x1 = 1;
		y1 = 0;
		i0 = Board.xyToIndex0(x1 * 4, y1 * 4);
		o0 = Board.xyToOffset0(x1 * 4, y1 * 4);
		all = 16;
		assertEquals(all, b.layerTotal1(i0, o0));
		assertEquals(15, b.get1(x1, y1));
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				b.set(x1 * 4 + i, y1 * 4 + j, false);
				--all;
				assertEquals(all, b.layerTotal1(i0, o0));
				ans = (all > 7) ? (all - 1) : all;
				assertEquals(ans, b.get1(x1, y1));
			}
		}

		x1 = 0;
		y1 = 1;
		i0 = Board.xyToIndex0(x1 * 4, y1 * 4);
		o0 = Board.xyToOffset0(x1 * 4, y1 * 4);
		all = 16;
		assertEquals(all, b.layerTotal1(i0, o0));
		assertEquals(15, b.get1(x1, y1));
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				b.set(x1 * 4 + i, y1 * 4 + j, false);
				--all;
				assertEquals(all, b.layerTotal1(i0, o0));
				ans = (all > 7) ? (all - 1) : all;
				assertEquals(ans, b.get1(x1, y1));
			}
		}

		x1 = 2;
		y1 = 0;
		i0 = Board.xyToIndex0(x1 * 4, y1 * 4);
		o0 = Board.xyToOffset0(x1 * 4, y1 * 4);
		all = 16;
		assertEquals(all, b.layerTotal1(i0, o0));
		assertEquals(15, b.get1(x1, y1));
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				b.set(x1 * 4 + i, y1 * 4 + j, false);
				--all;
				assertEquals(all, b.layerTotal1(i0, o0));
				ans = (all > 7) ? (all - 1) : all;
				assertEquals(ans, b.get1(x1, y1));
			}
		}

		x1 = 13;
		y1 = 17;
		i0 = Board.xyToIndex0(x1 * 4, y1 * 4);
		o0 = Board.xyToOffset0(x1 * 4, y1 * 4);
		all = 16;
		assertEquals(all, b.layerTotal1(i0, o0));
		assertEquals(15, b.get1(x1, y1));
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				b.set(x1 * 4 + i, y1 * 4 + j, false);
				--all;
				assertEquals(all, b.layerTotal1(i0, o0));
				ans = (all > 7) ? (all - 1) : all;
				assertEquals(ans, b.get1(x1, y1));
			}
		}

		// layer 1 on
		b.setAll(false);

		x1 = 0;
		y1 = 0;
		i0 = Board.xyToIndex0(x1 * 4, y1 * 4);
		o0 = Board.xyToOffset0(x1 * 4, y1 * 4);
		all = 0;
		assertEquals(all, b.layerTotal1(i0, o0));
		assertEquals(0, b.get1(x1, y1));
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				b.set(x1 * 4 + i, y1 * 4 + j, true);
				++all;
				assertEquals(all, b.layerTotal1(i0, o0));
				ans = (all > 7) ? (all - 1) : all;
				assertEquals(ans, b.get1(x1, y1));
			}
		}

		x1 = 13;
		y1 = 17;
		i0 = Board.xyToIndex0(x1 * 4, y1 * 4);
		o0 = Board.xyToOffset0(x1 * 4, y1 * 4);
		all = 0;
		assertEquals(all, b.layerTotal1(i0, o0));
		assertEquals(0, b.get1(x1, y1));
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				b.set(x1 * 4 + i, y1 * 4 + j, true);
				++all;
				assertEquals(all, b.layerTotal1(i0, o0));
				ans = (all > 7) ? (all - 1) : all;
				assertEquals(ans, b.get1(x1, y1));
			}
		}

		// layer 2 off
		b.setAll(true);

		x2 = 0;
		y2 = 0;
		i0 = Board.xyToIndex0(x2 * 16, y2 * 16);
		all = 256;
		assertEquals(all, b.layerTotal2(i0));
		assertEquals(255, b.get2(x2, y2));
		for (int i = 0; i < 16; ++i) {
			for (int j = 0; j < 16; ++j) {
				b.set(x2 * 16 + i, y2 * 16 + j, false);
				--all;
				assertEquals(all, b.layerTotal2(i0));
				ans = (all > 127) ? (all - 1) : all;
				assertEquals(ans, b.get2(x2, y2));
			}
		}

		x2 = 1;
		y2 = 0;
		i0 = Board.xyToIndex0(x2 * 16, y2 * 16);
		all = 256;
		assertEquals(all, b.layerTotal2(i0));
		assertEquals(255, b.get2(x2, y2));
		for (int i = 0; i < 16; ++i) {
			for (int j = 0; j < 16; ++j) {
				b.set(x2 * 16 + i, y2 * 16 + j, false);
				--all;
				assertEquals(all, b.layerTotal2(i0));
				ans = (all > 127) ? (all - 1) : all;
				assertEquals(ans, b.get2(x2, y2));
			}
		}

	}

}
