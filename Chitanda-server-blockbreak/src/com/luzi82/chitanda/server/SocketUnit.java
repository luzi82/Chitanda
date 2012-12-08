package com.luzi82.chitanda.server;

import java.net.Socket;

import com.luzi82.chitanda.common.game.Board;

public class SocketUnit implements Runnable {

	Board pBoard;
	Socket mSocket;

	public SocketUnit(Board aBoard, Socket aSocket) {
		pBoard = aBoard;
		mSocket = aSocket;
	}

	@Override
	public void run() {
		
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

}
