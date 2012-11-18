package com.luzi82.gdx;

import java.lang.reflect.Field;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;

public abstract class GrScreen<G extends Game> implements Screen {

	protected G mParent;
	protected int mScreenWidth;
	protected int mScreenHeight;

	protected Logger mLogger = new Logger(this.getClass().getSimpleName());

	protected GrScreen(G aParent) {
		mParent = aParent;
		mLogger.setLevel(Logger.DEBUG);
	}

	@Override
	public void resize(int aWidth, int aHeight) {
		mScreenWidth = aWidth;
		mScreenHeight = aHeight;
	}

	@Override
	public void show() {
		mScreenWidth = Gdx.graphics.getWidth();
		mScreenHeight = Gdx.graphics.getHeight();
	}

	@Override
	public void resume() {
		mScreenWidth = Gdx.graphics.getWidth();
		mScreenHeight = Gdx.graphics.getHeight();
	}

	@Override
	public void render(float delta) {
	}

	@Override
	public void hide() {
		disposeMember();
	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
		disposeMember();
	}

	private void disposeMember() {
		for (Class<?> c = this.getClass(); c != GrScreen.class; c = c.getSuperclass()) {
			Field[] fv = c.getDeclaredFields();
			for (Field f : fv) {
				String n = f.getName();
				f.setAccessible(true);
				if (n.startsWith("m")) {
					try {
						Object o = f.get(this);
						if (o instanceof Disposable) {
							Disposable d = (Disposable) o;
							d.dispose();
						}
						f.set(this, null);
					} catch (IllegalArgumentException e) {
					} catch (IllegalAccessException e) {
					}
				}
				f.setAccessible(false);
			}
		}
	}

}
