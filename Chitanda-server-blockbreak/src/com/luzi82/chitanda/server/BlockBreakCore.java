package com.luzi82.chitanda.server;

import java.util.concurrent.ExecutorService;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import com.luzi82.chitanda.common.game.Board;

public class BlockBreakCore {

	Board mBoard;

	MultiMap mViewMap = new MultiValueMap();

	ExecutorService mExecutorService;

	public BlockBreakCore(ExecutorService aExecutorService) {
		mExecutorService = aExecutorService;
	}

	static public class ViewCoor implements Comparable<ViewCoor> {
		public int aZoom;
		public int aXz;
		public int aYz;

		@Override
		public int compareTo(ViewCoor o) {
			int ret;
			ret = aZoom - o.aZoom;
			if (ret != 0)
				return ret;
			ret = aXz - o.aXz;
			if (ret != 0)
				return ret;
			ret = aYz - o.aYz;
			if (ret != 0)
				return ret;
			return 0;
		}
	}

	static public interface View {
		public ViewCoor getCoor();

		public void update(byte[] aData);
	}

	public void addView(View aView) {
		mViewMap.put(aView.getCoor(), aView);
	}

	public void removeView(View aView) {
		mViewMap.remove(aView.getCoor(), aView);
	}

	public void startUpdate(final int aX0, final int aY0, final byte[] aData) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				synchronized (mBoard) {
					
				}
			}
		};
		synchronized (mExecutorService) {
			mExecutorService.execute(task);
		}
	}

}
