package com.tempura.bomberman.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Effects.Explosion;
import com.tempura.bomberman.Screens.PlayScreen;

public class Bomb extends Sprite {
	
	public enum Team { PLAYER, ENEMY };
	private Team team;

	public Body b2body;
	
	private World world;
	private PlayScreen screen;
	private Animation<TextureRegion> anim;
	private Fixture fixture;
	
	private float stateTimer;
	private int range;
	
	public Bomb(PlayScreen screen, float initX, float initY, short categoryBit, Team team, int range) {
		super(screen.getAtlas().findRegion("big_bomb"));
		
		this.team = team;
		this.world = screen.getWorld();
		this.screen = screen;
		this.stateTimer = 0;
		this.range = range;
		
		Array<TextureRegion> frames = new Array<>();
		for (int i = 0; i <= 5; i++) {
			frames.add(new TextureRegion(getTexture(), 164 + i * 16, 20, 16, 16));
		}
		anim = new Animation<>(0.5f, frames);
		frames.clear();
		
		defineBody(initX, initY, categoryBit);
		
		setBounds(0, 0, 16 / BomberGame.PPM, 16 / BomberGame.PPM);
		setRegion(new TextureRegion(getTexture(), 164, 20, 16, 16));
	}
	
	public void update() {
		setPosition(b2body.getPosition().x - getWidth() / 2,
				b2body.getPosition().y - getHeight() / 2);
		setRegion(anim.getKeyFrame(stateTimer));
		 
		this.stateTimer += Gdx.graphics.getDeltaTime();
		
		if (stateTimer >= 3) {
			screen.getBombs().removeValue(this, true);
			
			if (team == Team.PLAYER) screen.getPlayer().subtractBombCount();
			else screen.getEnemy().subtractBombCount();
			
			screen.explosions.add(new Explosion(screen, b2body.getPosition(), range));
			world.destroyBody(b2body);
		}
	}
	
	private void defineBody(float initX, float initY, short categoryBit) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.KinematicBody;
		bdef.position.set(initX, initY);
		
		b2body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(8 / BomberGame.PPM, 8 / BomberGame.PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.filter.categoryBits = categoryBit;
		fdef.shape = shape;
		
		fixture = b2body.createFixture(fdef);
		
		FixtureDef fdefSensor = new FixtureDef();
		fdefSensor.filter.categoryBits = BomberGame.OPAQUE_BOMB_BIT;
		fdefSensor.shape = shape;
		fdefSensor.isSensor = true;
		
		b2body.createFixture(fdefSensor).setUserData(this);
	}
	
	public short getCategoryBits() {
		return fixture.getFilterData().categoryBits;
	}
	
	public void setToOpaque() {
		Filter filter = new Filter();
		filter.categoryBits = BomberGame.OPAQUE_BOMB_BIT;
		fixture.setFilterData(filter);
	}
	
}
