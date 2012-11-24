package com.luzi82.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

public abstract class GrGame extends Game implements InputProcessor {

	GrScreen<?> mCurrentScreen;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void setScreen(Screen aScreen) {
		super.setScreen(aScreen);
		if (aScreen instanceof GrScreen) {
			mCurrentScreen = (GrScreen<?>) aScreen;
		} else {
			mCurrentScreen = null;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (mCurrentScreen == null)
			return false;
		return mCurrentScreen.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		if (mCurrentScreen == null)
			return false;
		return mCurrentScreen.keyUp(keycode);
	}

	@Override
	public boolean keyTyped(char character) {
		if (mCurrentScreen == null)
			return false;
		return mCurrentScreen.keyTyped(character);
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (mCurrentScreen == null)
			return false;
		return mCurrentScreen.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (mCurrentScreen == null)
			return false;
		return mCurrentScreen.touchUp(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if (mCurrentScreen == null)
			return false;
		return mCurrentScreen.touchDragged(x, y, pointer);
	}

	@Override
	public boolean touchMoved(int x, int y) {
		if (mCurrentScreen == null)
			return false;
		return mCurrentScreen.touchMoved(x, y);
	}

	@Override
	public boolean scrolled(int amount) {
		if (mCurrentScreen == null)
			return false;
		return mCurrentScreen.scrolled(amount);
	}

}