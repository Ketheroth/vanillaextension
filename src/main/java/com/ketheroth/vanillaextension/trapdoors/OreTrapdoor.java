package com.ketheroth.vanillaextension.trapdoors;

import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class OreTrapdoor extends TrapDoorBlock {

	private final UniformInt xpRange;

	public OreTrapdoor(Properties properties) {
		this(properties, UniformInt.of(0, 0));
	}

	public OreTrapdoor(Properties properties, UniformInt xpRange) {
		super(properties);
		this.xpRange = xpRange;
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.xpRange.sample(RANDOM) : 0;
	}

}
