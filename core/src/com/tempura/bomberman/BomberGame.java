package com.tempura.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tempura.bomberman.Screens.PlayScreen;

public class BomberGame extends Game {
	public static final int V_WIDTH = 240;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;
	
	public SpriteBatch batch;
	
	private PlayScreen playScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		playScreen = new PlayScreen(this);
		this.setScreen(playScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playScreen.dispose();
	}
}
