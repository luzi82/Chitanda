package com.luzi82.chitanda.game.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
				assertFalse(b.get(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				b.set(xx, yy, false);
				assertFalse(b.get(xx, yy));
			}
		}
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertFalse(b.get(xx, yy));
				b.set(xx, yy, true);
				assertTrue(b.get(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertFalse(b.get(xx, yy));
				b.set(xx, yy, true);
				assertTrue(b.get(xx, yy));
			}
		}
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertTrue(b.get(xx, yy));
				b.set(xx, yy, false);
				assertFalse(b.get(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertTrue(b.get(xx, yy));
				b.set(xx, yy, false);
				assertFalse(b.get(xx, yy));
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
				assertFalse(b.get(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertFalse(b.get(xx, yy));
			}
		}
		b.setAll(true);
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertTrue(b.get(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertTrue(b.get(xx, yy));
			}
		}
		b.setAll(false);
		for (x = 0; x < 100; ++x) {
			for (y = 0; y < 100; ++y) {
				xx = x;
				yy = y;
				assertFalse(b.get(xx, yy));

				xx = Board.WIDTH - x - 1;
				yy = Board.HEIGHT - y - 1;
				assertFalse(b.get(xx, yy));
			}
		}
	}

}
