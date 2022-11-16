package com.tempura.bomberman.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tempura.bomberman.BomberGame;

public class Player extends Sprite {
	
	public enum State { IDLE, MOVING_RIGHT, MOVING_UP, MOVING_DOWN };
	public State previousState;
	public State currentState;
	
	private World world;
	private Vector2 velocity;
	
	public Body b2body;
	
	private Animation<TextureRegion> playerMoveRight;
	private Animation<TextureRegion> playerMoveUp;
	private Animation<TextureRegion> playerMoveDown;
	
	private float stateTimer;
	
	private boolean isMovingRight;
	
	public Player (World world) {
		this.world = world;
		velocity = new Vector2();
		
		currentState = State.IDLE;
		previousState = State.IDLE;
		stateTimer = 0;
		isMovingRight = true;
		
		Array<TextureRegion> frames = new Array<>();
		
		
		defineBody();
	}
	
	public void handleInput() {
		velocity.set(0, 0);
	
		if (Gdx.input.isKeyPressed(Keys.A)) velocity.x -= 1; // (-1, 0)
		if (Gdx.input.isKeyPressed(Keys.D)) velocity.x += 1; // (1, 0)
		if (Gdx.input.isKeyPressed(Keys.W)) velocity.y += 1; // (0, 1)
		if (Gdx.input.isKeyPressed(Keys.S)) velocity.y -= 1; // (0, -1)
		
		velocity = velocity.nor();
		b2body.setLinearVelocity(velocity.setLength(0.5f));
	}
	
	private void defineBody() {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(24 / BomberGame.PPM, 24 / BomberGame.PPM);
		
		b2body = world.createBody(bdef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(7 / BomberGame.PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		
		b2body.createFixture(fdef);
	}
}
