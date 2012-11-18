package com.luzi82.chitanda;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.luzi82.chitanda.game.ui.GameScreen;

public class ChitandaGame extends Game {

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.log("asdf", "create");
		setScreen(new GameScreen(this));
	}

}
