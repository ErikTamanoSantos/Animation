package com.eriktamano.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.eriktamano.game.Animator;
import com.github.czyzby.websocket.CommonWebSockets;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		CommonWebSockets.initiate();
		config.setForegroundFPS(60);
		config.setWindowedMode(1600, 800);
		config.setTitle("Animation");
		new Lwjgl3Application(new Animator(), config);
	}
}
