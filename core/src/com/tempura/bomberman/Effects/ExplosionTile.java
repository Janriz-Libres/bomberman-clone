package com.tempura.bomberman.Effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Effects.Explosion.ExplosionType;
import com.tempura.bomberman.Screens.PlayScreen;

public class ExplosionTile extends Sprite {
	
	private ExplosionType type;
	
	private World world;
	private Vector2 pos;
	private Animation<TextureRegion> anim;
	
	public Body b2body;
	
	public ExplosionTile(World world, ExplosionType type, Vector2 pos, AtlasRegion region, float rotation) {
		super(region);
		this.world = world;
		this.type = type;
		this.pos = pos;
		
		defineAnimations();
		defineBody();
		
		setBounds(0, 0, 16 / BomberGame.PPM, 16 / BomberGame.PPM);
		setPosition(pos.x - getWidth() / 2, pos.y - getHeight() / 2);
		setOriginCenter();
		rotate(rotation);
	}
	
	private void defineAnimations() {
		Array<TextureRegion> frames = new Array<>();
		
		int a = 0;
		if (type == ExplosionType.ARM) a = 1;
		else if (type == ExplosionType.TAIL) a = 2;
		
		for (int i = 0; i < 7; i++) {
			frames.add(new TextureRegion(getTexture(), 2 + i * 32, 2 + a * 32, 32, 32));
		}
		anim = new Animation<>(0.1f, frames);
	}
	
	public void update(float stateTimer) {
		setRegion(anim.getKeyFrame(stateTimer));
	}
	
	public void defineBody() {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(pos);
		
		b2body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(4 / BomberGame.PPM, 4 / BomberGame.PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.filter.categoryBits = BomberGame.EXPLOSION_BIT;
		fdef.filter.maskBits = BomberGame.DEFAULT_BIT | BomberGame.LIGHT_BLOCK_BIT | BomberGame.PLAYER_BIT;
		fdef.shape = shape;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef);
	}
}
