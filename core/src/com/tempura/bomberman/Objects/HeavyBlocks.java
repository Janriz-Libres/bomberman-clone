package com.tempura.bomberman.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.tempura.bomberman.BomberGame;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class HeavyBlocks extends Sprite {
	
	private World world;
	private TiledMap map;

	public HeavyBlocks(World world, TiledMap map) {
		this.world = world;
		this.map = map;
		defineBodies();
	}
	
	private void defineBodies() {
		Body body;
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		
		for(RectangleMapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = object.getRectangle();

            bdef.type = BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / BomberGame.PPM,
            		(rect.getY() + rect.getHeight() / 2) / BomberGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / BomberGame.PPM, rect.getHeight() / 2 / BomberGame.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = BomberGame.HEAVY_BLOCK_BIT;
            body.createFixture(fdef);
        }
	}
}
