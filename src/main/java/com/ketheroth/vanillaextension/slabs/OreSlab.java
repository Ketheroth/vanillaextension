package com.ketheroth.vanillaextension.slabs;

import com.ketheroth.vanillaextension.init.SlabInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class OreSlab extends SlabBlock {
	public OreSlab(Properties properties) {
		super(properties);
	}

	protected int getExperience(Random rand) {
		if (this == SlabInit.coal_ore_slab.get()) {
			return MathHelper.nextInt(rand, 0, 1);
		} else if (this == SlabInit.diamond_ore_slab.get()) {
			return MathHelper.nextInt(rand, 2, 4);
		} else if (this == SlabInit.emerald_ore_slab.get()) {
			return MathHelper.nextInt(rand, 2, 4);
		} else if (this == SlabInit.lapis_ore_slab.get()) {
			return MathHelper.nextInt(rand, 1, 3);
		} else if (this == SlabInit.nether_quartz_ore_slab.get()) {
			return MathHelper.nextInt(rand, 1, 3);
		} else {
			return this == SlabInit.nether_gold_ore_slab.get() ? MathHelper.nextInt(rand, 0, 1) : 0;
		}
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.getExperience(RANDOM) : 0;
	}
}
