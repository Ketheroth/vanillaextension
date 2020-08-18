package com.nbrichau.vanillaextension.fences;

import com.nbrichau.vanillaextension.init.BlockInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class GrassBlockFence extends FenceBlock {

	public GrassBlockFence(Properties properties) {
		super(properties);
	}

	private static boolean isSnowyConditions(BlockState state, IWorldReader worldReader, BlockPos pos) {
		BlockPos blockpos = pos.up();
		BlockState blockstate = worldReader.getBlockState(blockpos);
		if (blockstate.isIn(Blocks.SNOW) && blockstate.get(SnowBlock.LAYERS) == 1) {
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

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!isSnowyConditions(state, worldIn, pos)) {
			if (!worldIn.isAreaLoaded(pos, 3))
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
			worldIn.setBlockState(pos, BlockInit.dirt_wall.getDefaultState().with(NORTH, state.get(NORTH)).with(EAST, state.get(EAST)).with(SOUTH, state.get(SOUTH)).with(WEST, state.get(WEST)).with(WATERLOGGED, state.get(WATERLOGGED)));
		} else {
			if (worldIn.getLight(pos.up()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					BlockState blockstate = worldIn.getBlockState(blockpos);
					if (isSnowyAndNotUnderwater(blockstate, worldIn, blockpos)) {
						if (blockstate.isIn(Blocks.DIRT)) {
							worldIn.setBlockState(blockpos, Blocks.GRASS_BLOCK.getDefaultState().with(SNOWY, worldIn.getBlockState(blockpos.up()).isIn(Blocks.SNOW)));
						} else if (blockstate.isIn(BlockInit.dirt_stairs)) {
							worldIn.setBlockState(blockpos, BlockInit.grass_block_stairs.getDefaultState().with(HORIZONTAL_FACING, blockstate.get(HORIZONTAL_FACING)).with(HALF, blockstate.get(HALF)).with(STAIRS_SHAPE, blockstate.get(STAIRS_SHAPE)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} else if (blockstate.isIn(BlockInit.dirt_slab)) {
							worldIn.setBlockState(blockpos, BlockInit.grass_block_slab.getDefaultState().with(SLAB_TYPE, blockstate.get(SLAB_TYPE)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} else if (blockstate.isIn(BlockInit.dirt_fence)) {
							worldIn.setBlockState(blockpos, BlockInit.grass_block_fence.getDefaultState().with(NORTH, blockstate.get(NORTH)).with(EAST, blockstate.get(EAST)).with(SOUTH, blockstate.get(SOUTH)).with(WEST, blockstate.get(WEST)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} else if (blockstate.isIn(BlockInit.dirt_wall)) {
							worldIn.setBlockState(blockpos, BlockInit.grass_block_wall.getDefaultState().with(UP, blockstate.get(UP)).with(WALL_HEIGHT_NORTH, blockstate.get(WALL_HEIGHT_NORTH)).with(WALL_HEIGHT_EAST, blockstate.get(WALL_HEIGHT_EAST)).with(WALL_HEIGHT_SOUTH, blockstate.get(WALL_HEIGHT_SOUTH)).with(WALL_HEIGHT_WEST, blockstate.get(WALL_HEIGHT_WEST)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						}
					}
				}
			}
		}
	}
}
