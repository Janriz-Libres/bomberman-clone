package com.tempura.bomberman.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Objects.Bomb;
import com.tempura.bomberman.Objects.LightBlock;
import com.tempura.bomberman.Screens.PlayScreen;

public class WorldContactListener extends GameContactListener {
	
	private PlayScreen screen;
	
	public WorldContactListener(PlayScreen screen) {
		this.screen = screen;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		int filterData = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		
		if (filterData == (BomberGame.EXPLOSION_BIT | BomberGame.LIGHT_BLOCK_BIT)) {
			Fixture lightBlock = fixA.getFilterData().categoryBits == BomberGame.LIGHT_BLOCK_BIT ?
					fixA : fixB;
			screen.getDestroyables().add(lightBlock.getBody());
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		int filterData = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		
		if (filterData == (BomberGame.PLAYER_BIT | BomberGame.OPAQUE_BOMB_BIT)) {
			Fixture bombObject = fixA.getFilterData().categoryBits == BomberGame.OPAQUE_BOMB_BIT ?
					fixA : fixB;
			Bomb bomb = ((Bomb) bombObject.getUserData());
			
			if (bombObject.isSensor() && bomb.getCategoryBits() != BomberGame.OPAQUE_BOMB_BIT) {
				bomb.setToOpaque();
			}
		}
	}
	
}
