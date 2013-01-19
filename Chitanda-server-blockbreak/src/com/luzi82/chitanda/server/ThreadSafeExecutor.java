package com.luzi82.chitanda.server;

import java.util.concurrent.Executor;

public class ThreadSafeExecutor implements Executor {

	final Executor mExecutor;

	public ThreadSafeExecutor(Executor aExecutor) {
		mExecutor = aExecutor;
	}

	@Override
	public synchronized void execute(Runnable command) {
		mExecutor.execute(command);
	}

}
