package com.tempura.bomberman.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Core extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private SplashWorker splashWorker;
	
	@Override
	public void create() {
		splashWorker.closeSplashScreen();
		batch = new SpriteBatch();
		img = new Texture("sprites/tempuragames.png");
	}

	@Override
	public void render() {
		super.render();
		ScreenUtils.clear(Color.BLACK);
		batch.begin();
		batch.draw(img,100,55);
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
	
	public SplashWorker getSplashWorker() {
		return splashWorker;
	}
	
	public void setSplashWorker(SplashWorker splashWorker) {
		this.splashWorker = splashWorker;
	}
}
