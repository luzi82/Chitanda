package com.luzi82.chitanda.common.game.io;

import com.luzi82.chitanda.common.Callback;

public interface BlockBreakService {

	static public interface Listener {
		public void updateBlock(int aBoardId, int aZoom, int aXz, int aYz, byte[] aData);

		public void newGame(int aBoardId);
	}

	public void setListener(Listener aListener);

	public void setView(int aZoom, int aXZ, int aYZ, int aWZ, int aHZ);

	public void startUpdateBoard(int aBoardId, int aX0, int aY0, byte[] aData, Callback<Void> aCallback);

	public void startGetBoardId(Callback<Integer> aCallback);

}
