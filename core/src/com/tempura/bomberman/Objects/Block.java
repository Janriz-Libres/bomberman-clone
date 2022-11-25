package com.tempura.bomberman.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Screens.PlayScreen;
import com.tempura.bomberman.Tools.GameObject;

public class Block extends Sprite implements GameObject {
	
	private PlayScreen screen;
	
	private int layer;
	private short category;
	
	public Block(PlayScreen screen, int layer, short category) {
		this.screen = screen;
		this.layer = layer;
		this.category = category;
		
		defineBodies();
	}
	
	@Override
	public void defineBodies() {
		Body body;
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		
		for (RectangleMapObject object :
				screen.getMap().getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = object.getRectangle();
			
			bdef.type = BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / BomberGame.PPM,
					(rect.getY() + rect.getHeight() / 2) / BomberGame.PPM);
			
			body = screen.getWorld().createBody(bdef);
			
			shape.setAsBox(rect.getWidth() / 2 / BomberGame.PPM, rect.getHeight() / 2 / BomberGame.PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = category;
			fdef.filter.maskBits = BomberGame.DEFAULT_BIT | BomberGame.PLAYER_BIT | BomberGame.EXPLOSION_BIT;
			body.createFixture(fdef);
		}
	}
}
