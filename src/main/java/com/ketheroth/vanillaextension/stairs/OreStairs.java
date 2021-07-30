package com.ketheroth.vanillaextension.stairs;

import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class OreStairs extends StairBlock {

	private final UniformInt xpRange;

	public OreStairs(Supplier<BlockState> state, Properties properties) {
		this(state, properties, UniformInt.of(0, 0));
	}

	public OreStairs(Supplier<BlockState> state, Properties properties, UniformInt xpRange) {
		super(state, properties);
		this.xpRange = xpRange;
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.xpRange.sample(RANDOM) : 0;
	}

}
