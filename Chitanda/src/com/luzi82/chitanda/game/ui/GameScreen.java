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

	private Board mBoard;

	private OrthographicCamera mCamera;
	// private SpriteBatch mBatch;
	private Pixmap mPixmap0;
	private Pixmap mPixmap1;
	private Texture mTexture0;
	private Texture mTexture1;
	// private Sprite mSprite0;
	// private Sprite mSprite1;
	private Mesh mMesh0;
	private Mesh mMesh1;

	private float mViewPortWidth;
	private float mViewPortHeight;

	private float iCameraZoom;
	private float iCameraX;
	private float iCameraY;
	private boolean mCameraUpdate;

	private boolean mNewTouch;
	private boolean[] mTouching;
	private int[] mTouchX;
	private int[] mTouchY;
	// private boolean[] mTouching;
	// private boolean[] mMoved;
	private float mTouchStartCameraX;
	private float mTouchStartCameraY;
	private float mTouchStartDiff;
	private float mTouchStartCameraZoom;
//	private boolean mMoveEnabled;

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

		mPixmap0 = new Pixmap(Gdx.files.internal("data/chitanda0.png"));
		mPixmap1 = new Pixmap(Gdx.files.internal("data/chitanda1.png"));
		mTexture0 = new Texture(mPixmap0, true);
		mTexture1 = new Texture(mPixmap1, true);

		VertexAttributes va = new VertexAttributes( //
				new VertexAttribute(VertexAttributes.Usage.Position, 3, "position"),//
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "texturecoordinates")//
		);

		mMesh0 = new Mesh(true, 4, 4, va);
		mMesh0.setVertices(new float[] { //
		0f, 1024f * 4, 0f, 0f, 0f,//
				1024f * 4, 1024f * 4, 0f, 1f, 0f,//
				0f, 0f, 0f, 0f, 1f,//
				1024f * 4, 0f, 0f, 1f, 1f,//
		});
		mMesh0.setIndices(new short[] { 0, 1, 2, 3 });
		mMesh1 = new Mesh(true, 4, 4, va);
		mMesh1.setVertices(new float[] { //
		1024f * 4, 1024f * 4, 0f, 0f, 0f,//
				1024f * 8, 1024f * 4, 0f, 1f, 0f,//
				1024f * 4, 0f, 0f, 0f, 1f,//
				1024f * 8, 0f, 0f, 1f, 1f,//
		});
		mMesh1.setIndices(new short[] { 0, 1, 2, 3 });

		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		// mTouching = new boolean[TOUCH_MAX];
		// mLastX = new int[TOUCH_MAX];
		// mLastY = new int[TOUCH_MAX];
		// mSumX = new int[TOUCH_MAX];
		// mSumY = new int[TOUCH_MAX];
		mNewTouch = true;
		mTouching = new boolean[TOUCH_MAX];
		mTouchX = new int[TOUCH_MAX];
		mTouchY = new int[TOUCH_MAX];
	}

	@Override
	public void onScreenRender(float delta) {
		int i;

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
				mNewTouch=false;
			} else {
				if (touchCount > 1) {
					iCameraZoom = mTouchStartCameraZoom * mTouchStartDiff / touchDiff;
				}
				iCameraX = (iCameraZoom * mViewPortWidth) * (0.5f - touchXAvg / mScreenWidth) + mTouchStartCameraX;
				iCameraY = (iCameraZoom * mViewPortHeight) * (0.5f + touchYAvg / mScreenHeight - 1) + mTouchStartCameraY;
				mCameraUpdate = true;
			}
		}

		if (mCameraUpdate) {
			mCamera.zoom = iCameraZoom;
			mCamera.position.x = iCameraX;
			mCamera.position.y = iCameraY;
			mCamera.update();
			mCameraUpdate = false;
		}
		mCamera.apply(gl);

		mTexture0.bind();
		mMesh0.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mTexture1.bind();
		mMesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
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
		mTouching[pointer] = true;
		mTouchX[pointer] = x;
		mTouchY[pointer] = y;
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		mNewTouch = true;
		mTouching[pointer] = false;
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		mTouching[pointer] = true;
		mTouchX[pointer] = x;
		mTouchY[pointer] = y;
		return true;
	}

}
