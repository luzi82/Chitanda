package com.luzi82.chitanda.game.ui;

import com.luzi82.chitanda.Const;

public class CameraControl {

	public CameraCalc mCameraCalc;

	// lock
	public long mLockTime;

	// touch
	public static final int TOUCH_MAX = 16;
	private boolean mTouchCountChange;
	private boolean mTouchChange;
	private boolean[] mTouching;
	private int[] mTouchSX;
	private int[] mTouchSY;
	private float mTouchStartBXAvg;
	private float mTouchStartBYAvg;
	private float mTouchStartSDiff;
	private float mTouchStartCameraZoom;

	// mouse
	private int mMouseOverSX;
	private int mMouseOverSY;
	private int mMouseScrolled;

	public CameraControl(CameraCalc aCameraCalc) {
		mCameraCalc = aCameraCalc;

		mLockTime = -1;

		mTouching = new boolean[TOUCH_MAX];
		mTouchSX = new int[TOUCH_MAX];
		mTouchSY = new int[TOUCH_MAX];
	}

	public void update(float aDelta) {
		int i;

		if (mLockTime < System.currentTimeMillis()) {
			mLockTime = -1;
		}

		float reduce = (float) Math.pow(Const.SMOOTH_REDUCE, aDelta);
		float intReduce = (reduce - 1) * Const.DIV_LN_SMOOTH_REDUCE;

		float touchSXAvg = 0;
		float touchSYAvg = 0;
		float touchSDiff = 0;
		int touchCount = 0;

		if (mTouchCountChange) {
			mCameraCalc.iCameraBX = mCameraCalc.iCameraRealBX;
			mCameraCalc.iCameraBY = mCameraCalc.iCameraRealBY;
			mCameraCalc.iCameraZoom = mCameraCalc.iCameraRealZoom;
		}

		for (i = 0; i < TOUCH_MAX; ++i) {
			if (!mTouching[i])
				continue;
			touchSXAvg += mTouchSX[i];
			touchSYAvg += mTouchSY[i];
			++touchCount;
		}
		if (touchCount > 0) {
			touchSXAvg /= touchCount;
			touchSYAvg /= touchCount;
			if (touchCount > 1) {
				for (i = 0; i < TOUCH_MAX; ++i) {
					if (!mTouching[i])
						continue;
					float d = 0, dd = 0;
					dd = mTouchSX[i] - touchSXAvg;
					dd *= dd;
					d += dd;
					dd = mTouchSY[i] - touchSYAvg;
					dd *= dd;
					d += dd;
					touchSDiff += (float) Math.sqrt(d);
				}
			}
			touchSDiff /= touchCount;
			if (mTouchCountChange) {
				mTouchStartBXAvg = mCameraCalc.screenToBoardX(touchSXAvg);
				mTouchStartBYAvg = mCameraCalc.screenToBoardY(touchSYAvg);
				if (touchCount > 1) {
					mTouchStartSDiff = touchSDiff;
					mTouchStartCameraZoom = mCameraCalc.iCameraZoom;
				} else {
					mCameraCalc.smoothZoom(aDelta, reduce, intReduce);
					float newCameraBX = mCameraCalc.screenBoardToCameraX(touchSXAvg, mTouchStartBXAvg);
					float newCameraBY = mCameraCalc.screenBoardToCameraY(touchSYAvg, mTouchStartBYAvg);
					mCameraCalc.xyMove(newCameraBX, newCameraBY, aDelta);
				}
				mTouchCountChange = false;
			} else if (mTouchChange) {
				if (touchCount > 1) {
					float newZoom = mTouchStartCameraZoom * mTouchStartSDiff / touchSDiff;
					mCameraCalc.zoomMove(newZoom, aDelta);
				} else {
					mCameraCalc.smoothZoom(aDelta, reduce, intReduce);
				}
				float newCameraBX = mCameraCalc.screenBoardToCameraX(touchSXAvg, mTouchStartBXAvg);
				float newCameraBY = mCameraCalc.screenBoardToCameraY(touchSYAvg, mTouchStartBYAvg);
				mCameraCalc.xyMove(newCameraBX, newCameraBY, aDelta);
			} else if (touchCount == 1) {
				mCameraCalc.smoothZoom(aDelta, reduce, intReduce);
				float newCameraBX = mCameraCalc.screenBoardToCameraX(touchSXAvg, mTouchStartBXAvg);
				float newCameraBY = mCameraCalc.screenBoardToCameraY(touchSYAvg, mTouchStartBYAvg);
				mCameraCalc.xySet(newCameraBX, newCameraBY);
			}
		} else if (mMouseScrolled != 0) {
			float mouseBX = mCameraCalc.screenToBoardX(mMouseOverSX);
			float mouseBY = mCameraCalc.screenToBoardY(mMouseOverSY);

			mCameraCalc.mCameraZoomD -= mMouseScrolled * Const.PHI;
			mCameraCalc.smoothZoom(aDelta, reduce, intReduce);

			float newCameraBX = mCameraCalc.screenBoardToCameraX(mMouseOverSX, mouseBX);
			float newCameraBY = mCameraCalc.screenBoardToCameraY(mMouseOverSY, mouseBY);
			mCameraCalc.xyMove(newCameraBX, newCameraBY, aDelta);
			mMouseScrolled = 0;
		} else {
			mCameraCalc.smoothZoom(aDelta, reduce, intReduce);
			mCameraCalc.smoothXY(aDelta, reduce, intReduce);
		}
		mTouchChange = false;

		if (mLockTime < 0) {
			mCameraCalc.iCameraRealBX = mCameraCalc.iCameraBX;
			mCameraCalc.iCameraRealBY = mCameraCalc.iCameraBY;
			mCameraCalc.iCameraRealZoom = mCameraCalc.iCameraZoom;
		}
	}

	public void touchDown(int aSX, int aSY, int aPointer, int aButton, long aTime) {
		// iLogger.debug("touchDown");
		mTouchCountChange = true;
		mTouchChange = true;
		mTouching[aPointer] = true;
		mTouchSX[aPointer] = aSX;
		mTouchSY[aPointer] = aSY;
	}

	public void touchUp(int aSX, int aSY, int aPointer, int aButton, long aTime) {
		// iLogger.debug("touchUp");
		mTouchCountChange = true;
		mTouchChange = true;
		mTouching[aPointer] = false;
	}

	public void touchDragged(int aSX, int aSY, int aPointer, long aTime) {
		// iLogger.debug("touchDragged");
		mTouching[aPointer] = true;
		mTouchChange = ((mTouchSX[aPointer] != aSX) || (mTouchSY[aPointer] != aSY));
		mTouchSX[aPointer] = aSX;
		mTouchSY[aPointer] = aSY;
	}

	public void touchMoved(int aX, int aY, long aTime) {
		// iLogger.debug("touchMoved");
		mMouseOverSX = aX;
		mMouseOverSY = aY;
	}

	public void scrolled(int aAmount, long aTime) {
		mMouseScrolled += aAmount;
	}

}
