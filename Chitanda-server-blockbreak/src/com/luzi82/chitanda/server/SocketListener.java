package com.luzi82.chitanda.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.luzi82.chitanda.common.game.Board;

public class SocketListener implements Runnable {

	Board pBoard;
	int mPort;

	ServerSocket mServerSocket;

	public SocketListener(Board aBoard, int aPort) {
		pBoard = aBoard;
		mPort = aPort;
	}

	@Override
	public void run() {
		try {
			mServerSocket = new ServerSocket(mPort);
			while (true) {
				Socket s = mServerSocket.accept();
				SocketUnit su = new SocketUnit(pBoard,s);
				su.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

}
