package com.tempura.bomberman.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Screens.PlayScreen;
import com.tempura.bomberman.Tools.GameObject;
import com.tempura.bomberman.Actors.Character;

public class Powerup extends Sprite implements GameObject {
	
	public enum PowerType { RANGE, SPEED, CAPACITY };
	public PowerType type;
	
	private World world;
	private Body b2body;
	
	private float x;
	private float y;
	
	public Powerup(PlayScreen screen, float x, float y, PowerType type) {
		super(new TextureAtlas("sprites/powerups.atlas").findRegion("addRange"));
		this.world = screen.getWorld();
		this.x = x;
		this.y = y;
		this.type = type;
		
		setBounds(0, 0, 14 / BomberGame.PPM, 14 / BomberGame.PPM);
		setPosition(x - getWidth() / 2, y - getHeight() / 2);
		setRegion(pickRegion());
		
		screen.creatablePowerups.add(this);
	}
	
	private TextureRegion pickRegion() {
		switch (type) {
		case RANGE:
			return new TextureRegion(getTexture(), 20, 2, 16, 16);
		case CAPACITY:
			return new TextureRegion(getTexture(), 2, 2, 16, 16);
		case SPEED:
		default:
			return new TextureRegion(getTexture(), 38, 2, 16, 16);
		}
	}
	
	public void takeEffect(Character player) {
		switch (type) {
		case RANGE:
			player.addRange();
			break;
		case CAPACITY:
			player.addCapacity();
			break;
		case SPEED:
		default:
			player.boostSpeed();
		}
	}

	@Override
	public void defineBodies() {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.StaticBody;
		bdef.position.set(x, y);
		
		b2body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(7 / BomberGame.PPM, 7 / BomberGame.PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.filter.categoryBits = BomberGame.POWERUP_BIT;
		fdef.shape = shape;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef);
	}
}
