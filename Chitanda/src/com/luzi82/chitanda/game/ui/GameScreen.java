package com.luzi82.chitanda.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.luzi82.chitanda.ChitandaGame;
import com.luzi82.chitanda.game.logic.Board;
import com.luzi82.gdx.GrScreen;

public class GameScreen extends GrScreen<ChitandaGame> {

	private Board mBoard;

	private OrthographicCamera mCamera;
	private SpriteBatch mBatch;
	private Pixmap mPixmap0;
	private Pixmap mPixmap1;
	private Texture mTexture0;
	private Texture mTexture1;
	private Sprite mSprite0;
	private Sprite mSprite1;

	private float iCameraZoom;
	private float iCameraX;
	private float iCameraY;
	private boolean iCameraUpdate;

	public GameScreen(ChitandaGame aParent) {
		super(aParent);
	}
	
	@Override
	protected void onScreenShow() {
		iCameraZoom = Math.min(Board.WIDTH, Board.HEIGHT);
		iCameraX = Board.WIDTH / 2;
		iCameraY = Board.HEIGHT / 2;
		iCameraUpdate = true;
	}

	@Override
	protected void onScreenLoad() {
		mBoard = new Board();
		mBoard.setAll(true);

		mCamera = new OrthographicCamera();
		mBatch = new SpriteBatch();

		mPixmap0 = new Pixmap(Gdx.files.internal("data/chitanda0.png"));
		mPixmap1 = new Pixmap(Gdx.files.internal("data/chitanda1.png"));
		mTexture0 = new Texture(mPixmap0, true);
		mTexture1 = new Texture(mPixmap1, true);
		mSprite0 = new Sprite(mTexture0);
		mSprite1 = new Sprite(mTexture1);

		// TODO un-hardcode
		mSprite0.setSize(1024 * 4, 1024 * 4);
		mSprite0.setOrigin(0, 0);
		mSprite0.setPosition(0, 0);
		mSprite1.setSize(1024 * 4, 1024 * 4);
		mSprite1.setOrigin(0, 0);
		mSprite1.setPosition(1024 * 4, 0);
	}

	@Override
	public void onScreenRender(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (iCameraUpdate) {
			mCamera.zoom = iCameraZoom;
			mCamera.position.x = iCameraX;
			mCamera.position.y = iCameraY;
			mCamera.update();
			mBatch.setProjectionMatrix(mCamera.combined);
		}

		mBatch.begin();
		mSprite0.draw(mBatch);
		mSprite1.draw(mBatch);
		mBatch.end();
	}

	@Override
	public void onScreenResize() {
		float w = (iScreenWidth > iScreenHeight) ? (((float) iScreenWidth) / iScreenHeight) : 1;
		float h = (iScreenWidth > iScreenHeight) ? 1 : (((float) iScreenHeight) / iScreenWidth);
		mCamera.viewportWidth = w;
		mCamera.viewportHeight = h;
	}
}
