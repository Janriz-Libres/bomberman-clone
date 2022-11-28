package com.tempura.bomberman.Objects;

import java.util.Random;

import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Objects.Powerup.PowerType;
import com.tempura.bomberman.Screens.PlayScreen;

public class LightBlock extends Block {
	
	private PlayScreen screen;
	
	public LightBlock(PlayScreen screen) {
		super(screen, 3, BomberGame.LIGHT_BLOCK_BIT);
		this.screen = screen;
	}
	
	public void spawnPowerup(float x, float y) {
		Random rand = new Random();
		PowerType type = determineType(rand.nextInt(3));
		
		screen.addPowerup(new Powerup(screen, x, y, type));
	}
	
	public PowerType determineType(int probability) {
		switch (probability) {
		case 0:
			return PowerType.CAPACITY;
		case 1:
			return PowerType.RANGE;
		case 2:
		default:
			return PowerType.SPEED;
		}
	}
}
