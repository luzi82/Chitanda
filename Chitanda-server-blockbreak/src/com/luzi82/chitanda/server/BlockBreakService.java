package com.luzi82.chitanda.server;

import com.luzi82.chitanda.common.Callback;

public class BlockBreakService implements com.luzi82.chitanda.common.game.io.BlockBreakService {

	BlockBreakCore pCore;
	Listener pListener;

	int mZoom = -1;
	int mXZ;
	int mYZ;
	int mWZ;
	int mHZ;

	public BlockBreakService(BlockBreakCore aCore) {
		pCore = aCore;
	}

	@Override
	public void setListener(Listener aListener) {
		pListener = aListener;
	}

	@Override
	public void setView(final int aZoom, int aXZ, int aYZ, int aWZ, int aHZ) {
		mZoom = aZoom;
		mXZ = aXZ;
		mYZ = aYZ;
		mWZ = aWZ;
		mHZ = aHZ;
		pCore.mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				for (int x = mXZ; x < mXZ + mWZ; ++x) {
					for (int y = mYZ; y < mYZ + mHZ; ++y) {
						pCore.startServiceListenerUpdateBoard(aZoom, x, y, BlockBreakService.this);
					}
				}
			}
		});
	}

	@Override
	public void startUpdateBoard(int aBoardId, int aX0, int aY0, byte[] aData, Callback<Void> aCallback) {
		pCore.startUpdateBoard(aBoardId, aX0, aY0, aData, aCallback);
	}

	public void startListenerUpdateBoard(final int aBoardId, final int aZoom, final int aXz, final int aYz, final byte[] aData) {
		pCore.mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				if (pListener == null)
					return;
				if (aZoom != mZoom)
					return;
				if (aXz < mXZ)
					return;
				if (aXz >= mXZ + mWZ)
					return;
				if (aYz < mYZ)
					return;
				if (aYz >= mYZ + mHZ)
					return;
				pListener.updateBlock(aBoardId, aZoom, aXz, aYz, aData);
			}
		});
	}

	public void startListenerNewGame(final int aBoardId) {
		pCore.mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				pListener.newGame(aBoardId);
			}
		});
	}

	@Override
	public void startGetBoardId(Callback<Integer> aCallback) {
		pCore.startGetBoardId(aCallback);
	}

}
