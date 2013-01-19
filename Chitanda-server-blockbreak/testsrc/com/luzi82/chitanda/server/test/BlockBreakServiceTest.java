package com.luzi82.chitanda.server.test;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.Assert;

import org.junit.Test;

import com.luzi82.chitanda.common.game.Board;
import com.luzi82.chitanda.server.BlockBreakCore;
import com.luzi82.chitanda.server.BlockBreakService;

public class BlockBreakServiceTest {

	public static final SimpleDateFormat LOG_SIMPLEDATEFORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	public static class ListenerLogger extends Log.Logger implements BlockBreakService.Listener {
		private static final long serialVersionUID = -6450852765101030201L;

		public ListenerLogger(String aName, Log aLog) {
			super(aName, aLog);
		}

		@Override
		public void updateBlock(int aBoardId, int aZoom, int aXz, int aYz, byte[] aData) {
			Log.LogUnit log = createLog();
			log.put("aBoardId", aBoardId);
			log.put("aZoom", aZoom);
			log.put("aXz", aXz);
			log.put("aYz", aYz);
			log.put("aData", aData);
		}

		@Override
		public void newGame(int aBoardId) {
			Log.LogUnit log = createLog();
			log.put("aBoardId", aBoardId);
		}
	}

	@Test
	public void testSetView() {
		ExecutorService es = Executors.newFixedThreadPool(5);
		BlockBreakCore core = new BlockBreakCore(es);
		BlockBreakService service = new BlockBreakService(core);
		core.addService(service);
		ListenerLogger listenerLogger = new ListenerLogger("9YAAOPii", null);
		service.setListener(listenerLogger);

		service.setView(0, 0, 0, 1, 1);

		sleep(100);

		Assert.assertEquals(1, listenerLogger.size());
		Assert.assertEquals("updateBlock", listenerLogger.get(0).mStackTrace[0].getMethodName());

		listenerLogger.clear();

		service.setView(0, 0, 0, 2, 3);

		sleep(200);

		Assert.assertEquals(6, listenerLogger.size());
	}

	@Test
	public void testBoardcast() {
		ExecutorService es = Executors.newFixedThreadPool(5);
		BlockBreakCore core = new BlockBreakCore(es);
		BlockBreakService service0 = new BlockBreakService(core);
		BlockBreakService service1 = new BlockBreakService(core);
		ListenerLogger listenerLogger0 = new ListenerLogger("0t2D2d5C", null);
		ListenerLogger listenerLogger1 = new ListenerLogger("40NK1BQh", null);
		CallbackLock<Void> callbackLockVoid;

		core.mBoard.setAll(true);

		core.addService(service0);
		core.addService(service1);
		service0.setListener(listenerLogger0);
		service1.setListener(listenerLogger1);

		service0.setView(0, 0, 0, 1, 1);
		service1.setView(0, 0, 0, 1, 1);
		sleep(100);

		listenerLogger0.clear();
		listenerLogger1.clear();
		callbackLockVoid = new CallbackLock<Void>();
		service0.startUpdateBoard(0, 0, 0, new byte[Board.UPDATE0_SIZE], callbackLockVoid);

		sleep(100); // listenerLoggerN need to be notified

		Assert.assertEquals(true, callbackLockVoid.mDoneCalled);
		Assert.assertEquals(1, listenerLogger0.size());
		Assert.assertEquals(1, listenerLogger1.size());
	}

	public static void sleep(long aMs) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}
}
