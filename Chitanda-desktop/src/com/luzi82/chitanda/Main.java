package com.luzi82.chitanda;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.luzi82.chitanda.client.ChitandaGame;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Chitanda";
		cfg.useGL20 = false;
		cfg.width = 480*2;
		cfg.height = 320*2;
		
		new LwjglApplication(new ChitandaGame(), cfg);
	}
}
