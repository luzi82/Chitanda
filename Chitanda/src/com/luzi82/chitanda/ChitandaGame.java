package com.luzi82.chitanda;

import com.badlogic.gdx.Game;
import com.luzi82.chitanda.game.ui.GameScreen;

public class ChitandaGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen(this));
	}

}
