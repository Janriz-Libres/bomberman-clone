package com.tempura.bomberman.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Screens.PlayScreen;
import com.tempura.bomberman.Tools.GameObject;

public class Explosion extends Sprite implements GameObject {

	private World world;
	private TiledMap map;
	
	private Array<Body> bodies;
	private Vector2 pos;
	
	private Array<Sprite> topArmSprites;
	private Array<Sprite> rightArmSprites;
	private Array<Sprite> botArmSprites;
	private Array<Sprite> leftArmSprites;
	
	private Animation<TextureRegion> center;
	private Animation<TextureRegion> arm;
	private Animation<TextureRegion> tail;
	
	private float stateTimer;
	private int range;
	
	public Explosion(PlayScreen screen, Vector2 pos) {
		super(new TextureAtlas("sprites/explosion.atlas").findRegion("explosion"));
		this.pos = pos;
		
		world = screen.getWorld();
		map = screen.getMap();
		bodies = new Array<>();
		stateTimer = 0;
		range = 1;
		
		defineAnimations();
		defineBodies();
		
		setRegion(new TextureRegion(getTexture(), 2, 2, 32, 32));
		setBounds(0, 0, 16 / BomberGame.PPM, 16 / BomberGame.PPM);
		setPosition(pos.x - getWidth() / 2, pos.y - getHeight() / 2);
	}
	
	private void defineAnimations() {
		Array<TextureRegion> frames = new Array<>();
		
		for (int i = 0; i < 7; i++) {
			frames.add(new TextureRegion(getTexture(), 2 + i * 32, 2, 32, 32));
		}
		center = new Animation<>(0.1f, frames);
		frames.clear();
		
		for (int i = 0; i < 7; i++) {
			frames.add(new TextureRegion(getTexture(), 2 + i * 32, 2 + 32, 32, 32));
		}
		arm = new Animation<>(0.1f, frames);
		frames.clear();
		
		for (int i = 0; i < 7; i++) {
			frames.add(new TextureRegion(getTexture(), 2 + i * 32, 2 + 32 * 2, 32, 32));
		}
		tail = new Animation<>(0.1f, frames);
	}
	
	public void update(float dt) {
		setRegion(center.getKeyFrame(stateTimer));
		setArmRegions(topArmSprites);
		setArmRegions(rightArmSprites);
		setArmRegions(botArmSprites);
		setArmRegions(leftArmSprites);
		stateTimer += dt;
	}
	
	private void setArmRegions(Array<Sprite> sprites) {
		for (int i = 0; i < sprites.size; i++) {
			sprites.get(i).setRegion(arm.getKeyFrame(stateTimer));
		}
	}

	@Override
	public void defineBodies() {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.StaticBody;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(8 / BomberGame.PPM, 8 / BomberGame.PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.filter.categoryBits = BomberGame.DEFAULT_BIT;
		fdef.shape = shape;
		fdef.isSensor = true;
		
		bdef.position.set(pos);
		bodies.add(world.createBody(bdef));
		bodies.peek().createFixture(fdef);
		
		for (int i = 0; i < 4; i++) {
			Vector2 coor = findCoordinate(i);
			
			Cell cell = getCell(coor);
			
			bdef.position.set(coor);
			
			for (int j = 1; j <= range; j++) {
				bodies.add(world.createBody(bdef));
				bodies.peek().createFixture(fdef);
			}
		}
	}
	
	private Vector2 findCoordinate(int index) {
		switch (index) {
		case 0:
			return new Vector2(pos.x, pos.y + 16 * index / BomberGame.PPM);
		case 1:
			return new Vector2(pos.x + 16 * index / BomberGame.PPM, pos.y);
		case 2:
			return new Vector2(pos.x, pos.y - 16 * index / BomberGame.PPM);
		case 3:
		default:
			return new Vector2(pos.x - 16 * index / BomberGame.PPM, pos.y);
		}
	}
	
	private Cell getCell(Vector2 coor) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
		return layer.getCell((int) (coor.x * BomberGame.PPM / 16),
				(int) (coor.y * BomberGame.PPM / 16));
	}
}
