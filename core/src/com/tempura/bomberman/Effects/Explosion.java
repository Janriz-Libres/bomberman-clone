package com.tempura.bomberman.Effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Screens.PlayScreen;
import com.tempura.bomberman.Tools.GameObject;

public class Explosion implements GameObject {
	
	protected enum ExplosionType { CENTER, ARM, TAIL };

	private PlayScreen screen;
	private World world;
	private AtlasRegion region;
	private TiledMap map;
	
	private Array<ExplosionTile> tiles;
	private Vector2 origin;
	
	private int range;
	private float stateTimer;
	
	private static final int lightTileId = 10;
	private static final int heavyTileId1 = 71;
	private static final int heavyTileId2 = 86;
	
	public Explosion(PlayScreen screen, Vector2 origin, int range) {
		this.screen = screen;
		this.origin = origin;
		this.world = screen.getWorld();
		
		region = new TextureAtlas("sprites/explosion.atlas").findRegion("explosion");
		map = screen.getMap();
		tiles = new Array<>();
		
		this.range = range;
		stateTimer = 0;
		
		defineBodies();
	}
	
	public void update() {
		for (ExplosionTile tile : tiles) tile.update(stateTimer);
		stateTimer += Gdx.graphics.getDeltaTime();
		
		if (stateTimer >= 0.5) {
			screen.explosions.removeValue(this, true);
			for (ExplosionTile tile : tiles) world.destroyBody(tile.b2body);
		}
	}
	
	public void render(SpriteBatch batch) {
		for (ExplosionTile tile : tiles) tile.draw(batch);
	}

	@Override
	public void defineBodies() {
		tiles.add(new ExplosionTile(world, ExplosionType.CENTER, origin, region, 0));
		
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j <= range; j++) {
				Vector2 coor = findCoordinate(i, j);
				
				Cell cell = getCell(coor);
				int tileId = cell.getTile().getId();
				
				if (tileId == heavyTileId1 || tileId == heavyTileId2) break;
				if (tileId == lightTileId) {
					TiledMapTile tile = map.getTileSets().getTile(1);
					cell.setTile(tile);
					j = range;
				}
				
				if (j == range)
					tiles.add(new ExplosionTile(world, ExplosionType.TAIL, coor, region,
							findRotation(i)));
				else
					tiles.add(new ExplosionTile(world, ExplosionType.ARM, coor, region,
							findRotation(i)));
			}
		}
	}
	
	private float findRotation(int dirIndex) {
		if (dirIndex == 0) return 180;
		if (dirIndex == 1) return 90;
		if (dirIndex == 2) return 0;
		return -90;
	}
	
	private Vector2 findCoordinate(int dirIndex, int idxRange) {
		switch (dirIndex) {
		case 0:
			return new Vector2(origin.x, origin.y + 16 * idxRange / BomberGame.PPM);
		case 1:
			return new Vector2(origin.x + 16 * idxRange / BomberGame.PPM, origin.y);
		case 2:
			return new Vector2(origin.x, origin.y - 16 * idxRange / BomberGame.PPM);
		case 3:
		default:
			return new Vector2(origin.x - 16 * idxRange / BomberGame.PPM, origin.y);
		}
	}
	
	private Cell getCell(Vector2 coor) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
		return layer.getCell((int) (coor.x * BomberGame.PPM / 16),
				(int) (coor.y * BomberGame.PPM / 16));
	}
}
