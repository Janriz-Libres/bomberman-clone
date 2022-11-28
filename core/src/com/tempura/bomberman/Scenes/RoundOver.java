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
import com.tempura.bomberman.Screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class RoundOver {

	public Stage stage;
	private Viewport viewport;
	private FreeTypeFontGenerator fontGenerator;
	private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	private BitmapFont font;
	private Label intermission;
	private Label countdown;
	private BomberGame game;
	private int timer; 
	private float timeCount;
	
	public RoundOver(SpriteBatch batch, BomberGame game) {
		
	this.game = game;
		
	viewport = new FitViewport(BomberGame.V_WIDTH,
			(BomberGame.V_HEIGHT + 60), new OrthographicCamera());
	stage = new Stage(viewport,batch);
	
	fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("sprites\\bm.ttf"));
	fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
	fontParameter.size = 72;
	fontParameter.borderWidth = 5.0f;
	fontParameter.borderColor = Color.BLACK;
	font = fontGenerator.generateFont(fontParameter);
	font.getData().setScale(0.30f);
	
	Table table = new Table();
	table.top();
	table.setFillParent(true);
	Label.LabelStyle fontStyle =new Label.LabelStyle(font, Color.RED);
	
	intermission = new Label("Round Over!!!", fontStyle);
	countdown = new Label("The Next Round will begin in " + timer, fontStyle);
	
	table.add(intermission).expandX().pad(1);
	table.row();
	table.add(countdown).expandX().pad(1);
	
	stage.addActor(table);
	
	}
	public void update(float dt) {
		timeCount += dt;
		if(timeCount >= 1) {
			timer--;
		countdown.setText("The Next Round will begin in " + timer);
		timeCount = 0;
		
		if(timer <= 0) {
			dispose();
			game.setScreen(new PlayScreen((BomberGame) game));
			}	
		}
	}
	public void dispose() {
		stage.dispose();
	}
}
