package com.luzi82.chitanda.game.ui;

import com.luzi82.chitanda.Const;


public class CameraControl {

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

	public CameraCalc mCameraLogic;

	public CameraControl(CameraCalc aCameraLogic) {
		mCameraLogic = aCameraLogic;

		mTouching = new boolean[TOUCH_MAX];
		mTouchSX = new int[TOUCH_MAX];
		mTouchSY = new int[TOUCH_MAX];
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
				mTouchStartBXAvg = mCameraLogic.screenToBoardX(touchSXAvg);
				mTouchStartBYAvg = mCameraLogic.screenToBoardY(touchSYAvg);
				if (touchCount > 1) {
					mTouchStartSDiff = touchSDiff;
					mTouchStartCameraZoom = mCameraLogic.iCameraZoom;
				} else {
					mCameraLogic.smoothZoom(aDelta, reduce, intReduce);
					float newCameraBX = mCameraLogic.screenBoardToCameraX(touchSXAvg, mTouchStartBXAvg);
					float newCameraBY = mCameraLogic.screenBoardToCameraY(touchSYAvg, mTouchStartBYAvg);
					mCameraLogic.xyMove(newCameraBX, newCameraBY, aDelta);
				}
				mTouchCountChange = false;
			} else if (mTouchChange) {
				if (touchCount > 1) {
					float newZoom = mTouchStartCameraZoom * mTouchStartSDiff / touchSDiff;
					mCameraLogic.zoomMove(newZoom, aDelta);
				} else {
					mCameraLogic.smoothZoom(aDelta, reduce, intReduce);
				}
				float newCameraBX = mCameraLogic.screenBoardToCameraX(touchSXAvg, mTouchStartBXAvg);
				float newCameraBY = mCameraLogic.screenBoardToCameraY(touchSYAvg, mTouchStartBYAvg);
				mCameraLogic.xyMove(newCameraBX, newCameraBY, aDelta);
			} else if (touchCount == 1) {
				mCameraLogic.smoothZoom(aDelta, reduce, intReduce);
				float newCameraBX = mCameraLogic.screenBoardToCameraX(touchSXAvg, mTouchStartBXAvg);
				float newCameraBY = mCameraLogic.screenBoardToCameraY(touchSYAvg, mTouchStartBYAvg);
				mCameraLogic.xySet(newCameraBX, newCameraBY);
			}
		} else if (mMouseScrolled != 0) {
			float mouseBX = mCameraLogic.screenToBoardX(mMouseOverSX);
			float mouseBY = mCameraLogic.screenToBoardY(mMouseOverSY);

			mCameraLogic.mCameraZoomD -= mMouseScrolled * Const.PHI;
			mCameraLogic.smoothZoom(aDelta, reduce, intReduce);

			float newCameraBX = mCameraLogic.screenBoardToCameraX(mMouseOverSX, mouseBX);
			float newCameraBY = mCameraLogic.screenBoardToCameraY(mMouseOverSY, mouseBY);
			mCameraLogic.xyMove(newCameraBX, newCameraBY, aDelta);
			mMouseScrolled = 0;
		} else {
			mCameraLogic.smoothZoom(aDelta, reduce, intReduce);
			mCameraLogic.smoothXY(aDelta, reduce, intReduce);
		}
		mTouchChange = false;
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
