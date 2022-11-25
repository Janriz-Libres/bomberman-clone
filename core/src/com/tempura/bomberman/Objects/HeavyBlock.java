package com.tempura.bomberman.Objects;

import com.tempura.bomberman.BomberGame;
import com.tempura.bomberman.Screens.PlayScreen;

public class HeavyBlock extends Block {
	
	public HeavyBlock(PlayScreen screen) {
		super(screen, 2, BomberGame.HEAVY_BLOCK_BIT);
	}
}
