package com.tempura.bomberman.Objects;

import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Screens.PlayScreen;

public class LightBlock extends Block {
	public LightBlock(PlayScreen screen) {
		super(screen, 3, BomberGame.LIGHT_BLOCK_BIT);
	}
}
