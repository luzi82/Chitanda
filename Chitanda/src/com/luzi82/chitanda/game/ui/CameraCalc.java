package com.luzi82.chitanda.game.ui;

import com.luzi82.chitanda.Const;
import com.luzi82.chitanda.game.logic.Board;

public class CameraCalc {

	// screen
	public int mScreenW;
	public int mScreenH;
	public float mViewPortW;
	public float mViewPortH;

	// zoom limit
	public float mZoomMin;
	public float mZoomMax;
	public float mLogZoomMin;
	public float mLogZoomMax;

	// lock
	public long mLockTime;
	public float mLockBXDiff;
	public float mLockBYDiff;
	public float mLockZoomDiff;

	// camera (lock protected)
	public float iCameraRealZoom;
	public float iCameraRealBX;
	public float iCameraRealBY;

	// camera (calc use)
	public float iCameraZoom;
	public float iCameraBX;
	public float iCameraBY;

	// camera dynamic
	public float mCameraZoomD;
	public float mCameraBXD;
	public float mCameraBYD;

	public CameraCalc() {
		mLockTime = -1;
		mLockBXDiff = 0;
		mLockBYDiff = 0;
		mLockZoomDiff = 0;

		iCameraZoom = Math.min(Board.WIDTH, Board.HEIGHT);
		iCameraBX = Board.WIDTH / 2;
		iCameraBY = Board.HEIGHT / 2;
		mCameraZoomD = 0;
		mCameraBXD = 0;
		mCameraBYD = 0;
	}

	public void updateLock(float aReduce) {
		if (mLockTime < System.currentTimeMillis()) {
			mLockTime = -1;
		}

		if (mLockTime < 0) {
			CalcLockRet clr;

			clr = calcLock(iCameraRealBX, iCameraBX, mLockBXDiff, aReduce);
			iCameraRealBX = clr.mValue;
			mLockBXDiff = clr.mDiff;

			clr = calcLock(iCameraRealBY, iCameraBY, mLockBYDiff, aReduce);
			iCameraRealBY = clr.mValue;
			mLockBYDiff = clr.mDiff;

			clr = calcLock((float) Math.log(iCameraRealZoom), (float) Math.log(iCameraZoom), mLockZoomDiff, aReduce);
			iCameraRealZoom = (float) Math.pow(Math.E, clr.mValue);
			mLockZoomDiff = clr.mDiff;
		} else {
			mLockBXDiff = iCameraRealBX - iCameraBX;
			mLockBYDiff = iCameraRealBY - iCameraBY;
			mLockZoomDiff = Math.abs((float) Math.log(iCameraRealZoom / iCameraZoom));
		}
	}

	public void zoomMove(float aNewZoom, float aDelta) {
		mCameraZoomD = ((float) Math.log(aNewZoom / iCameraZoom)) / aDelta;
		iCameraZoom = aNewZoom;
	}

	public void xyMove(float aNewX, float aNewY, float aDelta) {
		mCameraBXD = (aNewX - iCameraBX) / aDelta;
		mCameraBYD = (aNewY - iCameraBY) / aDelta;
		iCameraBX = aNewX;
		iCameraBY = aNewY;
	}

	public void xySet(float aNewX, float aNewY) {
		iCameraBX = aNewX;
		iCameraBY = aNewY;
	}

	public float viewBY0Min() {
		return iCameraBY - iCameraZoom * mViewPortH / 2;
	}

	public float viewBY0Max() {
		return iCameraBY + iCameraZoom * mViewPortH / 2;
	}

	public float viewBX0Min() {
		return iCameraBX - iCameraZoom * mViewPortW / 2;
	}

	public float viewBX0Max() {
		return iCameraBX + iCameraZoom * mViewPortW / 2;
	}

	public float screenToBoardX(float aX) {
		return iCameraBX + (iCameraZoom * mViewPortW * (aX / mScreenW - 0.5f));
	}

	public float screenToBoardY(float aY) {
		return iCameraBY + (iCameraZoom * mViewPortH * (1 - (aY / mScreenH) - 0.5f));
	}

