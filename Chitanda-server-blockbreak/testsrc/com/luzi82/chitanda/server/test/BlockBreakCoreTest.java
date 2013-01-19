package com.luzi82.chitanda.server.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.chitanda.common.game.Board;
import com.luzi82.chitanda.server.BlockBreakCore;

public class BlockBreakCoreTest {

	@Test
	public void testStartUpdateBoardChange() {
		try {
			ExecutorService es = Executors.newFixedThreadPool(5);
			BlockBreakCore core = new BlockBreakCore(es);
			CallbackLock<Void> clv = new CallbackLock<Void>();
			core.startUpdate(0, 0, 0, new byte[Board.UPDATE0_SIZE], clv);
			clv.w(1000);
			Assert.assertTrue(clv.mDoneCalled);
			Assert.assertEquals(core.mBoard.get0(0, 0), false);
		} catch (Throwable t) {
			throw new Error(t);
		}
	}

}
