package com.tempura.bomberman.Screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.tempura.bomberman.BomberGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameOverScreen implements Screen {
		private OrthographicCamera gameCam;
		private Viewport viewport;
		private Stage stage;
		private BomberGame game;
		
		public GameOverScreen(BomberGame game, int player) {
			this.game = game;
			
			game.victoryMusic.play();
			gameCam = new OrthographicCamera();
			viewport = new FitViewport(BomberGame.V_WIDTH, BomberGame.V_HEIGHT, new OrthographicCamera());
			stage = new Stage(viewport, ((BomberGame) game).batch);
			
			gameCam.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
			
			Label.LabelStyle style =new Label.LabelStyle(game.font, Color.WHITE);
			
			Table table = new Table();
			table.center();
			table.setFillParent(true);
			
			Label gameoverLabel = new Label("The Winner is  Player " + player + "!!!" , style);
			Label returnLabel = new Label("Press/click the screen to return to main menu", style);
		
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
		update(delta);
		ScreenUtils.clear(Color.GRAY);
		
		if(Gdx.input.justTouched()) {
			game.hud.resetScores();
			game.victoryMusic.stop();
			game.menu.menuMusic.play();
			game.setScreen(game.menu);
		}
		
		stage.draw();
	}

	public void update(float delta) {
		gameCam.update();
		
	}
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		
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
