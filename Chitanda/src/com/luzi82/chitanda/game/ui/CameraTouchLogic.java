package com.luzi82.chitanda.game.ui;

public class CameraTouchLogic {

	// touch
	public static final int TOUCH_MAX = 16;
	private boolean mNewTouch;
	private boolean mNewTouchEvent;
	private boolean[] mTouching;
	private int[] mTouchX;
	private int[] mTouchY;
	private float mTouchStartCameraX;
	private float mTouchStartCameraY;
	private float mTouchStartDiff;
	private float mTouchStartCameraZoom;

	// mouse
	private int mMouseOverX;
	private int mMouseOverY;
	private int mMouseScrolled;

	public CameraLogic mCameraLogic;

	public CameraTouchLogic(CameraLogic aCameraLogic) {
		mCameraLogic = aCameraLogic;

		mTouching = new boolean[TOUCH_MAX];
		mTouchX = new int[TOUCH_MAX];
		mTouchY = new int[TOUCH_MAX];
	}

	public void update(float aDelta) {
		int i;

		float reduce = (float) Math.pow(CameraLogic.SMOOTH_REDUCE, aDelta);
		float intReduce = (reduce - 1) * CameraLogic.DIV_LN_SMOOTH_REDUCE;

		float touchXAvg = 0;
		float touchYAvg = 0;
		float touchDiff = 0;
		int touchCount = 0;

		for (i = 0; i < TOUCH_MAX; ++i) {
			if (!mTouching[i])
				continue;
			touchXAvg += mTouchX[i];
			touchYAvg += mTouchY[i];
			++touchCount;
		}
		if (touchCount > 0) {
			touchXAvg /= touchCount;
			touchYAvg /= touchCount;
			if (touchCount > 1) {
				for (i = 0; i < TOUCH_MAX; ++i) {
					if (!mTouching[i])
						continue;
					float d = 0, dd = 0;
					dd = mTouchX[i] - touchXAvg;
					dd *= dd;
					d += dd;
					dd = mTouchY[i] - touchYAvg;
					dd *= dd;
					d += dd;
					touchDiff += (float) Math.sqrt(d);
				}
			}
			touchDiff /= touchCount;
			if (mNewTouch) {
				mTouchStartCameraX = mCameraLogic.screenToBoardX(touchXAvg);
				mTouchStartCameraY = mCameraLogic.screenToBoardY(touchYAvg);
				if (touchCount > 1) {
					mTouchStartDiff = touchDiff;
					mTouchStartCameraZoom = mCameraLogic.iCameraZoom;
				}
				// mCameraZoomD = 0;
				mNewTouch = false;
			} else if (mNewTouchEvent) {
				if (touchCount > 1) {
					float newZoom = mTouchStartCameraZoom * mTouchStartDiff / touchDiff;
					mCameraLogic.zoomMove(newZoom, aDelta);
				} else {
					mCameraLogic.smoothZoom(aDelta, reduce, intReduce);
				}
				float newX = mCameraLogic.screenBoardToCameraX(touchXAvg, mTouchStartCameraX);
				float newY = mCameraLogic.screenBoardToCameraY(touchYAvg, mTouchStartCameraY);
				mCameraLogic.xyMove(newX, newY, aDelta);
			} else if (touchCount == 1) {
				mCameraLogic.smoothZoom(aDelta, reduce, intReduce);
				float newX = mCameraLogic.screenBoardToCameraX(touchXAvg, mTouchStartCameraX);
				float newY = mCameraLogic.screenBoardToCameraY(touchYAvg, mTouchStartCameraY);
				mCameraLogic.xyMove(newX, newY, aDelta);
			}
		} else if (mMouseScrolled != 0) {
			float x = mCameraLogic.screenToBoardX(mMouseOverX);
			float y = mCameraLogic.screenToBoardY(mMouseOverY);

			mCameraLogic.mCameraZoomD -= mMouseScrolled * CameraLogic.PHI;
			mCameraLogic.smoothZoom(aDelta, reduce, intReduce);

			float newX = mCameraLogic.screenBoardToCameraX(mMouseOverX, x);
			float newY = mCameraLogic.screenBoardToCameraY(mMouseOverY, y);
			// mCameraXD = (newX - iCameraX) / aDelta;
			// mCameraYD = (newY - iCameraY) / aDelta;
			// iCameraX = newX;
			// iCameraY = newY;
			mCameraLogic.xyMove(newX, newY, aDelta);
			mMouseScrolled = 0;
		} else {
			mCameraLogic.smoothZoom(aDelta, reduce, intReduce);
			mCameraLogic.smoothXY(aDelta, reduce, intReduce);
		}
		mNewTouchEvent = false;
	}

	public void touchDown(int x, int y, int pointer, int button) {
		// iLogger.debug("touchDown");
		mNewTouch = true;
		mNewTouchEvent = true;
		mTouching[pointer] = true;
		mTouchX[pointer] = x;
		mTouchY[pointer] = y;
	}

	public void touchUp(int x, int y, int pointer, int button) {
		// iLogger.debug("touchUp");
		mNewTouch = true;
		mNewTouchEvent = true;
		mTouching[pointer] = false;
	}

	public void touchDragged(int x, int y, int pointer) {
		// iLogger.debug("touchDragged");
		mTouching[pointer] = true;
		mNewTouchEvent = ((mTouchX[pointer] != x) || (mTouchY[pointer] != y));
		mTouchX[pointer] = x;
		mTouchY[pointer] = y;
	}

	public void touchMoved(int x, int y) {
		// iLogger.debug("touchMoved");
		mMouseOverX = x;
		mMouseOverY = y;
	}

	public void scrolled(int amount) {
		mMouseScrolled += amount;
	}

}
