package com.luzi82.chitanda.game.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.utils.Disposable;
import com.luzi82.gdx.GrGame;
import com.luzi82.gdx.GrScreen;

public class GrScreenTest {

	static int mStaticV;;

	@Test
	public void breadthDispose() {

		class D implements Disposable {
			@Override
			public void dispose() {
				++mStaticV;
			}
		}

		class T extends GrScreen<GrGame> {
			public D mD = new D();

			protected T(GrGame aParent) {
				super(aParent);
			}
		}

		T t = new T(null);
		mStaticV = 0;
		t.dispose();
		assertEquals(1, mStaticV);
		assertNull(t.mD);
	}

	@Test
	public void arrayDispose() {

		class D implements Disposable {
			@Override
			public void dispose() {
				++mStaticV;
			}
		}

		class T extends GrScreen<GrGame> {
			public D[] mD = new D[1];

			protected T(GrGame aParent) {
				super(aParent);
				mD[0] = new D();
			}
		}

		T t = new T(null);
		mStaticV = 0;
		t.dispose();
		assertEquals(1, mStaticV);
		assertNull(t.mD);
	}

	@Test
	public void array2Dispose() {

		class D implements Disposable {
			@Override
			public void dispose() {
				++mStaticV;
			}
		}

		class T extends GrScreen<GrGame> {
			public D[][] mD = new D[1][1];

			protected T(GrGame aParent) {
				super(aParent);
				mD[0][0] = new D();
			}
		}

		T t = new T(null);
		mStaticV = 0;
		t.dispose();
		assertEquals(1, mStaticV);
		assertNull(t.mD);
	}

}
