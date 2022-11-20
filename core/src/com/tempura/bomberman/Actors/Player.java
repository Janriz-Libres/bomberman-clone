package com.tempura.bomberman.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Objects.Bomb;
import com.tempura.bomberman.Screens.PlayScreen;

public class Player extends Sprite {
	
	public enum State { IDLE, RIGHT, UP, DOWN };
	public State previousState;
	public State currentState;
	
	private World world;
	private PlayScreen screen;
	
	public Body b2body;
	private FixtureDef fdef;
	private Vector2 velocity;
	
	// Idle frames
	private TextureRegion playerIdleUp;
	private TextureRegion playerIdleRight;
	private TextureRegion playerIdleDown;
	
	// Moving frames
	private Animation<TextureRegion> playerRight;
	private Animation<TextureRegion> playerUp;
	private Animation<TextureRegion> playerDown;

	private float stateTimer;
	private boolean isMovingRight;
	
	private int maxBombs;
	private int currentBombs;
	
	public Player (World world, TiledMap map, PlayScreen screen) {
		super(screen.getAtlas().findRegion("human"));
		
		this.world = world;
		this.screen = screen;
		this.maxBombs = 1;
		this.currentBombs = 0;
		
		velocity = new Vector2(0, 0);
		
		currentState = State.IDLE;
		previousState = State.IDLE;
		stateTimer = 0;
		isMovingRight = true;
		
		playerIdleUp = new TextureRegion(getTexture(), 2, 2, 16, 16);
		playerIdleRight = new TextureRegion(getTexture(), 2 + 4 * 16, 2, 16, 16);
		playerIdleDown = new TextureRegion(getTexture(), 2 + 16, 2, 16, 16);
		
		defineAnimations();
		defineBody();
		
		setBounds(0, 0, 16 / BomberGame.PPM, 16 / BomberGame.PPM);
		setRegion(playerIdleDown);
	}
	
	private void defineAnimations() {
		Array<TextureRegion> frames = new Array<>();
		
		// Moving Right
		for (int i = 5; i < 8; i++) {
			frames.add(new TextureRegion(getTexture(), 2 + i * 16, 2, 16, 16));
		}
		frames.add(new TextureRegion(getTexture(), 2 + 6 * 16, 2, 16, 16));
		playerRight = new Animation<>(0.1f, frames);
		frames.clear();
		
		// Moving Up
		frames.add(new TextureRegion(getTexture(), 2 + 8 * 16, 2, 16, 16));
		frames.add(playerIdleUp);
		frames.add(new TextureRegion(getTexture(), 2 + 9 * 16, 2, 16, 16));
		frames.add(playerIdleUp);
		playerUp = new Animation<>(0.1f, frames);
		frames.clear();
		
		// Moving Down
		frames.add(new TextureRegion(getTexture(), 2 + 2 * 16, 2, 16, 16));
		frames.add(new TextureRegion(getTexture(), 2 + 1 * 16, 2, 16, 16));
		frames.add(new TextureRegion(getTexture(), 2 + 3 * 16, 2, 16, 16));
		frames.add(new TextureRegion(getTexture(), 2 + 1 * 16, 2, 16, 16));
		playerDown = new Animation<>(0.1f, frames);
		frames.clear();
	}
	
	public void subtractBombCount() {
		currentBombs -= 1;
	}
	
	private boolean hasMovementInput() {
		if (Gdx.input.isKeyPressed(Keys.A)) return true;
		if (Gdx.input.isKeyPressed(Keys.D)) return true;
		if (Gdx.input.isKeyPressed(Keys.W)) return true;
		if (Gdx.input.isKeyPressed(Keys.S)) return true;
		return false;
	}
	
	public void handleInput() {
		// Drop bomb
		if (Gdx.input.isKeyJustPressed(Keys.F) && currentBombs < maxBombs) {
			int x = (int) (b2body.getPosition().x * BomberGame.PPM / 16);
			int y = (int) (b2body.getPosition().y * BomberGame.PPM / 16);
			float xSum = x + 0.5f;
			float ySum = y + 0.5f;
			float finalPosX = xSum * 16 / BomberGame.PPM;
			float finalPosY = ySum * 16 / BomberGame.PPM;
			
			screen.getBombs().add(new Bomb(screen, finalPosX, finalPosY,
					BomberGame.PLAYER_BOMB_BIT, Bomb.Team.PLAYER));
			
			currentBombs += 1;
		}
		
		if (!hasMovementInput()) {
			currentState = State.IDLE;
			b2body.setLinearVelocity(new Vector2(0, 0));
			return;
		}
		
		velocity = new Vector2(0, 0);
		
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

		velocity = velocity.nor();
		b2body.setLinearVelocity(velocity.setLength(0.5f));
	}
	
	public void update() {
		setPosition(b2body.getPosition().x - getWidth() / 2,
				b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame());
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
 	
	private void defineBody() {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(24 / BomberGame.PPM, 24 / BomberGame.PPM);
		
		b2body = world.createBody(bdef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(7 / BomberGame.PPM);
		
		fdef = new FixtureDef();
		fdef.filter.categoryBits = BomberGame.PLAYER_BIT;
		fdef.filter.maskBits = BomberGame.DEFAULT_BIT | BomberGame.HEAVY_BLOCK_BIT |
				BomberGame.LIGHT_BLOCK_BIT | BomberGame.ENEMY_BOMB_BIT |
				BomberGame.OPAQUE_BOMB_BIT;
		fdef.shape = shape;
		
		b2body.createFixture(fdef);
	}
}
