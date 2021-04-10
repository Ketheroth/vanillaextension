package com.nbrichau.vanillaextension.fences;

import com.nbrichau.vanillaextension.init.FenceInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class OreFence extends FenceBlock {
	public OreFence(Properties properties) {
		super(properties);
	}

	protected int getExperience(Random rand) {
		if (this == FenceInit.coal_ore_fence.get()) {
			return MathHelper.nextInt(rand, 0, 2);
		} else if (this == FenceInit.diamond_ore_fence.get()) {
			return MathHelper.nextInt(rand, 3, 7);
		} else if (this == FenceInit.emerald_ore_fence.get()) {
			return MathHelper.nextInt(rand, 3, 7);
		} else if (this == FenceInit.lapis_ore_fence.get()) {
			return MathHelper.nextInt(rand, 2, 5);
		} else if (this == FenceInit.nether_quartz_ore_fence.get()) {
			return MathHelper.nextInt(rand, 2, 5);
		} else {
			return this == FenceInit.nether_gold_ore_fence.get() ? MathHelper.nextInt(rand, 0, 1) : 0;
		}
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.getExperience(RANDOM) : 0;
	}
}
