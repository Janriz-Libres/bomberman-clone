package com.tempura.bomberman.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
	
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	
	boolean isPlaying = false;
	boolean isPlaying2 = false;
	BomberGame game;
	
	Texture playButtonActive;
	Texture playButtonInactive;
	Texture exitButtonActive;
	Texture exitButtonInactive;
	Texture Menu;
	Sound beep, beep2;
	
	
	Music menuMusic;
	public menuTemp(BomberGame game) {
		this.game = game;
		
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(WIDTH,
			HEIGHT, gameCam);
		
		gameCam.setToOrtho(false, gamePort.getWorldWidth(), gamePort.getWorldHeight());
		playButtonActive = new Texture("sprites/play_button_active.png");
		playButtonInactive = new Texture("sprites/play_button_inactive.png");
		exitButtonActive = new Texture("sprites/exit_button_active.png");
		exitButtonInactive = new Texture("sprites/exit_button_inactive.png");
		Menu = new Texture("sprites/menu.png");
		
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/MainTheme.wav"));
		menuMusic.setLooping(true);
		menuMusic.setVolume(0.4f);
		menuMusic.play();
		
		beep = Gdx.audio.newSound(Gdx.files.internal("sfx/beep.wav"));
		beep2 = Gdx.audio.newSound(Gdx.files.internal("sfx/beep.wav"));
	}
	@Override
	public void show() {
		
		
	}

	@Override
	public void render(float delta) {
		gameCam.update();
		
		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();
		
		game.batch.draw(Menu, 0, 0, WIDTH, HEIGHT);
		
		int x = (WIDTH / 2) - (EXIT_BUTTON_WIDTH / 2);
		if(Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && HEIGHT - Gdx.input.getY() > EXIT_BUTTON_Y ) {
			
			if(!isPlaying) {
		beep.play();
			isPlaying = true;
		}
			
			game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT );
			if(Gdx.input.isTouched()) {
				
				Gdx.app.exit();
			}
		}else {
			beep.stop();
			isPlaying = false;
			game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT );
		}
		
		x = (WIDTH / 2) - (PLAY_BUTTON_WIDTH / 2);
		if(Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x && HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && HEIGHT - Gdx.input.getY() > PLAY_BUTTON_Y ){
			
			if(!isPlaying2) {
				beep2.play();
				isPlaying2 = true;
			}
			
			game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH,PLAY_BUTTON_HEIGHT );
			if(Gdx.input.isTouched()) {
				this.dispose(); 
				menuMusic.stop();
				game.music.play();
				game.setScreen(game.playScreen);
			}
		}else {
			beep2.stop();
			isPlaying2 = false;
			game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH,PLAY_BUTTON_HEIGHT );
		}
		
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
		
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
