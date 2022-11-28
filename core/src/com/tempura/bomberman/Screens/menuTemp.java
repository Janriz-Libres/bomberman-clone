package com.tempura.bomberman.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.tempura.bomberman.BomberGame;

public class menuTemp implements Screen {
	private static final int WIDTH = BomberGame.V_WIDTH * 3;
	private static final int HEIGHT = BomberGame.V_HEIGHT * 3;
	private static final int EXIT_BUTTON_WIDTH = 100 ;
	private static final int EXIT_BUTTON_HEIGHT = 40 ;
	private static final int PLAY_BUTTON_WIDTH = 100 ;
	private static final int PLAY_BUTTON_HEIGHT = 40 ;
	private static final int EXIT_BUTTON_Y = (HEIGHT / 2) - 40 ;
	private static final int PLAY_BUTTON_Y = (HEIGHT / 2) + 40 ;
	
	BomberGame game;
	
	Texture playButtonActive;
	Texture playButtonInactive;
	Texture exitButtonActive;
	Texture exitButtonInactive;
	
	public menuTemp(BomberGame game) {
		this.game = game;
		playButtonActive = new Texture("sprites/play_button_active.png");
		playButtonInactive = new Texture("sprites/play_button_inactive.png");
		exitButtonActive = new Texture("sprites/exit_button_active.png");
		exitButtonInactive = new Texture("sprites/exit_button_inactive.png");
	}
	@Override
	public void show() {
		
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		
		int x = (WIDTH / 2) - (EXIT_BUTTON_WIDTH / 2);
		if(Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && HEIGHT - Gdx.input.getY() > EXIT_BUTTON_Y ) {
			game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT );
			if(Gdx.input.isTouched()) {
				Gdx.app.exit();
			}
		}else {
			game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT );
		}
		
		x = (WIDTH / 2) - (PLAY_BUTTON_WIDTH / 2);
		if(Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x && HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && HEIGHT - Gdx.input.getY() > PLAY_BUTTON_Y ){
			game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH,PLAY_BUTTON_HEIGHT );
			if(Gdx.input.isTouched()) {
				this.dispose();
				game.setScreen(new PlayScreen(game));
			}
		}else {
			game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH,PLAY_BUTTON_HEIGHT );
		}
		
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		
	}

}
