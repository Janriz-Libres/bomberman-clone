package com.tempura.bomberman.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.tempura.bomberman.BomberGame;

public class Player {
	
	private World world;
	private Vector2 velocity;
	
	public Body b2body;
	
	public Player (World world) {
		this.world = world;
		velocity = new Vector2();
		defineBody();
	}
	
	public void handleInput() {
		velocity.set(0, 0);
	
		if (Gdx.input.isKeyPressed(Keys.A)) {
			velocity.x -= 1; // (-1, 0)
		}
		
		if (Gdx.input.isKeyPressed(Keys.D)) {
			velocity.x += 1; // (1, 0)
		}
		
		if (Gdx.input.isKeyPressed(Keys.W)) {
			velocity.y += 1; // (0, 1)
		}
		
		if (Gdx.input.isKeyPressed(Keys.S)) {
			velocity.y -= 1; // (0, -1)
		}
		
		velocity = velocity.nor();
		b2body.setLinearVelocity(velocity.setLength(0.5f));
	}
	
	private void defineBody() {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.KinematicBody;
		bdef.position.set(24 / BomberGame.PPM, 24 / BomberGame.PPM);
		
		b2body = world.createBody(bdef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(8 / BomberGame.PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		
		b2body.createFixture(fdef);
	}
}
