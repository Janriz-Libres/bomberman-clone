package com.tempura.bomberman.Screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.*;
import com.tempura.bomberman.BomberGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameOverScreen implements Screen {

		private Viewport viewport;
		private Stage stage;
		private BomberGame game;
		
		public GameOverScreen(BomberGame game, int player) {
			this.game = game;
			viewport = new FitViewport(BomberGame.V_WIDTH, BomberGame.V_HEIGHT, new OrthographicCamera());
			stage = new Stage(viewport, ((BomberGame) game).batch);
			
			Label.LabelStyle font =new Label.LabelStyle(new BitmapFont(), Color.WHITE);
			
			Table table = new Table();
			table.center();
			table.setFillParent(true);
			
			Label gameoverLabel = new Label("The Winner is  Player " + player + "!!!" , font);
			Label returnLabel = new Label("press any key to return to main menu", font);
		
			table.add(gameoverLabel).expandX();
			table.row();
			table.add(returnLabel).expandX().padTop(5f);
			
			stage.addActor(table);
			
		}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		if(Gdx.input.justTouched()) {
	//		game.setScreen(new MenuScreen((BomberGame) game));
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		
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
		// TODO Auto-generated method stub
		
	}

}
