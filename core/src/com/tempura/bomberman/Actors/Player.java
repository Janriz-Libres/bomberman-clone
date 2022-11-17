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
import com.tempura.bomberman.Screens.PlayScreen;

public class Player extends Sprite {
	
	public enum State { IDLE, RIGHT, UP, DOWN };
	public State previousState;
	public State currentState;
	
	private World world;
	private Vector2 velocity;
	
	public Body b2body;
	
	private TextureRegion playerIdle;
	private Animation<TextureRegion> playerRight;
	private Animation<TextureRegion> playerUp;
	private Animation<TextureRegion> playerDown;
	
	private float stateTimer;
	private boolean isMovingRight;
	
	public Player (World world, PlayScreen screen) {
		super(screen.getAtlas().findRegion("human"));
		this.world = world;
		velocity = new Vector2();
		
		currentState = State.IDLE;
		previousState = State.IDLE;
		stateTimer = 0;
		isMovingRight = true;
		
		Array<TextureRegion> frames = new Array<>();
		
		// Moving Right
		for (int i = 4; i < 8; i++) {
			frames.add(new TextureRegion(getTexture(), 2 + i * 16, 2, 16, 16));
		}
		playerRight = new Animation<>(0.1f, frames);
		frames.clear();
		
		// Moving Up
		frames.add(new TextureRegion(getTexture(), 2, 2, 16, 16));
		frames.add(new TextureRegion(getTexture(), 2 + 8 * 16, 2, 16, 16));
		frames.add(new TextureRegion(getTexture(), 2 + 9 * 16, 2, 16, 16));
		playerUp = new Animation<>(0.1f, frames);
		frames.clear();
		
		// Moving Down
		for (int i = 1; i < 4; i++) {
			frames.add(new TextureRegion(getTexture(), 2 + i * 16, 2, 16, 16));
		}
		playerDown = new Animation<>(0.1f, frames);
		frames.clear();
		
		defineBody();
		
		playerIdle = new TextureRegion(getTexture(), 2, 2, 16, 16);
		setBounds(0, 0, 18 / BomberGame.PPM, 18 / BomberGame.PPM);
		setRegion(playerIdle);
	}
	
	public void handleInput() {
		velocity.set(0, 0);
	
		if (Gdx.input.isKeyPressed(Keys.A)) {
			velocity.x -= 1; // (-1, 0)
			currentState = State.RIGHT;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			velocity.x += 1; // (1, 0)
			currentState = State.RIGHT;
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			velocity.y += 1; // (0, 1)
			currentState = State.UP;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			velocity.y -= 1; // (0, -1)
			currentState = State.DOWN;
		}
		
		if (velocity.isZero()) currentState = State.IDLE;
		
		velocity = velocity.nor();
		b2body.setLinearVelocity(velocity.setLength(0.5f));
	}
	
	public void update() {
		setPosition(b2body.getPosition().x - getWidth() / 2,
				b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame());
	}
	
	public TextureRegion getFrame() {
		TextureRegion region;
		
		switch (currentState) {
		case UP:
			region = playerUp.getKeyFrame(stateTimer, true);
			break;
		case RIGHT:
			region = playerRight.getKeyFrame(stateTimer, true);
			break;
		case DOWN:
			region = playerDown.getKeyFrame(stateTimer, true);
			break;
		default:
			region = playerIdle;
		}
		
		if ((b2body.getLinearVelocity().x < 0 || !isMovingRight) && !region.isFlipX()) {
			region.flip(true, false);
			isMovingRight = false;
		} else if ((b2body.getLinearVelocity().x > 0 || isMovingRight) && region.isFlipX()) {
			region.flip(true, false);
			isMovingRight = true;
		}
		
		stateTimer = (currentState == previousState) ?
				stateTimer + Gdx.graphics.getDeltaTime() : 0;
		previousState = currentState;
		return region;
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
