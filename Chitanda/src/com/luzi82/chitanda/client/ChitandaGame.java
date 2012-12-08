package com.luzi82.chitanda.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.luzi82.chitanda.client.game.ui.GameScreen;
import com.luzi82.gdx.GrGame;

public class ChitandaGame extends GrGame {

	@Override
	public void create() {
		super.create();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		setScreen(new GameScreen(this));
	}

}
