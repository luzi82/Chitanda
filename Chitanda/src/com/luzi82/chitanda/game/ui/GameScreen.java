package com.luzi82.chitanda.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.luzi82.chitanda.ChitandaGame;
import com.luzi82.chitanda.game.logic.Board;
import com.luzi82.gdx.GrScreen;

public class GameScreen extends GrScreen<ChitandaGame> {

	public static final int TOUCH_MAX = 16;
	public static final float SMOOTH_REDUCE = 1f / 256;
	public static final float DIV_LN_SMOOTH_REDUCE = (float) (1 / Math.log(SMOOTH_REDUCE));
	public static final float PHI = (float) (1 + Math.sqrt(5)) / 2;

	private static final float LINE_ALPHA = 1f / 16;
	private static final float LINE_WIDTH = 1f / 16;

	private Board mBoard;

	private OrthographicCamera mCamera;

	private Pixmap mBasePixmap0;
	private Pixmap mBasePixmap1;
	private Texture mBaseTexture0;
	private Texture mBaseTexture1;
	private Mesh mBaseMesh0;
	private Mesh mBaseMesh1;

	private Mesh mLineMeshH;
	private float[] mLineMeshHF;
	private final int[] LINE_MESH_HA = {//
	1 * 7 - 1,//
			2 * 7 - 1,//
			7 * 7 - 1,//
			8 * 7 - 1,//
	};

	private float mViewPortWidth;
	private float mViewPortHeight;

	private float iCameraZoom;
	private float iCameraX;
	private float iCameraY;
	private boolean mCameraUpdate;
	private float mCameraZoomD;
	private float mCameraXD;
	private float mCameraYD;

	private boolean mNewTouch;
	private boolean mNewTouchEvent;
	private boolean[] mTouching;
	private int[] mTouchX;
	private int[] mTouchY;
	// private boolean[] mTouching;
	// private boolean[] mMoved;
	private float mTouchStartCameraX;
	private float mTouchStartCameraY;
	private float mTouchStartDiff;
	private float mTouchStartCameraZoom;

	// private boolean mMoveEnabled;

	public GameScreen(ChitandaGame aParent) {
		super(aParent);
	}

	@Override
	protected void onScreenShow() {
		iCameraZoom = Math.min(Board.WIDTH, Board.HEIGHT);
		iCameraX = Board.WIDTH / 2;
		iCameraY = Board.HEIGHT / 2;
		mCameraUpdate = true;
	}

	@Override
	protected void onScreenLoad() {
		GL10 gl = Gdx.graphics.getGL10();

		mBoard = new Board();
		mBoard.setAll(true);

		mCamera = new OrthographicCamera();

		mBasePixmap0 = new Pixmap(Gdx.files.internal("data/chitanda0.png"));
		mBasePixmap1 = new Pixmap(Gdx.files.internal("data/chitanda1.png"));
		mBaseTexture0 = new Texture(mBasePixmap0, true);
		mBaseTexture1 = new Texture(mBasePixmap1, true);

		VertexAttributes va = new VertexAttributes( //
				new VertexAttribute(VertexAttributes.Usage.Position, 3, "position"),//
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "texturecoordinates")//
		);

		mBaseMesh0 = new Mesh(true, 4, 4, va);
		mBaseMesh0.setVertices(new float[] { //
				0f, 1024f * 4, 0f, 0f, 0f,//
						1024f * 4, 1024f * 4, 0f, 1f, 0f,//
						0f, 0f, 0f, 0f, 1f,//
						1024f * 4, 0f, 0f, 1f, 1f,//
				});
		mBaseMesh0.setIndices(new short[] { 0, 1, 2, 3 });
		mBaseMesh1 = new Mesh(true, 4, 4, va);
		mBaseMesh1.setVertices(new float[] { //
				1024f * 4, 1024f * 4, 0f, 0f, 0f,//
						1024f * 8, 1024f * 4, 0f, 1f, 0f,//
						1024f * 4, 0f, 0f, 0f, 1f,//
						1024f * 8, 0f, 0f, 1f, 1f,//
				});
		mBaseMesh1.setIndices(new short[] { 0, 1, 2, 3 });

		va = new VertexAttributes( //
				new VertexAttribute(VertexAttributes.Usage.Position, 3, "position"),//
				new VertexAttribute(VertexAttributes.Usage.Color, 4, "color")//
		);

