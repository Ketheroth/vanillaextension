package com.nbrichau.vanillaextension.walls;

import com.nbrichau.vanillaextension.init.WallInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class OreWall extends WallBlock {

	public OreWall(Properties properties) {
		super(properties);
	}

	protected int getExperience(Random rand) {
		if (this == WallInit.coal_ore_wall) {
			return MathHelper.nextInt(rand, 0, 2);
		} else if (this == WallInit.diamond_ore_wall) {
			return MathHelper.nextInt(rand, 3, 7);
		} else if (this == WallInit.emerald_ore_wall) {
			return MathHelper.nextInt(rand, 3, 7);
		} else if (this == WallInit.lapis_ore_wall) {
			return MathHelper.nextInt(rand, 2, 5);
		} else if (this == WallInit.nether_quartz_ore_wall) {
			return MathHelper.nextInt(rand, 2, 5);
		}/* else {
			return this == WallInit.nether_gold_ore_wall ? MathHelper.nextInt(rand, 0, 1) : 0;
		}*/
		return 0;
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.getExperience(RANDOM) : 0;
	}
}
