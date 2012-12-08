package com.luzi82.chitanda.common.game.io;

public interface ClientSide extends Side {
	public void updateBoard(int aX, int aY, byte[] aValue);
}
