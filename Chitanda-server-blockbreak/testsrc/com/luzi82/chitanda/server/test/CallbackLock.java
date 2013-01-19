package com.luzi82.chitanda.server.test;

import com.luzi82.chitanda.common.Callback;

public class CallbackLock<T> implements Callback<T> {

	boolean mExceptionCalled = false;
	boolean mDoneCalled = false;
	T mResult;
	Exception mException;

	public synchronized void w(int aMs) {
		try {
			wait(aMs);
		} catch (InterruptedException e) {
		}
	}

	public synchronized void n() {
		notifyAll();
	}

	@Override
	public void done(T aResult) {
		if (mDoneCalled || mExceptionCalled) {
			throw new IllegalStateException();
		}
		mDoneCalled = true;
		mResult = aResult;
		n();
	}

	@Override
	public void exception(Exception aException) {
		if (mDoneCalled || mExceptionCalled) {
			throw new IllegalStateException();
		}
		mExceptionCalled = true;
		mException = aException;
		n();
	}

}
