package com.nbrichau.vanillaextension.walls;

import com.nbrichau.vanillaextension.init.FenceInit;
import com.nbrichau.vanillaextension.init.SlabInit;
import com.nbrichau.vanillaextension.init.StairsInit;
import com.nbrichau.vanillaextension.init.WallInit;
import net.minecraft.block.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class MyceliumWall extends WallBlock {
	public MyceliumWall(Properties properties) {
		super(properties);
	}

	private static boolean isSnowyConditions(BlockState state, IWorldReader worldReader, BlockPos pos) {
		BlockPos blockpos = pos.up();
		BlockState blockstate = worldReader.getBlockState(blockpos);
		if (blockstate.getBlock() == Blocks.SNOW && blockstate.get(SnowBlock.LAYERS) == 1) {
			return true;
		} else if (blockstate.getFluidState().getLevel() == 8) {
			return false;
		} else {
			int i = LightEngine.func_215613_a(worldReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getOpacity(worldReader, blockpos));
			return i < worldReader.getMaxLightLevel();
		}
	}

	private static boolean isSnowyAndNotUnderwater(BlockState state, IWorldReader worldReader, BlockPos pos) {
		BlockPos blockpos = pos.up();
		return isSnowyConditions(state, worldReader, pos) && !worldReader.getFluidState(blockpos).isTagged(FluidTags.WATER);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!isSnowyConditions(state, worldIn, pos)) {
			if (!worldIn.isAreaLoaded(pos, 3))
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
			worldIn.setBlockState(pos, WallInit.dirt_wall.getDefaultState().with(UP, state.get(UP)).with(NORTH, state.get(NORTH)).with(EAST, state.get(EAST)).with(SOUTH, state.get(SOUTH)).with(WEST, state.get(WEST)).with(WATERLOGGED, state.get(WATERLOGGED)));
		} else {
			if (worldIn.getLight(pos.up()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					BlockState blockstate = worldIn.getBlockState(blockpos);
					Block block = worldIn.getBlockState(blockpos).getBlock();
					if (isSnowyAndNotUnderwater(blockstate, worldIn, blockpos)) {
						if (block == Blocks.DIRT) {
							worldIn.setBlockState(blockpos, Blocks.MYCELIUM.getDefaultState().with(SNOWY, worldIn.getBlockState(blockpos.up()).getBlock() == Blocks.SNOW));
						} else if (block == StairsInit.dirt_stairs) {
							worldIn.setBlockState(blockpos, StairsInit.mycelium_stairs.getDefaultState().with(HORIZONTAL_FACING, blockstate.get(HORIZONTAL_FACING)).with(HALF, blockstate.get(HALF)).with(STAIRS_SHAPE, blockstate.get(STAIRS_SHAPE)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} else if (block == SlabInit.dirt_slab) {
							worldIn.setBlockState(blockpos, SlabInit.mycelium_slab.getDefaultState().with(SLAB_TYPE, blockstate.get(SLAB_TYPE)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} else if (block == FenceInit.dirt_fence) {
							worldIn.setBlockState(blockpos, FenceInit.mycelium_fence.getDefaultState().with(NORTH, blockstate.get(NORTH)).with(EAST, blockstate.get(EAST)).with(SOUTH, blockstate.get(SOUTH)).with(WEST, blockstate.get(WEST)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} else if (block == WallInit.dirt_wall) {
							worldIn.setBlockState(blockpos, WallInit.mycelium_wall.getDefaultState().with(UP, blockstate.get(UP)).with(NORTH, blockstate.get(NORTH)).with(EAST, blockstate.get(EAST)).with(SOUTH, blockstate.get(SOUTH)).with(WEST, blockstate.get(WEST)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} /* else if (block == TrapdoorInit.dirt_trapdoor) {
							worldIn.setBlockState(blockpos, TrapdoorInit.mycelium_trapdoor.getDefaultState().with(HORIZONTAL_FACING, blockstate.get(HORIZONTAL_FACING)).with(OPEN, blockstate.get(OPEN)).with(HALF, blockstate.get(HALF)).with(POWERED, blockstate.get(POWERED)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} */
					}
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.animateTick(stateIn, worldIn, pos, rand);
		if (rand.nextInt(10) == 0) {
			worldIn.addParticle(ParticleTypes.MYCELIUM, (double) pos.getX() + rand.nextDouble(), (double) pos.getY() + 1.1D, (double) pos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);
		}
	}

}