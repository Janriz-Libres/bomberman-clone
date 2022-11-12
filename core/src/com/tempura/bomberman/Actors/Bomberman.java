package com.tempura.bomberman.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.tempura.bomberman.BomberGame;

public class Bomberman {
	
	private World world;
	public Body b2body;
	
	public Bomberman (World world) {
		this.world = world;
		defineBody();
	}
	
	public void handleInput() {
		if (Gdx.input.isKeyPressed(Keys.A)) {
			b2body.setLinearVelocity(-1, 0);
		} else if (Gdx.input.isKeyPressed(Keys.W)) {
			b2body.setLinearVelocity(0, 1);
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			b2body.setLinearVelocity(0, -1);
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			b2body.setLinearVelocity(1, 0);
		} else {
			b2body.setLinearVelocity(0, 0);
		}
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
