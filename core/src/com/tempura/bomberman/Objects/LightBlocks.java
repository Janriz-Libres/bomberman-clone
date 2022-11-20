package com.tempura.bomberman.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tempura.bomberman.Screens.PlayScreen;

public class LightBlocks extends Sprite {

	private World world;
	private Body b2body;
	
	public LightBlocks(PlayScreen screen) {
		world = screen.getWorld();
		
		defineBody();
	}
	
	private void defineBody() {
		
	}
}
