package com.luzi82.chitanda.game.logic;
import com.badlogic.gdx.utils.Disposable;
import com.luzi82.gdx.GrGame;
import com.luzi82.gdx.GrScreen;


public class GrScreenTest_staticNoDispose_T extends GrScreen<GrGame> {
	
	public static D mD = new D();

	static class D implements Disposable {
		@Override
		public void dispose() {
			++GrScreenTest.mStaticV;
		}
	}

	public GrScreenTest_staticNoDispose_T(GrGame aParent) {
		super(aParent);
	}

}
