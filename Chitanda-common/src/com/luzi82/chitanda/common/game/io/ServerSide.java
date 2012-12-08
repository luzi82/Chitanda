package com.luzi82.chitanda.common.game.io;

public interface ServerSide {

	public void updateBoard(int aX, int aY, byte[] aValue, UpdateBoardReturn aUpdateBoardReturn);

	public interface UpdateBoardReturn {
		void updateBoardDone(boolean aSuccess);
	}

	public void setView(int aZoom, int aX, int aY, int aWidth, int aHeight);

}
