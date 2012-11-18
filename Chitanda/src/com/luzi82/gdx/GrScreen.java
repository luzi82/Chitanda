package com.luzi82.gdx;

import java.lang.reflect.Field;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;

public abstract class GrScreen<G extends Game> implements Screen {

	protected G iParent;
	protected int iScreenWidth;
	protected int iScreenHeight;

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
		iScreenWidth = aWidth;
		iScreenHeight = aHeight;
		if(iMemberLoaded)
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
					try {
						Object o = f.get(this);
						if (o instanceof Disposable) {
							Disposable d = (Disposable) o;
							d.dispose();
						}
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
	
	protected void onScreenLoad() {
		// dummy
	}

	protected void onScreenResize(){
		// dummy
	}
	
	protected void onScreenShow(){
		// dummy
	}
	
	protected void onScreenResume(){
		// dummy
	}
	
	protected void onScreenRender(float aDelta){
		// dummy
	}
	
	protected void onScreenHide(){
		// dummy
	}

	protected void onScreenPause(){
		// dummy
	}
	
	protected void onScreenDispose(){
		// dummy
	}

}
