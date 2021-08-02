package com.ketheroth.vanillaextension.trapdoors;

import com.ketheroth.vanillaextension.init.TrapdoorInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class OreTrapdoor extends TrapDoorBlock {

	public OreTrapdoor(Properties properties) {
		super(properties);
	}

	protected int getExperience(Random rand) {
		if (this == TrapdoorInit.coal_ore_trapdoor.get()) {
			return MathHelper.nextInt(rand, 0, 2);
		} else if (this == TrapdoorInit.diamond_ore_trapdoor.get()) {
			return MathHelper.nextInt(rand, 3, 7);
		} else if (this == TrapdoorInit.emerald_ore_trapdoor.get()) {
			return MathHelper.nextInt(rand, 3, 7);
		} else if (this == TrapdoorInit.lapis_ore_trapdoor.get()) {
			return MathHelper.nextInt(rand, 2, 5);
		} else if (this == TrapdoorInit.nether_quartz_ore_trapdoor.get()) {
			return MathHelper.nextInt(rand, 2, 5);
		} else if (this == TrapdoorInit.redstone_ore_trapdoor.get()) {
			return MathHelper.nextInt(rand, 1, 5);
		} else {
			return this == TrapdoorInit.nether_gold_ore_trapdoor.get() ? MathHelper.nextInt(rand, 0, 1) : 0;
		}
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.getExperience(RANDOM) : 0;
	}
}
