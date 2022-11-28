package com.tempura.bomberman.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Screens.PlayScreen;
import com.tempura.bomberman.Tools.GameObject;

public abstract class Character extends Sprite implements GameObject, Disposable {
	
	public enum State { IDLE, RIGHT, UP, DOWN };
	public State previousState;
	public State currentState;
	
	protected World world;
	protected PlayScreen screen;
	
	public Body b2body;
	protected FixtureDef fdef;
	protected Vector2 velocity;
	
	// Idle frames
	protected TextureRegion playerIdleUp;
	protected TextureRegion playerIdleRight;
	protected TextureRegion playerIdleDown;
	
	// Moving frames
	protected Animation<TextureRegion> playerRight;
	protected Animation<TextureRegion> playerUp;
	protected Animation<TextureRegion> playerDown;
	
	protected Sound dropSFX;
	public Sound powerupSFX;

	protected float stateTimer;
	private float deadTimer;
	protected boolean isMovingRight;
	public boolean isDead;
	
	protected int maxBombs;
	protected int currentBombs;
	
	private float speed;
	protected int range;
	
	public Character(World world, TiledMap map, PlayScreen screen) {
		super(screen.getAtlas().findRegion("human"));
		
		this.world = world;
		this.screen = screen;
		this.maxBombs = 1;
		this.currentBombs = 0;
		
		velocity = new Vector2(0, 0);
		speed = 0.5f;
		range = 1;
		
		dropSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/drop.wav"));
		powerupSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/powerup.wav"));
		
		currentState = State.IDLE;
		previousState = State.IDLE;
		deadTimer = 0;
		stateTimer = 0;
		isMovingRight = true;
		isDead = false;
		
		defineAnimations();
		defineBodies();
		
		setBounds(0, 0, 16 / BomberGame.PPM, 16 / BomberGame.PPM);
		setRegion(playerIdleDown);
	}
	
	public void addRange() {
		this.range++;
	}
	
	public void addCapacity() {
		this.maxBombs++;
	}
	
	public void boostSpeed() {
		this.speed = 1;
	}
	
	public void setToDie() {
		isDead = true;
	}
	
	public void maximizeStats() {
		range = 20;
		speed = 1;
		maxBombs = 100;
	}
	
	protected abstract void defineAnimations();
	protected abstract boolean hasMovementInput();
	protected abstract void dropBomb();
	protected abstract void handleMovement();
	public abstract void defineBodies();
	
	public void subtractBombCount() {
		currentBombs -= 1;
	}
	
	public void handleInput() {
		// Drop bomb
		dropBomb();
		
		if (!hasMovementInput() || isDead) {
			currentState = State.IDLE;
			b2body.setLinearVelocity(new Vector2(0, 0));
			return;
		}
		
		velocity = new Vector2(0, 0);
		
		handleMovement();

		velocity = velocity.nor();
		b2body.setLinearVelocity(velocity.setLength(speed));
	}
	
	public void update() {
		setPosition(b2body.getPosition().x - getWidth() / 2,
				b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame());
		
		if (isDead) deadTimer += Gdx.graphics.getDeltaTime();
		if (deadTimer >= 2) screen.destroyPlayer(b2body);
	}
	
	private TextureRegion getIdleDirection() {
		if (velocity.y > 0) return playerIdleUp;
		if (velocity.y < 0) return playerIdleDown;

		if (velocity.x < 0) {
			playerIdleRight.flip(!playerIdleRight.isFlipX(), false);
			return playerIdleRight;
		}
		if (velocity.x > 0) {
			playerIdleRight.flip(playerIdleRight.isFlipX(), false);
			return playerIdleRight;
		}
		
		return playerIdleDown;
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
			region = getIdleDirection();
		}
		
		if ((velocity.x < 0 || !isMovingRight) && !region.isFlipX()) {
			region.flip(true, false);
			isMovingRight = false;
		} else if ((velocity.x > 0 || isMovingRight) && region.isFlipX()) {
			region.flip(true, false);
			isMovingRight = true;
		}
		
		stateTimer = (currentState == previousState) ?
				stateTimer + Gdx.graphics.getDeltaTime() : 0;
		previousState = currentState;
		return region;
	}
	
	@Override
	public void dispose() {
		dropSFX.dispose();
		powerupSFX.dispose();
	}
}
