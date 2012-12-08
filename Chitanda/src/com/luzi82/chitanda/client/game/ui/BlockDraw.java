package com.luzi82.chitanda.client.game.ui;

import java.util.Arrays;
import java.util.TreeMap;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Disposable;
import com.luzi82.chitanda.client.Const;
import com.luzi82.chitanda.client.game.logic.Board;
import com.luzi82.gdx.GrDeepDispose;

public class BlockDraw implements Disposable {

	private GameScreen pGameScreen;

	private Mesh mBlockMesh;
	private Mesh mBlockGroupMesh;

	private static final int LAYER_COUNT = 3;
	private CellTexture[][] mCellTextureV;
	private TreeMap<Integer, CellTexture>[] mCellTextureM;
	private Pixmap mCellTexturePixmap;

	public BlockDraw(GameScreen aGameScreen) {
		pGameScreen = aGameScreen;

		VertexAttributes va;

		va = new VertexAttributes( //
				new VertexAttribute(VertexAttributes.Usage.Position, 3, "position") //
		);

		mBlockMesh = new Mesh(true, 4, 4, va);
		mBlockMesh.setVertices(new float[] {//
				0f, 1f, 0f,//
						1f, 1f, 0f,//
						0f, 0f, 0f,//
						1f, 0f, 0f,//
				});
		mBlockMesh.setIndices(new short[] { 0, 1, 2, 3 });

		va = new VertexAttributes( //
				new VertexAttribute(VertexAttributes.Usage.Position, 3, "position"),//
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "texturecoordinates")//
		);

		mBlockGroupMesh = new Mesh(true, 4, 4, va);
		mBlockGroupMesh.setVertices(new float[] { //
				0f, 1f, 0f, 0f, 1f,//
						1f, 1f, 0f, 1f, 1f,//
						0f, 0f, 0f, 0f, 0f,//
						1f, 0f, 0f, 1f, 0f,//
				});
		mBlockGroupMesh.setIndices(new short[] { 0, 1, 2, 3 });

