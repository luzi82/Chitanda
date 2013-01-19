package com.luzi82.chitanda.common;

import java.util.concurrent.Executor;

public interface Callback<T> {
	public void done(T aResult);

	public void exception(Exception aException);

	public class Ret {
		public static <T> void startDone(final Callback<T> aCallback, final T aArg, Executor aExecutor) {
			if (aCallback == null)
				return;
			aExecutor.execute(new Runnable() {
				@Override
				public void run() {
					aCallback.done(aArg);
				}
			});
		}

		public static <T> void startException(final Callback<T> aCallback, final Exception aException, Executor aExecutor) {
			if (aCallback == null)
				return;
			aExecutor.execute(new Runnable() {
				@Override
				public void run() {
					aCallback.exception(aException);
				}
			});
		}
	}
}
