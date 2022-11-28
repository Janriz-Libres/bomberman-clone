package com.tempura.bomberman.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Actors.Enemy;
import com.tempura.bomberman.Actors.Player;
import com.tempura.bomberman.Effects.Explosion;
import com.tempura.bomberman.Objects.Bomb;
import com.tempura.bomberman.Objects.HeavyBlock;
import com.tempura.bomberman.Objects.LightBlock;
import com.tempura.bomberman.Objects.Powerup;
import com.tempura.bomberman.Scenes.Hud;
import com.tempura.bomberman.Tools.WorldContactListener;

public class PlayScreen extends BomberScreen {
	
	public BomberGame game;
	private TextureAtlas atlas;
	
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	private Hud hud;
	
	//Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private Player player;
	private Enemy enemy;
	
	private Array<Bomb> bombs;
	public Array<Explosion> explosions;
	
	public Array<Powerup> creatablePowerups;
	public Array<Powerup> powerups;
	private Array<Body> destroyableBodies;
	
	private Music music;
	
	public PlayScreen (BomberGame game) {
		atlas = new TextureAtlas("sprites/bomber_party.atlas");
		this.game = game;
		
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(BomberGame.V_WIDTH / BomberGame.PPM,
			BomberGame.V_HEIGHT / BomberGame.PPM, gameCam);
		hud = game.hud;
	
		gameCam.setToOrtho(false, gamePort.getWorldWidth(), gamePort.getWorldHeight());
		
		world = new World(new Vector2(0, 0), true);
		b2dr = new Box2DDebugRenderer();
		
		maploader = new TmxMapLoader();
		map = maploader.load("tilemaps/level1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / BomberGame.PPM);
		
		player = new Player(world, map, this);
		enemy = new Enemy(world, map, this);
		
		bombs = new Array<>();
		explosions = new Array<>();
		
		creatablePowerups = new Array<>();
		powerups = new Array<>();
		destroyableBodies = new Array<>();
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music/BusyDay.wav"));
		music.setLooping(true);
		music.setVolume(0.6f);
		music.play();
		
		new HeavyBlock(this);
		new LightBlock(this);
		
		world.setContactListener(new WorldContactListener(this));
	}
	
	public void destroyPlayer(Body body) {
		if (!destroyableBodies.contains(body, true)) destroyableBodies.add(body);
		
		boolean playerDead;
		boolean enemyDead;
		
		if (player != null) playerDead = player.isDead ? true : false;
		else playerDead = true;
		
		if (enemy != null) enemyDead = enemy.isDead ? true : false;
		else enemyDead = true;
		
		if (playerDead && enemyDead) {
			player.dispose();
			enemy.dispose();
			player = null;
			enemy = null;
			return;
		}
		
		if (playerDead) {
			player.dispose();
			player = null;
			hud.setScore2(1);
		}
		
		if (enemyDead) {
			enemy.dispose();
			enemy = null;
			hud.setScore1(1);
		}
	}
	
	public void addPowerup(Powerup powerup) {
		powerups.add(powerup);
	}
	
	public Array<Body> getDestroyables() {
		return destroyableBodies;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Enemy getEnemy() {
		return enemy;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Array<Bomb> getBombs() {
		return bombs;
	}
	
	public TextureAtlas getAtlas() {
		return atlas;
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	private void update(float dt) {
		if (player == null || enemy == null || hud.getTimer() <= 0) {
			hud.resetTimer();
			music.stop();
			
			PlayScreen anotherScreen = new PlayScreen(game);
			game.setScreen(anotherScreen);
		}
		
		world.step(1/60f, 6, 2);
		
		for (Powerup powerup : creatablePowerups) {
			powerup.defineBodies();
			creatablePowerups.removeValue(powerup, true);
		}
		
		for (Body body : destroyableBodies) {
			world.destroyBody(body);
			destroyableBodies.removeValue(body, true);
		}
		
		if (player != null) {
			player.handleInput();
			player.update();
		}
		
		if (enemy!= null) {
			enemy.handleInput();
			enemy.update();
		}
		
		for (Bomb bomb : bombs) bomb.update();
		for (Explosion explosion : explosions) explosion.update();
		
		if (hud.getTimer() <= 60 && player != null & enemy != null) {
			player.maximizeStats();
			enemy.maximizeStats();
		}
		
		hud.setTimer();
		
		gameCam.update();
		renderer.setView(gameCam);
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		ScreenUtils.clear(Color.GRAY);
		
		renderer.render();
		
		b2dr.render(world, gameCam.combined);
		
		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();
		
		for (Bomb bomb : bombs) bomb.draw(game.batch);
		for (Powerup powerup: powerups) powerup.draw(game.batch);
		if (player != null) player.draw(game.batch);
		if (enemy != null) enemy.draw(game.batch);
		for (Explosion explosion : explosions) explosion.render(game.batch);
		
		game.batch.end();
		
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		music.dispose();
		if (player != null) player.dispose();
		if (enemy != null) enemy.dispose();
	}
}
