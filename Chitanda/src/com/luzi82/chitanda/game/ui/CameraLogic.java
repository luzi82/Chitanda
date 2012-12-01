package com.luzi82.chitanda.game.ui;

import com.luzi82.chitanda.Const;
import com.luzi82.chitanda.game.logic.Board;

public class CameraLogic {

	// screen
	public int mScreenWidth;
	public int mScreenHeight;
	public float mViewPortWidth;
	public float mViewPortHeight;

	// camera
	public float iCameraZoom;
	public float iCameraX;
	public float iCameraY;

	// camera dynamic
	public float mCameraZoomD;
	public float mCameraXD;
	public float mCameraYD;

	// zoom limit
	public float mZoomMin;
	public float mZoomMax;
	public float mLogZoomMin;
	public float mLogZoomMax;

	public CameraLogic() {
		iCameraZoom = Math.min(Board.WIDTH, Board.HEIGHT);
		iCameraX = Board.WIDTH / 2;
		iCameraY = Board.HEIGHT / 2;
		mCameraZoomD = 0;
		mCameraXD = 0;
		mCameraYD = 0;
	}

	public void zoomMove(float aNewZoom, float aDelta) {
		mCameraZoomD = ((float) Math.log(aNewZoom / iCameraZoom)) / aDelta;
		iCameraZoom = aNewZoom;
	}

	public void xyMove(float aNewX, float aNewY, float aDelta) {
		mCameraXD = (aNewX - iCameraX) / aDelta;
		mCameraYD = (aNewY - iCameraY) / aDelta;
		iCameraX = aNewX;
		iCameraY = aNewY;
	}

	public void xySet(float aNewX, float aNewY) {
		iCameraX = aNewX;
		iCameraY = aNewY;
	}

	public float viewBY0Min() {
		return iCameraY - iCameraZoom * mViewPortHeight / 2;
	}

	public float viewBY0Max() {
		return iCameraY + iCameraZoom * mViewPortHeight / 2;
	}

	public float viewBX0Min() {
		return iCameraX - iCameraZoom * mViewPortWidth / 2;
	}

	public float viewBX0Max() {
		return iCameraX + iCameraZoom * mViewPortWidth / 2;
	}

	public float screenToBoardX(float aX) {
		return iCameraX + (iCameraZoom * mViewPortWidth * (aX / mScreenWidth - 0.5f));
	}

	public float screenToBoardY(float aY) {
		return iCameraY + (iCameraZoom * mViewPortHeight * (1 - (aY / mScreenHeight) - 0.5f));
	}

	public float screenBoardToCameraX(float aScreenX, float aBoardX) {
		return (iCameraZoom * mViewPortWidth) * (0.5f - aScreenX / mScreenWidth) + aBoardX;
	}

	public float screenBoardToCameraY(float aScreenY, float aBoardY) {
		return (iCameraZoom * mViewPortHeight) * (0.5f + aScreenY / mScreenHeight - 1) + aBoardY;
	}

	public void onScreenResize(int aScreenWidth, int aScreenHeight) {
		mScreenWidth = aScreenWidth;
		mScreenHeight = aScreenHeight;
		mViewPortWidth = (mScreenWidth > mScreenHeight) ? (((float) mScreenWidth) / mScreenHeight) : 1;
		mViewPortHeight = (mScreenWidth > mScreenHeight) ? 1 : (((float) mScreenHeight) / mScreenWidth);

		mZoomMin = 4 * Const.PHI;
		mZoomMax = Math.max(Board.HEIGHT / mViewPortHeight, Board.WIDTH / mViewPortWidth) * Const.PHI;
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
		iCameraX = smooth(aDelta, aReduce, aIntReduce, iCameraX, mCameraXD, 0, Board.WIDTH);
		iCameraY = smooth(aDelta, aReduce, aIntReduce, iCameraY, mCameraYD, 0, Board.HEIGHT);
		mCameraXD *= aReduce;
		mCameraYD *= aReduce;
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

}
