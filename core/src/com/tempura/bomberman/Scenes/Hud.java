package com.tempura.bomberman.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tempura.bomberman.BomberGame;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Hud{
	public Stage stage;
	private Viewport viewport;
	
	private Integer worldTimer;
	private float timeCount;

	private Integer player1Score;
	private Integer player2Score;
	private FreeTypeFontGenerator fontGenerator;
	private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	private BitmapFont font;
	
	Label player1;
	Label player2;
	Label timeLabel;
	Label countdownLabel;
	Label ScoreLabel1;
	Label ScoreLabel2;
	
	public Hud(SpriteBatch batch) {
		worldTimer = 300;
		player1Score = 0;
		player2Score = 0;
		
		viewport = new FitViewport(BomberGame.V_WIDTH,
				(BomberGame.V_HEIGHT + 60), new OrthographicCamera());
		stage = new Stage(viewport,batch);
		
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("sprites\\bm.ttf"));
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 72;
		fontParameter.borderWidth = 5.0f;
		fontParameter.borderColor = Color.BLACK;
		font = fontGenerator.generateFont(fontParameter);
		font.getData().setScale(0.12f);
		
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		Label.LabelStyle fontStyle =new Label.LabelStyle(font, Color.WHITE);
			
		player1 = new Label("Player 1", fontStyle);
		player2 = new Label("Player 2", fontStyle);
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
	
	public void setScore1(int score) {
		player1Score += score;
		ScoreLabel1.setText(player1Score);
	}
	
	public void setScore2(int score) {
		player2Score += score;
		ScoreLabel2.setText(player2Score);
	}

	public void setTimer() {
		//worldTimer -= Gdx.graphics.getDeltaTime();
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
	 	if(timeCount >= 1) {
	 		worldTimer--;
	 	countdownLabel.setText(String.format("%03d", worldTimer));
	 	timeCount = 0;
	 	
	 		}
	 	}
	 	
	 	public void addScore(int playerNum) {
	 		if(playerNum == 1) {
	 			player1Score++;
	 			player1.setText(String.format("%01d", player1Score));
	 		}
	 		if(playerNum == 2) {
	 			player2Score++;
	 			player2.setText(String.format("%01d", player2Score));
	 		}
	 	}
	 	public void dispose() {
	 		stage.dispose();
	 	}
	 
}