		mCellTexturePixmap = new Pixmap(CELLTEXTURE_SIZE, CELLTEXTURE_SIZE, Pixmap.Format.RGBA8888);
	}

	public void drawBlock(GL10 aGl) {
		int screenWidth = pGameScreen.getScreenWidth();
		int screenHeight = pGameScreen.getScreenHeight();
		int minSide = Math.min(screenWidth, screenHeight);
		float blockPerPixel = pGameScreen.mCameraCalc.iCameraRealZoom / minSide;
		if (blockPerPixel > pGameScreen.mBlockPerPixelBorder) {
			int layer = (blockPerPixel > 4) ? 2 //
					: (blockPerPixel > 1) ? 1 //
							: 0;
			aGl.glEnable(GL10.GL_BLEND);
			aGl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			aGl.glEnable(GL10.GL_TEXTURE_2D);
			aGl.glColor4f(1f, 1f, 1f, 1f);
			int minBX = (int) Math.floor(pGameScreen.mCameraCalc.screenToBoardRealX(0));
			int maxBX = (int) Math.ceil(pGameScreen.mCameraCalc.screenToBoardRealX(screenWidth));
			int minBY = (int) Math.floor(pGameScreen.mCameraCalc.screenToBoardRealY(screenHeight));
			int maxBY = (int) Math.ceil(pGameScreen.mCameraCalc.screenToBoardRealY(0));
			minBX = Const.minMax(0, minBX, Board.WIDTH);
			maxBX = Const.minMax(0, maxBX, Board.WIDTH);
			minBY = Const.minMax(0, minBY, Board.HEIGHT);
			maxBY = Const.minMax(0, maxBY, Board.HEIGHT);
			updateCellContent(minBX, maxBX, minBY, maxBY, layer);
			drawCellTextureV(aGl, minBX, maxBX, minBY, maxBY, layer);
			// aGl.glColor4f(1f, 1f, 1f, 1f);
		} else {
			aGl.glDisable(GL10.GL_BLEND);
			aGl.glBlendFunc(GL10.GL_ONE, GL10.GL_ZERO);
			aGl.glDisable(GL10.GL_TEXTURE_2D);
			int minX = (int) Math.floor(pGameScreen.mCameraCalc.screenToBoardRealX(0));
			int maxX = (int) Math.ceil(pGameScreen.mCameraCalc.screenToBoardRealX(screenWidth));
			int minY = (int) Math.floor(pGameScreen.mCameraCalc.screenToBoardRealY(screenHeight));
			int maxY = (int) Math.ceil(pGameScreen.mCameraCalc.screenToBoardRealY(0));
			minX = Const.minMax(0, minX, Board.WIDTH);
			maxX = Const.minMax(0, maxX, Board.WIDTH);
			minY = Const.minMax(0, minY, Board.HEIGHT);
			maxY = Const.minMax(0, maxY, Board.HEIGHT);
			for (int x = minX; x < maxX; ++x) {
				aGl.glPushMatrix();
				aGl.glTranslatef(x, 0, 0);
				for (int y = minY; y < maxY; ++y) {
					if (pGameScreen.mBoard.get0(x, y)) {
						aGl.glPushMatrix();
						aGl.glTranslatef(0, y, 0);
						if ((x + y) % 2 == 0) {
							aGl.glColor4f(0f, 0f, 0f, 1f);
						} else {
							aGl.glColor4f(0.1f, 0.1f, 0.1f, 1f);
						}
						mBlockMesh.render(GL10.GL_TRIANGLE_STRIP);
						aGl.glPopMatrix();
					}
				}
				aGl.glPopMatrix();
			}
			aGl.glColor4f(1f, 1f, 1f, 1f);
		}
	}

	static final private int CELLTEXTURE_SIZE = 64;

	private void drawCellTextureV(GL10 aGl, float aMinBX, float aMaxBX, float aMinBY, float aMaxBY, int aLayer) {
		int layerShift = 2 * aLayer;
		int minCX = ((int) Math.floor(aMinBX / (CELLTEXTURE_SIZE << layerShift)));
		int maxCX = ((int) Math.ceil(aMaxBX / (CELLTEXTURE_SIZE << layerShift)));
		int minCY = ((int) Math.floor(aMinBY / (CELLTEXTURE_SIZE << layerShift)));
		int maxCY = ((int) Math.ceil(aMaxBY / (CELLTEXTURE_SIZE << layerShift)));
		// iLogger.debug("minCX " + minCX);
		aGl.glPushMatrix();
		aGl.glScalef(CELLTEXTURE_SIZE << layerShift, CELLTEXTURE_SIZE << layerShift, 1);
		int idx = -1;
		for (int cx = minCX; cx < maxCX; ++cx) {
			aGl.glPushMatrix();
			aGl.glTranslatef(cx, 0, 0);
			for (int cy = minCY; cy < maxCY; ++cy) {
				try {
					aGl.glPushMatrix();
					aGl.glTranslatef(0, cy, 0);
					idx = (cx << 16) | cy;
					CellTexture ct;
					ct = mCellTextureM[aLayer].get(idx);
					ct.mTexture.bind();
					mBlockGroupMesh.render(GL10.GL_TRIANGLE_STRIP);
					// mBlockMesh.render(GL10.GL_TRIANGLE_STRIP);
					aGl.glPopMatrix();
				} catch (NullPointerException npe) {
					// iLogger.debug(String.format("npe %08x %d", idx,
					// mBoard.getVersion()));
					throw npe;
				}
			}
			aGl.glPopMatrix();
		}
		aGl.glPopMatrix();
	}

	@SuppressWarnings("unchecked")
	public void updateCellTextureV() {
		// pGameScreen.iLogger.debug("updateCellTextureV");

		if (mCellTextureV == null)
			mCellTextureV = new CellTexture[LAYER_COUNT][];
		if (mCellTextureM == null)
			mCellTextureM = new TreeMap[LAYER_COUNT];

		// TODO object reuse
		int ctvw = ((pGameScreen.getScreenWidth() + (CELLTEXTURE_SIZE - 1)) / CELLTEXTURE_SIZE) + 1;
		int ctvh = ((pGameScreen.getScreenHeight() + (CELLTEXTURE_SIZE - 1)) / CELLTEXTURE_SIZE) + 1;
		int len = ctvw * ctvh;
		for (int layer = 0; layer < LAYER_COUNT; ++layer) {
			if (mCellTextureV[layer] != null) {
				if (mCellTextureV[layer].length == len)
					continue;
				for (int i = 0; i < mCellTextureV[layer].length; ++i) {
					CellTexture ct = mCellTextureV[layer][i];
					if (ct != null)
						ct.dispose();
					mCellTextureV[layer][i] = null;
				}
				mCellTextureV[layer] = null;
			}
			mCellTextureM[layer] = new TreeMap<Integer, CellTexture>();
			mCellTextureV[layer] = new CellTexture[len];
			for (int i = 0; i < len; ++i) {
				mCellTextureV[layer][i] = new CellTexture();
			}
		}
	}

	private void updateCellContent(float aMinBX, float aMaxBX, float aMinBY, float aMaxBY, int aLayer) {
		int layerShift = 2 * aLayer;
		int minCX = ((int) Math.floor(aMinBX / (CELLTEXTURE_SIZE << layerShift)));
		int maxCX = ((int) Math.ceil(aMaxBX / (CELLTEXTURE_SIZE << layerShift)));
		int minCY = ((int) Math.floor(aMinBY / (CELLTEXTURE_SIZE << layerShift)));
		int maxCY = ((int) Math.ceil(aMaxBY / (CELLTEXTURE_SIZE << layerShift)));
		boolean good = true;
		fullTest: for (int cx = minCX; cx < maxCX; ++cx) {
			for (int cy = minCY; cy < maxCY; ++cy) {
				int idx = (cx << 16) | cy;
				if (!mCellTextureM[aLayer].containsKey(idx)) {
					good = false;
					break fullTest;
				}
			}
		}
		if (!good) {
			// iLogger.debug("!good");
			int offset = 0;
			sortCellTextureV(aMinBX, aMaxBX, aMinBY, aMaxBY, aLayer);
			for (int cx = minCX; cx < maxCX; ++cx) {
				for (int cy = minCY; cy < maxCY; ++cy) {
					int idx = (cx << 16) | cy;
					if (!mCellTextureM[aLayer].containsKey(idx)) {
						CellTexture ct = mCellTextureV[aLayer][offset++];
						if (ct.mDistanceSq <= 0) {
							throw new AssertionError();
						}
						// iLogger.debug(String.format("remove %08x", ct.mIdx));
						if (ct.mUpdate) {
							mCellTextureM[aLayer].remove(ct.mIdx);
						}
						ct.update(cx, cy, idx, aLayer);
						mCellTextureM[aLayer].put(idx, ct);
					}
				}
			}
		}

		// for (int i = 0; i < mCellTextureV[aLayer].length; ++i) {
		// mCellTextureV[aLayer][i].calcDistance(aMinBX, aMaxBX, aMinBY,
		// aMaxBY);
		// }
		// int c = 0;
		// for (CellTexture ct : mCellTextureV[aLayer]) {
		// if (ct.mDistanceSq <= 0) {
		// ++c;
		// }
		// }
		// iLogger.debug("c " + c);
	}

	private void sortCellTextureV(float aMinBX, float aMaxBX, float aMinBY, float aMaxBY, int aLayer) {
		for (int i = 0; i < mCellTextureV[aLayer].length; ++i) {
			mCellTextureV[aLayer][i].calcDistance(aMinBX, aMaxBX, aMinBY, aMaxBY);
		}
		Arrays.sort(mCellTextureV[aLayer]);
	}

	private class CellTexture implements Disposable, Comparable<CellTexture> {
		int mCX = -1;
		int mCY = -1;
		int mIdx = -1;
		int mLayer = -1;
		// int mVersion = -1;
		boolean mUpdate = false;
		float mDistanceSq = 0;
		Texture mTexture = new Texture(CELLTEXTURE_SIZE, CELLTEXTURE_SIZE, Pixmap.Format.RGBA8888);

		@Override
		public void dispose() {
			if (mTexture != null)
				mTexture.dispose();
			mTexture = null;
		}

		@Override
		public int compareTo(CellTexture o) {
			// reverse sort mDistanceSq, farest get first
			return Float.compare(o.mDistanceSq, mDistanceSq);
		}

		public void calcDistance(float aMinBX, float aMaxBX, float aMinBY, float aMaxBY) {
			if ((!mUpdate) || (mCX == -1) || (mCY == -1) || (mLayer == -1)) {
				mDistanceSq = Float.MAX_VALUE;
				return;
			}
			float bx = mCX * CELLTEXTURE_SIZE << (mLayer * 2);
			float by = mCY * CELLTEXTURE_SIZE << (mLayer * 2);
			float t;
			aMinBX -= CELLTEXTURE_SIZE << (mLayer * 2);
			aMinBY -= CELLTEXTURE_SIZE << (mLayer * 2);
			mDistanceSq = 0;
			if (bx < aMinBX) {
				t = aMinBX - bx;
				mDistanceSq += t * t;
			}
			if (aMaxBX < bx) {
				t = bx - aMaxBX;
				mDistanceSq += t * t;
			}
			if (by < aMinBY) {
				t = aMinBY - by;
				mDistanceSq += t * t;
			}
			if (aMaxBY < by) {
				t = by - aMaxBY;
				mDistanceSq += t * t;
			}
		}

		public void update(int aCX, int aCY, int aIdx, int aLayer) {
			mCX = aCX;
			mCY = aCY;
			mIdx = aIdx;
			mLayer = aLayer;
			switch (aLayer) {
			case 0:
				pGameScreen.mBoard.writePixmap0(mCellTexturePixmap, mCX, mCY);
				break;
			case 1:
				pGameScreen.mBoard.writePixmap1(mCellTexturePixmap, mCX, mCY);
				break;
			case 2:
				pGameScreen.mBoard.writePixmap2(mCellTexturePixmap, mCX, mCY);
				break;
			default:
				throw new IllegalStateException();
			}
			mTexture.draw(mCellTexturePixmap, 0, 0);
			mUpdate = true;
		}
	}

	public void dirty(int aBX, int aBY) {
		int tx = aBX / CELLTEXTURE_SIZE;
		int ty = aBY / CELLTEXTURE_SIZE;
		for (int layer = 0; layer < LAYER_COUNT; ++layer) {
			CellTexture ct = mCellTextureM[layer].remove((tx << 16) | ty);
			if (ct != null) {
				ct.mUpdate = false;
			}
			tx >>= 2;
			ty >>= 2;
		}
	}

	@Override
	public void dispose() {
		GrDeepDispose.disposeMember(this, super.getClass());
	}

}
