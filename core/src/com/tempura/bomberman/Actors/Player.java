package com.tempura.bomberman.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Objects.Bomb;
import com.tempura.bomberman.Screens.PlayScreen;

public class Player extends Character {
	
	public Player(World world, TiledMap map, PlayScreen screen) {
		super(world, map, screen);
	}

	@Override
	protected void defineAnimations() {
		playerIdleUp = new TextureRegion(getTexture(), 2, 2, 16, 16);
		playerIdleRight = new TextureRegion(getTexture(), 2 + 4 * 16, 2, 16, 16);
		playerIdleDown = new TextureRegion(getTexture(), 2 + 16, 2, 16, 16);
		
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
	
	@Override
	protected boolean hasMovementInput() {
		if (Gdx.input.isKeyPressed(Keys.A)) return true;
		if (Gdx.input.isKeyPressed(Keys.D)) return true;
		if (Gdx.input.isKeyPressed(Keys.W)) return true;
		if (Gdx.input.isKeyPressed(Keys.S)) return true;
		return false;
	}
	
	@Override
	protected void dropBomb() {
		if (Gdx.input.isKeyJustPressed(Keys.F) && currentBombs < maxBombs && !isDead) {
			int x = (int) (b2body.getPosition().x * BomberGame.PPM / 16);
			int y = (int) (b2body.getPosition().y * BomberGame.PPM / 16);
			float xSum = x + 0.5f;
			float ySum = y + 0.5f;
			float finalPosX = xSum * 16 / BomberGame.PPM;
			float finalPosY = ySum * 16 / BomberGame.PPM;
			
			screen.getBombs().add(new Bomb(screen, finalPosX, finalPosY,
					BomberGame.PLAYER_BOMB_BIT, Bomb.Team.PLAYER, range));
			dropSFX.play();
			
			currentBombs += 1;
		}
	}
	
	@Override
	protected void handleMovement() {
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
	}
 	
	@Override
	public void defineBodies() {
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
				BomberGame.OPAQUE_BOMB_BIT | BomberGame.EXPLOSION_BIT | BomberGame.POWERUP_BIT;
		fdef.shape = shape;
		
		b2body.createFixture(fdef).setUserData(this);
	}
}
