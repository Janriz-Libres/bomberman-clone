package com.tempura.bomberman.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Screens.PlayScreen;

public class Enemy extends Sprite {
	
	public enum State{ IDLE, RIGHT, UP, DOWN };
	public State previousState2;
	public State currentState2;
	
	private World world;
	private Vector2 velocity;
	
	public Body b2body2;
	
	//Idle frames
	private TextureRegion enemyIdleUp;
	private TextureRegion enemyIdleRight;
	private TextureRegion enemyIdleDown;
	
	//moving frames
	private Animation<TextureRegion> enemyRight;
	private Animation<TextureRegion> enemyUp;
	private Animation<TextureRegion> enemyDown;
	
	private float stateTimer2;
	private boolean isMovingRight2;
	
	public Enemy (World world, PlayScreen screen) {
		super(screen.getAtlas().findRegion("human"));
		this.world = world;
		velocity = new Vector2(0,0);
		
		currentState2 = State.IDLE;
		previousState2 = State.IDLE;
		stateTimer2 = 0;
		isMovingRight2 = true;
		
		enemyIdleUp = new TextureRegion(getTexture(), 2, 2, 16, 16);
		enemyIdleRight = new TextureRegion(getTexture(), 2 + 4 * 16, 2, 16, 16);
		enemyIdleDown = new TextureRegion(getTexture(), 2 + 16, 2, 16, 16);
		
		Array<TextureRegion> frames = new Array<>();
		
		//right
		for(int i = 5; i <8; i++) {
			frames.add(new TextureRegion(getTexture(), 2 + i * 16, 2, 16, 16));
		}
		frames.add(new TextureRegion(getTexture(), 2 + 6 * 16, 2, 16, 16));
		enemyRight = new Animation<>(0.1f, frames);
		frames.clear();
		
		//up
		frames.add(new TextureRegion(getTexture(), 2 + 8 * 16, 2, 16, 16));
		frames.add(enemyIdleUp);
		frames.add(new TextureRegion(getTexture(), 2 + 9 * 16, 2, 16, 16));
		frames.add(enemyIdleUp);
		enemyUp = new Animation<>(0.1f, frames);
		frames.clear();
		
		//down
		frames.add(new TextureRegion(getTexture(), 2 + 2 * 16, 2, 16, 16));
		frames.add(new TextureRegion(getTexture(), 2 + 1 * 16, 2, 16, 16));
		frames.add(new TextureRegion(getTexture(), 2 + 3 * 16, 2, 16, 16));
		frames.add(new TextureRegion(getTexture(), 2 + 1 * 16, 2, 16, 16));
		enemyDown = new Animation<>(0.1f, frames);
		frames.clear();
		
		defineBody();
		
		setBounds (0, 0, 18 / BomberGame.PPM, 18 / BomberGame.PPM);
		setRegion(enemyIdleUp);
	}
	
	private boolean hasInput() {
		if (Gdx.input.isKeyPressed(Keys.LEFT)) return true;
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) return true;
		if (Gdx.input.isKeyPressed(Keys.UP)) return true;
		if (Gdx.input.isKeyPressed(Keys.DOWN)) return true;
		return false;
	}
	
	public void handleInput() {
		if(!hasInput()) {
			currentState2 = State.IDLE;
			b2body2.setLinearVelocity(new Vector2(0,0));
			return;
		}
		
		velocity = new Vector2(0, 0);
		
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			velocity.x -= 1; // (-1,0)
			currentState2 = State.RIGHT;
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			velocity.x += 1; // (1,0)
			currentState2 = State.RIGHT;
		}
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			velocity.y += 1; // (0,1)
			currentState2 = State.UP;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			velocity.y -= 1; // (0,-1)
			currentState2 = State.DOWN;
		}
		
		velocity = velocity.nor();
		b2body2.setLinearVelocity(velocity.setLength(0.5f));
	}
	
	public void update(){
		setPosition(b2body2.getPosition().x - getWidth() / 2,
				b2body2.getPosition().y - getHeight() / 2);
		setRegion(getFrame());
	}
	
	private TextureRegion getIdleDirection() {
		if (velocity.y > 0) return enemyIdleUp;
		if (velocity.y < 0) return enemyIdleDown;
		
		if (velocity.x < 0) {
			enemyIdleRight.flip(!enemyIdleRight.isFlipX(), false);
			return enemyIdleRight;
		}
		if (velocity.x > 0) {
			enemyIdleRight.flip(enemyIdleRight.isFlipX(), false);
			return enemyIdleRight;
		}
		
		return enemyIdleDown;
	}
	
	public TextureRegion getFrame() {
		TextureRegion region;
		
		switch (currentState2) {
		case UP:
			region = enemyUp.getKeyFrame(stateTimer2, true);
			break;
		case RIGHT:
			region = enemyRight.getKeyFrame(stateTimer2, true);
			break;
		case DOWN:
			region = enemyDown.getKeyFrame(stateTimer2, true);
			break;
		default:
			region = getIdleDirection();
		}
			
		if((b2body2.getLinearVelocity().x < 0 || !isMovingRight2) && !region.isFlipX()) {
			region.flip(true, false);
			isMovingRight2 = false;
		} else if ((b2body2.getLinearVelocity().x > 0 || isMovingRight2) && region.isFlipX()) {
			region.flip(true, false);
			isMovingRight2 = true;
		}
		
		stateTimer2 = (currentState2 == previousState2) ?
				stateTimer2 + Gdx.graphics.getDeltaTime() : 0;
		previousState2 = currentState2;
		return region;
	}
	
	private void defineBody() {
		BodyDef ebdef = new BodyDef();
		ebdef.type = BodyType.DynamicBody;
		ebdef.position.set(215 / BomberGame.PPM, 200 / BomberGame.PPM);
		
		b2body2 = world.createBody(ebdef);
		
		CircleShape shape2 = new CircleShape();
		shape2.setRadius(7 / BomberGame.PPM);
		
		FixtureDef efdef = new FixtureDef();
		efdef.shape = shape2;
		
		b2body2.createFixture(efdef);
	}
}
