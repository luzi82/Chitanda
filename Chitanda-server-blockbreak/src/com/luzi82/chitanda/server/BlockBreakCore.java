package com.luzi82.chitanda.server;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

import com.luzi82.chitanda.common.Callback;
import com.luzi82.chitanda.common.game.Board;

public class BlockBreakCore {

	public Board mBoard = new Board();
	public int mBoardId = 0;

	public List<BlockBreakService> mServiceList = new LinkedList<BlockBreakService>();

	public Executor mExecutor;

	public BlockBreakCore(Executor aExecutorService) {
		mExecutor = new ThreadSafeExecutor(aExecutorService);
	}

	public void addService(BlockBreakService aService) {
		synchronized (mServiceList) {
			mServiceList.add(aService);
		}
	}

	public void removeService(BlockBreakService aService) {
		synchronized (mServiceList) {
			mServiceList.remove(aService);
		}
	}

	public void startNewGame(final Callback<Void> aCallback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				int boardId = -1;
				synchronized (BlockBreakCore.this) {
					mBoard.setAll(true);
					++mBoardId;
					boardId = mBoardId;
				}
				Callback.Ret.startDone(aCallback, null, mExecutor);
				List<BlockBreakService> serviceList = cloneServiceList();
				for (BlockBreakService service : serviceList) {
					service.startListenerNewGame(boardId);
				}
			}
		});
	}

	public void startUpdate(final int aBoardId, final int aX0, final int aY0, final byte[] aData, final Callback<Void> aCallback) {
		synchronized (this) {
			if (aBoardId != mBoardId) {
				Callback.Ret.startException(aCallback, new BoardIdException(), mExecutor);
				return;
			}
		}
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				byte[] d0, d1, d2;
				boolean good = false;
				synchronized (BlockBreakCore.this) {
					if (aBoardId != mBoardId) {
						Callback.Ret.startException(aCallback, new BoardIdException(), mExecutor);
						return;
					}
					good = mBoard.update0(aX0, aY0, aData);
					if (!good) {
						Callback.Ret.startDone(aCallback, null, mExecutor);
						return;
					}
					d0 = mBoard.getUpdate0(aX0 & Board.UPDATE_BLOCK_COOR_MASK, aY0 & Board.UPDATE_BLOCK_COOR_MASK);
					d1 = mBoard.getUpdate1((aX0 >> 2) & Board.UPDATE_BLOCK_COOR_MASK, (aY0 >> 2) & Board.UPDATE_BLOCK_COOR_MASK);
					d2 = mBoard.getUpdate2((aX0 >> 4) & Board.UPDATE_BLOCK_COOR_MASK, (aY0 >> 4) & Board.UPDATE_BLOCK_COOR_MASK);
				}
				Callback.Ret.startDone(aCallback, null, mExecutor);
				List<BlockBreakService> serviceList = cloneServiceList();
				final byte[] data0 = d0;
				final byte[] data1 = d1;
				final byte[] data2 = d2;
				for (final BlockBreakService service : serviceList) {
					service.startListenerUpdateBoard(aBoardId, 0, aX0, aY0, data0);
					service.startListenerUpdateBoard(aBoardId, 1, aX0 >> 2, aY0 >> 2, data1);
					service.startListenerUpdateBoard(aBoardId, 2, aX0 >> 4, aY0 >> 4, data2);
				}
			}
		});
	}

	public void startGetBoardId(final Callback<Integer> aCallback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				int boardId;
				synchronized (BlockBreakCore.this) {
					boardId = mBoardId;
				}
				aCallback.done(boardId);
			}
		});
	}

	private List<BlockBreakService> cloneServiceList() {
		synchronized (mServiceList) {
			return new LinkedList<BlockBreakService>(mServiceList);
		}
	}

	public static class BoardIdException extends Exception {

		private static final long serialVersionUID = -2491178527578542584L;

	}
}
