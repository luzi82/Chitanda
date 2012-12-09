package com.luzi82.chitanda.server;

import com.luzi82.chitanda.common.game.Board;

public class BlockBreakService implements com.luzi82.chitanda.common.game.io.BlockBreakService {

	Board pBoard;
	Listener pListener;

	public BlockBreakService(Board aBoard) {
		pBoard = aBoard;
	}

	@Override
	public void setListener(Listener aListener) {
		pListener = aListener;
	}

	@Override
	public void setView(int aZoom, int aXZ, int aYZ, int aWZ, int aHZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlock(int aX0, int aY0, byte[] aData, UpdateBlockCallback aCallback) {
		// TODO Auto-generated method stub
		
	}

}
