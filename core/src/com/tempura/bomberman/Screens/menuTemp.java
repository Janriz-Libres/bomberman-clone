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

public class menuTemp extends BomberScreen {
	private static final float WIDTH = BomberGame.V_WIDTH / BomberGame.PPM;
	private static final float HEIGHT = BomberGame.V_HEIGHT / BomberGame.PPM;
	private static final float BUTTON_WIDTH = 35 / BomberGame.PPM;
	private static final float BUTTON_HEIGHT = 15 / BomberGame.PPM;
	private static final float PLAY_BUTTON_Y = BomberGame.V_HEIGHT / 2 + 10;
	private static final float EXIT_BUTTON_Y = BomberGame.V_HEIGHT / 2 - 50;

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
	public Music menuMusic;

	public menuTemp(BomberGame game) {
		this.game = game;

		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(BomberGame.V_WIDTH / BomberGame.PPM, BomberGame.V_HEIGHT / BomberGame.PPM, gameCam);

		gameCam.setToOrtho(false, gamePort.getWorldWidth(), gamePort.getWorldHeight());
		playButtonActive = new Texture("sprites/play_button_active.png");
		playButtonInactive = new Texture("sprites/play_button_inactive.png");
		exitButtonActive = new Texture("sprites/exit_button_active.png");
		exitButtonInactive = new Texture("sprites/exit_button_inactive.png");
		Menu = new Texture("sprites/menu.png");

		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/MainTheme.wav"));
		menuMusic.setLooping(true);
		menuMusic.setVolume(0.4f);

		beep = Gdx.audio.newSound(Gdx.files.internal("sfx/beep.wav"));
		beep2 = Gdx.audio.newSound(Gdx.files.internal("sfx/beep.wav"));
	}

	@Override
	public void render(float delta) {
		gameCam.update();

		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();

		game.batch.draw(Menu, 0, 0, BomberGame.V_WIDTH / BomberGame.PPM, BomberGame.V_HEIGHT / BomberGame.PPM);

		float x = WIDTH / 2 - BUTTON_WIDTH / 2;
		float locX = Gdx.input.getX() / 3 / BomberGame.PPM;
		float locY = HEIGHT - Gdx.input.getY() / 3 / BomberGame.PPM;

		if (withinBoundsPlay(x, locX, locY)) {
			if (!isPlaying) {
				beep.play();
				isPlaying = true;
			}

			game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y / BomberGame.PPM, BUTTON_WIDTH, BUTTON_HEIGHT);

			if (Gdx.input.isTouched()) {
				menuMusic.stop();
				game.music.play();
				game.setScreen(game.playScreen);
			}
			
		} else {
			isPlaying = false;
			game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y / BomberGame.PPM, BUTTON_WIDTH, BUTTON_HEIGHT);
		}

		if (withinBoundsExit(x, locX, locY)) {
			if (!isPlaying2) {
				beep2.play();
				isPlaying2 = true;
			}
			
			game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y / BomberGame.PPM, BUTTON_WIDTH, BUTTON_HEIGHT);
			
			if (Gdx.input.isTouched()) {
				Gdx.app.exit();
			}
			
		} else {
			isPlaying2 = false;
			game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y / BomberGame.PPM, BUTTON_WIDTH, BUTTON_HEIGHT);
		}

		game.batch.end();
	}

	private boolean withinBoundsPlay(float x, float locX, float locY) {
		if (locX > x + BUTTON_WIDTH)
			return false;
		if (locX < x)
			return false;
		if (locY > PLAY_BUTTON_Y / BomberGame.PPM + BUTTON_HEIGHT)
			return false;
		if (locY < PLAY_BUTTON_Y / BomberGame.PPM)
			return false;
		return true;
	}

	private boolean withinBoundsExit(float x, float locX, float locY) {
		if (locX > x + BUTTON_WIDTH)
			return false;
		if (locX < x)
			return false;
		if (locY > EXIT_BUTTON_Y / BomberGame.PPM + BUTTON_HEIGHT)
			return false;
		if (locY < EXIT_BUTTON_Y / BomberGame.PPM)
			return false;
		return true;

//		 && Gdx.input.getX() > x
//		&& HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + BUTTON_HEIGHT
//		&& HEIGHT - Gdx.input.getY() > EXIT_BUTTON_Y
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void dispose() {
		playButtonActive.dispose();
		playButtonInactive.dispose();
		exitButtonActive.dispose();
		exitButtonInactive.dispose();
		Menu.dispose();
		beep.dispose();
		beep2.dispose();
		menuMusic.dispose();
	}

}
