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

		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}

	@Override
	public void onScreenRender(float delta) {
		GL10 gl = Gdx.graphics.getGL10();

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (iCameraUpdate) {
			mCamera.zoom = iCameraZoom;
			mCamera.position.x = iCameraX;
			mCamera.position.y = iCameraY;
			mCamera.update();
			iCameraUpdate = false;
		}
		mCamera.apply(gl);

		mTexture0.bind();
		mMesh0.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mTexture1.bind();
		mMesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public void onScreenResize() {
		float w = (iScreenWidth > iScreenHeight) ? (((float) iScreenWidth) / iScreenHeight) : 1;
		float h = (iScreenWidth > iScreenHeight) ? 1 : (((float) iScreenHeight) / iScreenWidth);
		mCamera.viewportWidth = w;
		mCamera.viewportHeight = h;
		iCameraUpdate = true;
	}
}
