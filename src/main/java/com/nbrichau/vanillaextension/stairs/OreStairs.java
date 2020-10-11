package com.nbrichau.vanillaextension.stairs;

import com.nbrichau.vanillaextension.init.StairsInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;
import java.util.function.Supplier;

public class OreStairs extends StairsBlock {

	public OreStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}

	protected int getExperience(Random rand) {
		if (this == StairsInit.coal_ore_stairs) {
			return MathHelper.nextInt(rand, 0, 2);
		} else if (this == StairsInit.diamond_ore_stairs) {
			return MathHelper.nextInt(rand, 3, 7);
		} else if (this == StairsInit.emerald_ore_stairs) {
			return MathHelper.nextInt(rand, 3, 7);
		} else if (this == StairsInit.lapis_ore_stairs) {
			return MathHelper.nextInt(rand, 2, 5);
		} else if (this == StairsInit.nether_quartz_ore_stairs) {
			return MathHelper.nextInt(rand, 2, 5);
		} else {
			return 0;
		}
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.getExperience(RANDOM) : 0;
	}
}