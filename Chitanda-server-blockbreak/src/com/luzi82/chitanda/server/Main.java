package com.luzi82.chitanda.server;

import com.luzi82.chitanda.common.game.Board;

public class Main {

	Board mBoard;
	SocketListener mSocketListener;

	public Main(int aPort) {
		mBoard = new Board();
		mSocketListener = new SocketListener(mBoard, aPort);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main m = new Main(5678);
		m.start();
	}

	private void start() {
		mSocketListener.start();
	}

}
