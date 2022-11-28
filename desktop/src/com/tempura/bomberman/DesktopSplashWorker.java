package com.tempura.bomberman;

import java.awt.SplashScreen;
import com.tempura.bomberman.Screens.SplashWorker;

public class DesktopSplashWorker implements SplashWorker {
	
	@Override
	public void closeSplashScreen() {
		SplashScreen splash = SplashScreen.getSplashScreen();
		if(splash != null) {
			splash.close();
		}
	}
}
