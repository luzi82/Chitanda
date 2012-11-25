package com.luzi82.gdx;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;

public abstract class GrScreen<G extends GrGame> implements Screen, InputProcessor {

	protected G iParent;
	protected int mScreenWidth;
	protected int mScreenHeight;

	private boolean iMemberLoaded = false;

	protected Logger iLogger = new Logger(this.getClass().getSimpleName(), Logger.DEBUG);

	protected GrScreen(G aParent) {
		iParent = aParent;
	}

	@Override
	public final void show() {
		iLogger.debug("show");
		load();
		onScreenShow();
	}

	@Override
	public final void resume() {
		iLogger.debug("resume");
		load();
		onScreenResume();
	}

	@Override
	public final void resize(int aWidth, int aHeight) {
		iLogger.debug("resize");
		mScreenWidth = aWidth;
		mScreenHeight = aHeight;
		if (iMemberLoaded)
			onScreenResize();
	}

	@Override
	public final void render(float aDelta) {
		onScreenRender(aDelta);
	}

	@Override
	public final void hide() {
		iLogger.debug("hide");
		onScreenHide();
		disposeMember();
	}

	@Override
	public final void pause() {
		iLogger.debug("pause");
		onScreenPause();
		disposeMember();
	}

	@Override
	public final void dispose() {
		iLogger.debug("dispose");
		onScreenDispose();
		disposeMember();
	}

	private void load() {
		if (iMemberLoaded)
			return;
		iMemberLoaded = true;
		onScreenLoad();
	}

	private void disposeMember() {
		iLogger.debug("disposeMember");
		for (Class<?> c = this.getClass(); c != GrScreen.class; c = c.getSuperclass()) {
			Field[] fv = c.getDeclaredFields();
			for (Field f : fv) {
				String n = f.getName();
				f.setAccessible(true);
				if (n.startsWith("m")) {
					iLogger.debug(n);
					try {
						Object o = f.get(this);
						deepDispose(o);
						if (!f.getType().isPrimitive())
							f.set(this, null);
					} catch (IllegalArgumentException e) {
						iLogger.debug("", e);
					} catch (IllegalAccessException e) {
						iLogger.debug("", e);
					}
				}
				f.setAccessible(false);
			}
		}
		iMemberLoaded = false;
	}

	private void deepDispose(Object mObject) {
		if (mObject == null)
			return;
		Class<?> c = mObject.getClass();
		if (c.isPrimitive()) {
			// do nothing
		} else if (Disposable.class.isAssignableFrom(c)) {
			Disposable d = (Disposable) mObject;
			d.dispose();
		} else if (c.isArray()) {
			Class<?> cc = c.getComponentType();
			if (cc.isPrimitive()) {
				// do nothing
			} else {
				for (int i = 0; i < Array.getLength(mObject); ++i) {
					Object o = Array.get(mObject, i);
					deepDispose(o);
					Array.set(mObject, i, null);
				}
			}
		}
	}

	protected void onScreenLoad() {
		// dummy
	}

	protected void onScreenResize() {
		// dummy
	}

	protected void onScreenShow() {
		// dummy
	}

	protected void onScreenResume() {
		// dummy
	}

	protected void onScreenRender(float aDelta) {
		// dummy
	}

	protected void onScreenHide() {
		// dummy
	}

	protected void onScreenPause() {
		// dummy
	}

	protected void onScreenDispose() {
		// dummy
	}

	@Override
	public boolean keyDown(int keycode) {
		// dummy
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// dummy
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// dummy
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// dummy
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// dummy
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// dummy
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// dummy
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// dummy
		return false;
	}

}
