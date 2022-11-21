package com.tempura.bomberman.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Objects.Bomb;

public class WorldContactListener extends GameContactListener {

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
