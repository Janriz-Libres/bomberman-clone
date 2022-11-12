package com.tempura.bomberman.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Actors.Bomberman;

public class PlayScreen extends BomberScreen {
	
	private Game game;
	
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	
	//Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private Bomberman player;
	
	public PlayScreen (Game game) {
		this.game = game;
		
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(BomberGame.V_WIDTH / BomberGame.PPM,
			BomberGame.V_HEIGHT / BomberGame.PPM, gameCam);
		
		gameCam.setToOrtho(false, gamePort.getWorldWidth(), gamePort.getWorldHeight());
		
		world = new World(new Vector2(0, 0), true);
		b2dr = new Box2DDebugRenderer();
		
		player = new Bomberman(world);
		
		maploader = new TmxMapLoader();
		map = maploader.load("Tilemaps/level1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / BomberGame.PPM);
	}
	
	private void update() {
		player.handleInput();
		
		gameCam.update();
		renderer.setView(gameCam);
	}

	@Override
	public void render(float delta) {
		update();
		
		ScreenUtils.clear(Color.BLACK);
		
		renderer.render();
		
		b2dr.render(world, gameCam.combined);
		
		world.step(1/60f, 6, 2);
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
