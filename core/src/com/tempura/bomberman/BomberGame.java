package com.tempura.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tempura.bomberman.Screens.PlayScreen;
import com.tempura.bomberman.Screens.menuTemp;

public class BomberGame extends Game {
	public static final int V_WIDTH = 240;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;
	
	public static final short DEFAULT_BIT = 1, PLAYER_BIT = 2, HEAVY_BLOCK_BIT  = 4,
			LIGHT_BLOCK_BIT = 8, DESTROYED_BLOCK_BIT = 16, PLAYER_BOMB_BIT = 32,
			ENEMY_BOMB_BIT = 64, OPAQUE_BOMB_BIT = 128, EXPLOSION_BIT = 256;
	
	public SpriteBatch batch;
	
	private PlayScreen playScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		menuTemp menu = new menuTemp(this);
		this.setScreen(menu);
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
