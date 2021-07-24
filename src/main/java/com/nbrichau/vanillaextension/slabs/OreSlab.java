package com.nbrichau.vanillaextension.slabs;

import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

public class OreSlab extends SlabBlock {

	private final UniformInt xpRange;

	public OreSlab(Properties properties) {
		this(properties, UniformInt.of(0, 0));
	}

	public OreSlab(Properties properties, UniformInt xpRange) {
		super(properties);
		this.xpRange = xpRange;
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.xpRange.sample(RANDOM) : 0;
	}

}
