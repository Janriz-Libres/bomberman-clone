package com.tempura.bomberman.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tempura.bomberman.BomberGame;

public class LogoScreen extends BomberScreen {
	SpriteBatch batch;
	Texture img;
	
	private Viewport viewport;
	
	private BomberGame game;
	private float timer;
	
	public LogoScreen(BomberGame game) {
		this.game = game;
		
		viewport = new FitViewport(BomberGame.V_WIDTH / BomberGame.PPM,
				BomberGame.V_HEIGHT / BomberGame.PPM, new OrthographicCamera());
		batch = new SpriteBatch();
		img = new Texture("sprites/tempuragames.png");
		timer = 0;
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(Color.BLACK);
		batch.begin();
		batch.draw(img,100,55);
		batch.end();
		
		timer += delta;
		
		if (timer >= 4) {
			game.setScreen(game.menu);
			game.menu.menuMusic.play();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}
