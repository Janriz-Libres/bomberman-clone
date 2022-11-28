package com.tempura.bomberman.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tempura.bomberman.BomberGame;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Hud {
	public Stage stage;
	private Viewport viewport;

	private Integer worldTimer;
	private float timeCount;
	BomberGame game;
	public Integer player1Score;
	public Integer player2Score;

	Image player1;
	Image player2;
	Label timeLabel;
	Label countdownLabel;
	Label ScoreLabel1;
	Label ScoreLabel2;

	public Hud(SpriteBatch batch, int playerScore, int enemyScore, TextureRegion p1, TextureRegion p2,
			BomberGame game) {
		
		this.game = game;
		worldTimer = 300;
		player1Score = playerScore;
		player2Score = enemyScore;

		viewport = new FitViewport(BomberGame.V_WIDTH, (BomberGame.V_HEIGHT + 60), new OrthographicCamera());
		stage = new Stage(viewport, batch);

		Table table = new Table();
		table.top();
		table.setFillParent(true);
		Label.LabelStyle fontStyle = new Label.LabelStyle(game.font, Color.WHITE);

		player1 = new Image(p1);
		player2 = new Image(p2);

		timeLabel = new Label("Time", fontStyle);
		countdownLabel = new Label(String.format("%03d", worldTimer), fontStyle);
		ScoreLabel1 = new Label(String.format("%01d", player1Score), fontStyle);
		ScoreLabel2 = new Label(String.format("%01d", player2Score), fontStyle);

		table.add(player1).expandX().pad(1);
		table.add(player2).expandX().pad(1);
		table.add(timeLabel).expandX().pad(1);

		table.row();

		table.add(ScoreLabel1).expandX().pad(1);
		table.add(ScoreLabel2).expandX().pad(1);
		table.add(countdownLabel).expandX().pad(1);

		stage.addActor(table);
	}
	
	public void resetScores() {
		player1Score = 0;
		player2Score = 0;
		ScoreLabel1.setText(player1Score);
		ScoreLabel2.setText(player2Score);
	}

	public void setScore1(int score) {
		player1Score += score;
		ScoreLabel1.setText(player1Score);
	}

	public void setScore2(int score) {
		player2Score += score;
		ScoreLabel2.setText(player2Score);
	}

	public int getScore1() {
		return player1Score;
	}

	public int getScore2() {
		return player2Score;
	}

	public void setTimer() {
		// worldTimer -= Gdx.graphics.getDeltaTime();
		countdownLabel.setText((int) worldTimer);
	}

	public void resetTimer() {
		worldTimer = 300;
	}

	public float getTimer() {
		return worldTimer;
	}

	public void update(float dt) {
		timeCount += dt;
		if (timeCount >= 1) {
			worldTimer--;
			countdownLabel.setText(String.format("%03d", worldTimer));
			timeCount = 0;

		}
	}

	public void dispose() {
		stage.dispose();
	}

}
