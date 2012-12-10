package com.luzi82.chitanda.common.game.io;

public interface BlockBreakService {

	static public interface Listener {
		public void updateBlock(int aZoom, int aXz, int aYz, byte[] aData);
	}

	public void setListener(Listener aListener);

	public void setView(int aZoom, int aXZ, int aYZ, int aWZ, int aHZ);

	public void updateBlock(int aX0, int aY0, byte[] aData, UpdateBlockCallback aCallback);

	static public interface UpdateBlockCallback {
		public void updateBlockCallback(boolean aSuccess);
	}

}
