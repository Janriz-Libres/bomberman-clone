package com.tempura.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.tempura.bomberman.Scenes.Hud;
import com.tempura.bomberman.Screens.PlayScreen;
import com.tempura.bomberman.Screens.menuTemp;

public class BomberGame extends Game {
	public static final int V_WIDTH = 240;
	public static final int V_HEIGHT = 224;
	public static final float PPM = 100;
	
	private FreeTypeFontGenerator fontGenerator;
	private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	public BitmapFont font;
	
	public static final short DEFAULT_BIT = 1, PLAYER_BIT = 2, HEAVY_BLOCK_BIT  = 4,
			LIGHT_BLOCK_BIT = 8, DESTROYED_BLOCK_BIT = 16, PLAYER_BOMB_BIT = 32,
			ENEMY_BOMB_BIT = 64, OPAQUE_BOMB_BIT = 128, EXPLOSION_BIT = 256, POWERUP_BIT = 512;
	
	public SpriteBatch batch;
	public Hud hud;
	public Music music;
	public Music victoryMusic;
	
	public PlayScreen playScreen;
	public menuTemp menu;
	private TextureAtlas atlas;
	
	
	
	@Override
	public void create () {
		
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("sprites\\bm.ttf"));
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 72;
		fontParameter.borderWidth = 5.0f;
		fontParameter.borderColor = Color.BLACK;
		font = fontGenerator.generateFont(fontParameter);
		font.getData().setScale(0.12f);
		
		batch = new SpriteBatch();
		
		atlas = new TextureAtlas("sprites/bomber_party.atlas");

		menu = new menuTemp(this);
		this.setScreen(menu);
		
		Sprite player = new Sprite(atlas.findRegion("human"));
		hud = new Hud(batch, 0, 0, new TextureRegion(player.getTexture(), 2 + 16, 2, 16, 16),
				new TextureRegion(player.getTexture(), 164 + 16, 38, 16, 16), this);
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music/BusyDay.wav"));
		music.setLooping(true);
		music.setVolume(0.4f);
		
		victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("music/VictoryMusic.wav"));
		victoryMusic.setLooping(true);
		victoryMusic.setVolume(0.4f);
		
		playScreen = new PlayScreen(this);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		menu.dispose();
		playScreen.dispose();
		hud.dispose();
	}
}
