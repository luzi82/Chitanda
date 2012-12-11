package com.luzi82.chitanda.common.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class BoardTest {

	@Test
	public void testGetSet() {
		Board b = new Board();
		int x, y, xx, yy;
		boolean setRet;
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
				setRet = b.set(xx, yy, true);
				assertTrue(b.get0(xx, yy));
				assertTrue(setRet);

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertFalse(b.get0(xx, yy));
				setRet = b.set(xx, yy, true);
				assertTrue(b.get0(xx, yy));
				assertTrue(setRet);
			}
		}
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertTrue(b.get0(xx, yy));
				setRet = b.set(xx, yy, true);
				assertTrue(b.get0(xx, yy));
				assertFalse(setRet);

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertTrue(b.get0(xx, yy));
				setRet = b.set(xx, yy, true);
				assertTrue(b.get0(xx, yy));
				assertFalse(setRet);
			}
		}
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertTrue(b.get0(xx, yy));
				setRet = b.set(xx, yy, false);
				assertFalse(b.get0(xx, yy));
				assertTrue(setRet);

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertTrue(b.get0(xx, yy));
				setRet = b.set(xx, yy, false);
				assertFalse(b.get0(xx, yy));
				assertTrue(setRet);
			}
		}
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertFalse(b.get0(xx, yy));
				setRet = b.set(xx, yy, false);
				assertFalse(b.get0(xx, yy));
				assertFalse(setRet);

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertFalse(b.get0(xx, yy));
				setRet = b.set(xx, yy, false);
				assertFalse(b.get0(xx, yy));
				assertFalse(setRet);
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

	@Test
	public void testUpdate() {
		Board b = new Board();
		Random r = new Random();

		boolean[][] block = new boolean[16][16];
		byte[] data = new byte[32];
		int[][] l1 = new int[4][4];
		int l2;

		int t;

		for (int c = 0; c < 100; ++c) {
			b.setAll(true);
			for (int i = 0; i < 32; ++i) {
				data[i] = 0;
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					l1[i][j] = 0;
				}
			}
			l2 = 0;
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					boolean tmpB = r.nextBoolean();
					block[i][j] = tmpB;
					data[j * 2 + i / 8] |= tmpB ? (1 << (i % 8)) : 0;
					l1[i >> 2][j >> 2] += tmpB ? 1 : 0;
					l2 += tmpB ? 1 : 0;
				}
			}

			b.update0(0, 0, data);
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					assertEquals(block[i][j], b.get0(i, j));
				}
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					t = l1[i][j];
					t -= t >> 3;
					assertEquals(t, b.get1(i, j));
				}
			}
			t = l2;
			t -= t >> 7;
			assertEquals(t, b.get2(0, 0));

			b.update0(23 << 4, 31 << 4, data);
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					assertEquals(block[i][j], b.get0((23 << 4) + i, (31 << 4) + j));
				}
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					t = l1[i][j];
					t -= t >> 3;
					assertEquals(t, b.get1((23 << 2) + i, (31 << 2) + j));
				}
			}
			t = l2;
			t -= t >> 7;
			assertEquals(t, b.get2(23, 31));

			b.update0(511 << 4, 255 << 4, data);
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					assertEquals(block[i][j], b.get0((511 << 4) + i, (255 << 4) + j));
				}
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					t = l1[i][j];
					t -= t >> 3;
					assertEquals(t, b.get1((511 << 2) + i, (255 << 2) + j));
				}
			}
			t = l2;
			t -= t >> 7;
			assertEquals(t, b.get2(511, 255));
		}
	}

	@Test
	public void testUpdateOverTrue() {
		Board b = new Board();
		Random r = new Random();

		boolean[][] block = new boolean[16][16];
		boolean[][] block0 = new boolean[16][16];
		byte[] data = new byte[32];
		int[][] l1 = new int[4][4];
		int l2;

		int t;

		for (int c = 0; c < 100; ++c) {
			b.setAll(true);

			for (int i = 0; i < 32; ++i) {
				data[i] = 0;
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					l1[i][j] = 0;
				}
			}
			l2 = 0;
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					boolean tmpB = r.nextBoolean();
					block[i][j] = tmpB;
					data[j * 2 + i / 8] |= tmpB ? (1 << (i % 8)) : 0;
					l1[i >> 2][j >> 2] += tmpB ? 1 : 0;
					l2 += tmpB ? 1 : 0;
				}
			}

			boolean tB = b.update0(0, 0, data);
			assertTrue(tB);
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					assertEquals(block[i][j], b.get0(i, j));
				}
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					t = l1[i][j];
					t -= t >> 3;
					assertEquals(t, b.get1(i, j));
				}
			}
			t = l2;
			t -= t >> 7;
			assertEquals(t, b.get2(0, 0));

			// /

			for (int i = 0; i < 32; ++i) {
				data[i] = 0;
			}
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					boolean tmpB = block[i][j] || r.nextBoolean();
					block0[i][j] = tmpB;
					data[j * 2 + i / 8] |= tmpB ? (1 << (i % 8)) : 0;
				}
			}

			tB = b.update0(0, 0, data);
			assertFalse(tB);
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					assertEquals(block[i][j], b.get0(i, j));
				}
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					t = l1[i][j];
					t -= t >> 3;
					assertEquals(t, b.get1(i, j));
				}
			}
			t = l2;
			t -= t >> 7;
			assertEquals(t, b.get2(0, 0));
		}
	}

	@Test
	public void testUpdateOverFalse() {
		Board b = new Board();
		Random r = new Random();

		boolean[][] block = new boolean[16][16];
		byte[] data = new byte[32];
		int[][] l1 = new int[4][4];
		int l2;

		int t;

		for (int c = 0; c < 100; ++c) {
			b.setAll(true);

			for (int i = 0; i < 32; ++i) {
				data[i] = 0;
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					l1[i][j] = 0;
				}
			}
			l2 = 0;
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					boolean tmpB = r.nextBoolean();
					block[i][j] = tmpB;
					data[j * 2 + i / 8] |= tmpB ? (1 << (i % 8)) : 0;
					l1[i >> 2][j >> 2] += tmpB ? 1 : 0;
					l2 += tmpB ? 1 : 0;
				}
			}

			boolean tB = b.update0(0, 0, data);
			assertTrue(tB);
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					assertEquals(block[i][j], b.get0(i, j));
				}
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					t = l1[i][j];
					t -= t >> 3;
					assertEquals(t, b.get1(i, j));
				}
			}
			t = l2;
			t -= t >> 7;
			assertEquals(t, b.get2(0, 0));

			// /

			for (int i = 0; i < 32; ++i) {
				data[i] = 0;
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					l1[i][j] = 0;
				}
			}
			l2 = 0;
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					boolean tmpB = block[i][j] && r.nextBoolean();
					block[i][j] = tmpB;
					data[j * 2 + i / 8] |= tmpB ? (1 << (i % 8)) : 0;
					l1[i >> 2][j >> 2] += tmpB ? 1 : 0;
					l2 += tmpB ? 1 : 0;
				}
			}

			tB = b.update0(0, 0, data);
			assertTrue(tB);
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					assertEquals(block[i][j], b.get0(i, j));
				}
			}
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					t = l1[i][j];
					t -= t >> 3;
					assertEquals(t, b.get1(i, j));
				}
			}
			t = l2;
			t -= t >> 7;
			assertEquals(t, b.get2(0, 0));
		}
	}

	@Test
	public void testGetUpdate0() {
		Board b = new Board();
		Random r = new Random();

		byte[] data = new byte[32];
		byte[] update = null;

		for (int c = 0; c < 100; ++c) {
			b.setAll(true);

			r.nextBytes(data);

			b.update0(0, 0, data);
			update = b.getUpdate0(0, 0);
			assertTrue(Arrays.equals(data, update));

			b.update0(Board.WIDTH - 16, Board.HEIGHT - 16, data);
			update = b.getUpdate0(Board.WIDTH - 16, Board.HEIGHT - 16);
			assertTrue(Arrays.equals(data, update));
		}
	}

}
