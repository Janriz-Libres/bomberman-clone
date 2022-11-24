package com.tempura.bomberman.Scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.*;
import com.tempura.bomberman.BomberGame;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class Hud{
	public Stage stage;
	private Viewport viewport;
	
	private Integer worldTimer;
	private Integer player1Score;
	private Integer player2Score;
	
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
		
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		Label.LabelStyle font =new Label.LabelStyle(new BitmapFont(), Color.WHITE);
		
		player1 = new Label("Player 1", font);
		player2 = new Label("Player 2", font);
		timeLabel = new Label("Time", font);
		countdownLabel = new Label(String.format("%03d", worldTimer), font);
		ScoreLabel1 = new Label(String.format("%01d", player1Score), font);
		ScoreLabel2 = new Label(String.format("%01d", player2Score), font);
		
		table.add(player1).expandX().pad(1);
		table.add(player2).expandX().pad(1);
		table.add(timeLabel).expandX().pad(1);
		
		table.row();
		
		table.add(ScoreLabel1).expandX().pad(1);
		table.add(ScoreLabel2).expandX().pad(1);
		table.add(countdownLabel).expandX().pad(1);
		
		stage.addActor(table);	
	}
}
