package com.game.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.game.Game;

// Launcher of the libGDX class
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Game.TITLE;
		config.width = Game.WIDTH;
		config.height = Game.HEIGHT;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.resizable = false;
		new LwjglApplication(new Game(), config);
	}
}