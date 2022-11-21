package com.tempura.bomberman.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Actors.Enemy;
import com.tempura.bomberman.Actors.Player;
import com.tempura.bomberman.Objects.Bomb;
import com.tempura.bomberman.Objects.HeavyBlocks;
import com.tempura.bomberman.Tools.WorldContactListener;

public class PlayScreen extends BomberScreen {
	
	private BomberGame game;
	private TextureAtlas atlas;
	
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	
	//Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private Player player;
  private Enemy enemy;
  private Array<Bomb> bombs;
	
	public PlayScreen (BomberGame game) {
		atlas = new TextureAtlas("sprites/bomber_party.atlas");
		this.game = game;
		
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(BomberGame.V_WIDTH / BomberGame.PPM,
			BomberGame.V_HEIGHT / BomberGame.PPM, gameCam);
		
		gameCam.setToOrtho(false, gamePort.getWorldWidth(), gamePort.getWorldHeight());
		
		world = new World(new Vector2(0, 0), true);
		b2dr = new Box2DDebugRenderer();
		
		maploader = new TmxMapLoader();
		map = maploader.load("tilemaps/level1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / BomberGame.PPM);
		
		player = new Player(world, map, this);
    enemy = new Enemy(world, this);
		bombs = new Array<>();
		
		new HeavyBlocks(world, map);
		
		world.setContactListener(new WorldContactListener());
	}
	
	public Player getPlayer() {
		return player;
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
	
	private void update() {
		player.handleInput();
		enemy.handleInput();
		
		world.step(1/60f, 6, 2);
		
		player.update();
    enemy.update();
    for (Bomb bomb : bombs) bomb.update();
		
		gameCam.update();
		renderer.setView(gameCam);
	}

	@Override
	public void render(float delta) {
		update();
		
		ScreenUtils.clear(Color.BLACK);
		
		renderer.render();
		
		b2dr.render(world, gameCam.combined);
		
		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();
		for (Bomb bomb : bombs) bomb.draw(game.batch);
		player.draw(game.batch);
		enemy.draw(game.batch);
		game.batch.end();
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
	}

}
