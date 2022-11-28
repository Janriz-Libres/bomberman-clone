package com.tempura.bomberman;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tempura.bomberman.Screens.Core;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Bomberman Ultra");
		config.setWindowedMode(BomberGame.V_WIDTH * 3, BomberGame.V_HEIGHT * 3);
		config.useVsync(true);
		Core core = new Core();
		core.setSplashWorker(new DesktopSplashWorker());
		new Lwjgl3Application(core, config);
	}
}
