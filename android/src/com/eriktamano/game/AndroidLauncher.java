package com.eriktamano.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.eriktamano.game.Animator;
import com.github.czyzby.websocket.CommonWebSockets;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonWebSockets.initiate();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Animator(), config);
	}
}