	public float screenToBoardRealX(float aX) {
		return iCameraRealBX + (iCameraRealZoom * mViewPortW * (aX / mScreenW - 0.5f));
	}

	public float screenToBoardRealY(float aY) {
		return iCameraRealBY + (iCameraRealZoom * mViewPortH * (1 - (aY / mScreenH) - 0.5f));
	}

	public float screenBoardToCameraX(float aScreenX, float aBoardX) {
		return (iCameraZoom * mViewPortW) * (0.5f - aScreenX / mScreenW) + aBoardX;
	}

	public float screenBoardToCameraY(float aScreenY, float aBoardY) {
		return (iCameraZoom * mViewPortH) * (0.5f + aScreenY / mScreenH - 1) + aBoardY;
	}

	public void onScreenResize(int aScreenWidth, int aScreenHeight) {
		mScreenW = aScreenWidth;
		mScreenH = aScreenHeight;
		mViewPortW = (mScreenW > mScreenH) ? (((float) mScreenW) / mScreenH) : 1;
		mViewPortH = (mScreenW > mScreenH) ? 1 : (((float) mScreenH) / mScreenW);

		mZoomMin = 4 * Const.PHI;
		mZoomMax = Math.max(Board.HEIGHT / mViewPortH, Board.WIDTH / mViewPortW) * Const.PHI;
		mLogZoomMin = (float) Math.log(mZoomMin);
		mLogZoomMax = (float) Math.log(mZoomMax);
	}

	public void smoothZoom(float aDelta, float aReduce, float aIntReduce) {
		// iCameraZoom *= (float) Math.pow(Math.E, mCameraZoomD * aIntReduce);
		float logCameraZoom = (float) Math.log(iCameraZoom);
		logCameraZoom = smooth(aDelta, aReduce, aIntReduce, logCameraZoom, mCameraZoomD, mLogZoomMin, mLogZoomMax);
		iCameraZoom = (float) Math.pow(Math.E, logCameraZoom);
		mCameraZoomD *= aReduce;
	}

	public void smoothXY(float aDelta, float aReduce, float aIntReduce) {
		// iCameraX += mCameraXD * aIntReduce;
		// iCameraY += mCameraYD * aIntReduce;
		iCameraBX = smooth(aDelta, aReduce, aIntReduce, iCameraBX, mCameraBXD, 0, Board.WIDTH);
		iCameraBY = smooth(aDelta, aReduce, aIntReduce, iCameraBY, mCameraBYD, 0, Board.HEIGHT);
		mCameraBXD *= aReduce;
		mCameraBYD *= aReduce;
	}

	private static float smooth(float aDelta, float aReduce, float aIntReduce, float aS0, float aV, float aMin, float aMax) {
		if ((aMin <= aS0) && (aS0 <= aMax)) {
			float s1 = aS0 + aV * aIntReduce;
			// TODO mid border
			return s1;
		} else {
			float border = (aS0 < aMin) ? aMin : aMax;
			float dm = aV * aIntReduce;
			float db = (aReduce - 1) * (2 * aS0 - 2 * border + dm) / 2;
			float s1 = aS0 + dm + db;
			// TODO mid border
			return s1;
		}

	}

	public static float diff(float aX0, float aY0, float aX1, float aY1) {
		float dx = aX1 - aX0;
		float dy = aY1 - aY0;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	private static class CalcLockRet {
		float mValue;
		float mDiff;

		CalcLockRet(float aTar, float aDiff) {
			mValue = aTar;
			mDiff = aDiff;
		}
	}

	private static CalcLockRet calcLock(float aOri, float aTar, float aDiff, float aReduce) {
		if (aDiff == 0)
			return new CalcLockRet(aTar, 0);
		if (aOri == aTar)
			return new CalcLockRet(aTar, 0);
		float diff = aOri - aTar;
		aDiff *= aReduce;
		if (diff * aDiff <= 0)
			return new CalcLockRet(aTar, 0);
		if (Math.abs(diff) <= Math.abs(aDiff))
			return new CalcLockRet(aOri, diff);
		return new CalcLockRet(aTar + aDiff, aDiff);
	}

}