		mLineMeshH = new Mesh(false, 8, 12, va);
		mLineMeshH.setIndices(new short[] { //
				0, 1, 2, //
						1, 2, 3, //
						4, 5, 6, //
						5, 6, 7 //
				});
		mLineMeshHF = new float[] { //
		1f, 0f, 0f, 0f, 0f, 0f, LINE_ALPHA,//
				0f, 0f, 0f, 0f, 0f, 0f, LINE_ALPHA,//
				1f, LINE_WIDTH, 0f, 0f, 0f, 0f, 0f,//
				0f, LINE_WIDTH, 0f, 0f, 0f, 0f, 0f,//

				1f, 1 - LINE_WIDTH, 0f, 0f, 0f, 0f, 0f,//
				0f, 1 - LINE_WIDTH, 0f, 0f, 0f, 0f, 0f,//
				1f, 1f, 0f, 0f, 0f, 0f, LINE_ALPHA,//
				0f, 1f, 0f, 0f, 0f, 0f, LINE_ALPHA,//
		};

		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

		// mTouching = new boolean[TOUCH_MAX];
		// mLastX = new int[TOUCH_MAX];
		// mLastY = new int[TOUCH_MAX];
		// mSumX = new int[TOUCH_MAX];
		// mSumY = new int[TOUCH_MAX];
		mNewTouch = true;
		mTouching = new boolean[TOUCH_MAX];
		mTouchX = new int[TOUCH_MAX];
		mTouchY = new int[TOUCH_MAX];

