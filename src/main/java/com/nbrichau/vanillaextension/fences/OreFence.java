package com.nbrichau.vanillaextension.fences;

import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class OreFence extends FenceBlock {

	private final UniformInt xpRange;

	public OreFence(Properties properties) {
		this(properties, UniformInt.of(0, 0));
	}

	public OreFence(Properties properties, UniformInt xpRange) {
		super(properties);
		this.xpRange = xpRange;
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.xpRange.sample(RANDOM) : 0;
	}

}
