package com.luzi82.chitanda.game.ui;

import com.badlogic.gdx.Gdx;
import com.luzi82.chitanda.Const;

public class CameraControl {

	public CameraCalc mCameraCalc;

	// touch
	public static final int TOUCH_MAX = 16;
	private boolean mTouchCountChange;
	private boolean mTouchChange;
	private boolean[] mTouching;
	private int[] mTouchSX;
	private int[] mTouchSY;
	private int[] mTouchStartSX;
	private int[] mTouchStartSY;
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

		mTouching = new boolean[TOUCH_MAX];
		mTouchSX = new int[TOUCH_MAX];
		mTouchSY = new int[TOUCH_MAX];

		mTouchStartSX = new int[TOUCH_MAX];
		mTouchStartSY = new int[TOUCH_MAX];
	}

	public void update(float aDelta) {
		int i;

		float reduce = (float) Math.pow(Const.SMOOTH_REDUCE, aDelta);
		float intReduce = (reduce - 1) * Const.DIV_LN_SMOOTH_REDUCE;

		float touchSXAvg = 0;
		float touchSYAvg = 0;
		float touchSDiff = 0;
		int touchCount = 0;

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

		if (mTouchCountChange && (touchCount == 0) && (mCameraCalc.mLockTime > 0)) {
			mCameraCalc.iCameraBX = mCameraCalc.iCameraRealBX;
			mCameraCalc.iCameraBY = mCameraCalc.iCameraRealBY;
			mCameraCalc.iCameraZoom = mCameraCalc.iCameraRealZoom;
			mCameraCalc.mCameraBXD = 0;
			mCameraCalc.mCameraBYD = 0;
			mCameraCalc.mCameraZoomD = 0;
		}

		mCameraCalc.updateLock(reduce);
	}

	public void touchDown(int aSX, int aSY, int aPointer, int aButton, long aTime) {
		// iLogger.debug("touchDown");
		mTouchCountChange = true;
		mTouchChange = true;
		mTouching[aPointer] = true;
		mTouchSX[aPointer] = aSX;
		mTouchSY[aPointer] = aSY;
		mTouchStartSX[aPointer] = aSX;
		mTouchStartSY[aPointer] = aSY;
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

		float diff = CameraCalc.diff(aSX, aSY, mTouchStartSX[aPointer], mTouchStartSY[aPointer]);
		if (diff > Gdx.graphics.getPpcX() * 3) {
			mCameraCalc.mLockTime = -1;
		}
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