		mCameraZoomD = 0;
		mCameraXD = 0;
		mCameraYD = 0;
	}

	@Override
	public void onScreenRender(float delta) {
		int i;

		float reduce = (float) Math.pow(SMOOTH_REDUCE, delta);
		float intReduce = (reduce - 1) * DIV_LN_SMOOTH_REDUCE;

		GL10 gl = Gdx.graphics.getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

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
				mTouchStartCameraX = iCameraX + (iCameraZoom * mViewPortWidth * (touchXAvg / mScreenWidth - 0.5f));
				mTouchStartCameraY = iCameraY + (iCameraZoom * mViewPortHeight * (1 - (touchYAvg / mScreenHeight) - 0.5f));
				if (touchCount > 1) {
					mTouchStartDiff = touchDiff;
					mTouchStartCameraZoom = iCameraZoom;
				}
				mNewTouch = false;
			} else if (mNewTouchEvent) {
				if (touchCount > 1) {
					float newZoom = mTouchStartCameraZoom * mTouchStartDiff / touchDiff;
					mCameraZoomD = ((float) Math.log(newZoom / iCameraZoom)) / delta;
					iCameraZoom = newZoom;
				} else {
					// iCameraZoom *= (float)Math.pow(Math.E,mCameraZoomD *
					// intReduce);
					// mCameraZoomD *= reduce;
					smoothZoom(delta, reduce, intReduce);
				}
				float newX = (iCameraZoom * mViewPortWidth) * (0.5f - touchXAvg / mScreenWidth) + mTouchStartCameraX;
				float newY = (iCameraZoom * mViewPortHeight) * (0.5f + touchYAvg / mScreenHeight - 1) + mTouchStartCameraY;
				mCameraXD = (newX - iCameraX) / delta;
				mCameraYD = (newY - iCameraY) / delta;
				iCameraX = newX;
				iCameraY = newY;
				mCameraUpdate = true;
			}
		} else {
			smoothZoom(delta, reduce, intReduce);
			smoothXY(delta, reduce, intReduce);
			// iCameraZoom *= (float)Math.pow(Math.E,mCameraZoomD * intReduce);
			// iCameraX += mCameraXD * intReduce;
			// iCameraY += mCameraYD * intReduce;
			// mCameraZoomD *= reduce;
			// mCameraXD *= reduce;
			// mCameraYD *= reduce;
			mCameraUpdate = true;
		}
		mNewTouchEvent = false;

		// iCameraZoom = correct(PHI * 4, iCameraZoom, 4 * 1024 * PHI);
		// iCameraX = correct(0, iCameraX, Board.WIDTH);
		// iCameraY = correct(0, iCameraY, Board.HEIGHT);

		if (mCameraUpdate) {
			mCamera.zoom = iCameraZoom;
			mCamera.position.x = iCameraX;
			mCamera.position.y = iCameraY;
			mCamera.update();
			mCameraUpdate = false;
		}
		mCamera.apply(gl);

		// back image
		gl.glDisable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ZERO);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		mBaseTexture0.bind();
		mBaseMesh0.render(GL10.GL_TRIANGLE_STRIP);
		mBaseTexture1.bind();
		mBaseMesh1.render(GL10.GL_TRIANGLE_STRIP);

		// line
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL10.GL_TEXTURE_2D);

		if (iCameraZoom < ZOOM_MIN * PHI * PHI) {
			float a = iCameraZoom / (ZOOM_MIN * PHI);
			a = (float) Math.log(a);
			a /= (float) Math.log(PHI);
			a = 1 - a;
			if (a > 0) {
				if (a > 1)
					a = 1f;
				a *= LINE_ALPHA;
				for (int ai : LINE_MESH_HA) {
					mLineMeshHF[ai] = a;
				}
				mLineMeshH.setVertices(mLineMeshHF);

				int min, max;

				min = (int) Math.floor(iCameraY - iCameraZoom * mViewPortHeight / 2);
				max = (int) Math.ceil(iCameraY + iCameraZoom * mViewPortHeight / 2);
				if (min < 0)
					min = 0;
				if (max > Board.HEIGHT)
					max = Board.HEIGHT;
				for (i = min; i < max; ++i) {
					gl.glPushMatrix();
					gl.glTranslatef(0, i, 0);
					gl.glScalef(Board.WIDTH, 1, 1);
					mLineMeshH.render(GL10.GL_TRIANGLES);
					gl.glPopMatrix();
				}

				min = (int) Math.floor(iCameraX - iCameraZoom * mViewPortWidth / 2);
				max = (int) Math.ceil(iCameraX + iCameraZoom * mViewPortWidth / 2);
				if (min < 0)
					min = 0;
				if (max > Board.WIDTH)
					max = Board.WIDTH;
				gl.glPushMatrix();
				gl.glRotatef(90, 0, 0, 1);
				gl.glScalef(1, -1, 1);
				for (i = min; i < max; ++i) {
					gl.glPushMatrix();
					gl.glTranslatef(0, i, 0);
					gl.glScalef(Board.HEIGHT, 1, 1);
					mLineMeshH.render(GL10.GL_TRIANGLES);
					gl.glPopMatrix();
				}
				gl.glPopMatrix();
			}
		}
	}

	@Override
	public void onScreenResize() {
		mViewPortWidth = (mScreenWidth > mScreenHeight) ? (((float) mScreenWidth) / mScreenHeight) : 1;
		mViewPortHeight = (mScreenWidth > mScreenHeight) ? 1 : (((float) mScreenHeight) / mScreenWidth);
		mCamera.viewportWidth = mViewPortWidth;
		mCamera.viewportHeight = mViewPortHeight;
		mCameraUpdate = true;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		mNewTouch = true;
		mNewTouchEvent = true;
		mTouching[pointer] = true;
		mTouchX[pointer] = x;
		mTouchY[pointer] = y;
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		mNewTouch = true;
		mNewTouchEvent = true;
		mTouching[pointer] = false;
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		mNewTouchEvent = true;
		mTouching[pointer] = true;
		mTouchX[pointer] = x;
		mTouchY[pointer] = y;
		return true;
	}

	static private float ZOOM_MIN = PHI * 4;
	static private float ZOOM_MAX = 4 * 1024 * PHI;
	static private float LOG_ZOOM_MIN = (float) Math.log(ZOOM_MIN);
	static private float LOG_ZOOM_MAX = (float) Math.log(ZOOM_MAX);

	private void smoothZoom(float aDelta, float aReduce, float aIntReduce) {
		// iCameraZoom *= (float) Math.pow(Math.E, mCameraZoomD * aIntReduce);
		float logCameraZoom = (float) Math.log(iCameraZoom);
		logCameraZoom = smooth(aDelta, aReduce, aIntReduce, logCameraZoom, mCameraZoomD, LOG_ZOOM_MIN, LOG_ZOOM_MAX);
		iCameraZoom = (float) Math.pow(Math.E, logCameraZoom);
		mCameraZoomD *= aReduce;
	}

	private void smoothXY(float aDelta, float aReduce, float aIntReduce) {
		// iCameraX += mCameraXD * aIntReduce;
		// iCameraY += mCameraYD * aIntReduce;
		iCameraX = smooth(aDelta, aReduce, aIntReduce, iCameraX, mCameraXD, 0, Board.WIDTH);
		iCameraY = smooth(aDelta, aReduce, aIntReduce, iCameraY, mCameraYD, 0, Board.HEIGHT);
		mCameraXD *= aReduce;
		mCameraYD *= aReduce;
	}

	private float smooth(float aDelta, float aReduce, float aIntReduce, float aS0, float aV, float aMin, float aMax) {
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
