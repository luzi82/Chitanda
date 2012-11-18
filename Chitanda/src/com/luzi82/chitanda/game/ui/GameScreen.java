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

	public GameScreen(ChitandaGame aParent) {
		super(aParent);
	}

	public void show() {
		super.show();

		mBoard = new Board();
		mBoard.setAll(true);

		mCamera = new OrthographicCamera();
		mBatch = new SpriteBatch();

		updateZoom();

		mPixmap0 = new Pixmap(Gdx.files.internal("data/chitanda0.png"));
		mPixmap1 = new Pixmap(Gdx.files.internal("data/chitanda1.png"));
		mTexture0 = new Texture(mPixmap0,true);
		mTexture1 = new Texture(mPixmap1,true);
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
	public void render(float delta) {
		super.render(delta);

		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		mCamera.zoom=1024*4;
		mCamera.position.x=Board.WIDTH/2;
		mCamera.position.y=Board.HEIGHT/2;
		mCamera.update();
		
		mBatch.setProjectionMatrix(mCamera.combined);
		mBatch.begin();
		mSprite0.draw(mBatch);
		mSprite1.draw(mBatch);
		mBatch.end();
	}
	
	@Override
	public void resize(int aWidth, int aHeight) {
		super.resize(aWidth, aHeight);
		updateZoom();
	}

	private void updateZoom(){
		float w = (mScreenWidth > mScreenHeight) ? (((float) mScreenWidth) / mScreenHeight) : 1;
		float h = (mScreenWidth > mScreenHeight) ? 1 : (((float) mScreenHeight) / mScreenWidth);
		mCamera.viewportWidth=w;
		mCamera.viewportHeight=h;
	}
}
