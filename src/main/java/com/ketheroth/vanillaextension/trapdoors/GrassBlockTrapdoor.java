package com.ketheroth.vanillaextension.trapdoors;

import com.ketheroth.vanillaextension.init.VEBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.lighting.LayerLightEngine;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GrassBlockTrapdoor extends FlattenableTrapdoor {

	public GrassBlockTrapdoor(Properties properties) {
		super(properties);
	}

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		if (!isSnowyConditions(state, worldIn, pos)) {
			if (!worldIn.isAreaLoaded(pos, 3))
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
			worldIn.setBlockAndUpdate(pos, VEBlocks.dirt_trapdoor.get().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(OPEN, state.getValue(OPEN)).setValue(HALF, state.getValue(HALF)).setValue(POWERED, state.getValue(POWERED)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
		} else {
			if (worldIn.getMaxLocalRawBrightness(pos.above()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					BlockState blockstate = worldIn.getBlockState(blockpos);
					if (isSnowyAndNotUnderwater(blockstate, worldIn, blockpos)) {
						if (blockstate.is(Blocks.DIRT)) {
							worldIn.setBlockAndUpdate(blockpos, Blocks.GRASS_BLOCK.defaultBlockState().setValue(BlockStateProperties.SNOWY, worldIn.getBlockState(blockpos.above()).is(Blocks.SNOW)));
						} else if (blockstate.is(VEBlocks.dirt_stairs.get())) {
							worldIn.setBlockAndUpdate(blockpos, VEBlocks.grass_block_stairs.get().defaultBlockState().setValue(FACING, blockstate.getValue(FACING)).setValue(HALF, blockstate.getValue(HALF)).setValue(BlockStateProperties.STAIRS_SHAPE, blockstate.getValue(BlockStateProperties.STAIRS_SHAPE)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(VEBlocks.dirt_slab.get())) {
							worldIn.setBlockAndUpdate(blockpos, VEBlocks.grass_block_slab.get().defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, blockstate.getValue(BlockStateProperties.SLAB_TYPE)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(VEBlocks.dirt_fence.get())) {
							worldIn.setBlockAndUpdate(blockpos, VEBlocks.grass_block_fence.get().defaultBlockState().setValue(BlockStateProperties.NORTH, blockstate.getValue(BlockStateProperties.NORTH)).setValue(BlockStateProperties.EAST, blockstate.getValue(BlockStateProperties.EAST)).setValue(BlockStateProperties.SOUTH, blockstate.getValue(BlockStateProperties.SOUTH)).setValue(BlockStateProperties.WEST, blockstate.getValue(BlockStateProperties.WEST)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(VEBlocks.dirt_wall.get())) {
							worldIn.setBlockAndUpdate(blockpos, VEBlocks.grass_block_wall.get().defaultBlockState().setValue(BlockStateProperties.UP, blockstate.getValue(BlockStateProperties.UP)).setValue(BlockStateProperties.NORTH_WALL, blockstate.getValue(BlockStateProperties.NORTH_WALL)).setValue(BlockStateProperties.EAST_WALL, blockstate.getValue(BlockStateProperties.EAST_WALL)).setValue(BlockStateProperties.SOUTH_WALL, blockstate.getValue(BlockStateProperties.SOUTH_WALL)).setValue(BlockStateProperties.WEST_WALL, blockstate.getValue(BlockStateProperties.WEST_WALL)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(VEBlocks.dirt_trapdoor.get())) {
							worldIn.setBlockAndUpdate(blockpos, VEBlocks.grass_block_trapdoor.get().defaultBlockState().setValue(FACING, blockstate.getValue(FACING)).setValue(OPEN, blockstate.getValue(OPEN)).setValue(HALF, blockstate.getValue(HALF)).setValue(POWERED, blockstate.getValue(POWERED)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						}
					}
				}
			}
		}
	}

	private static boolean isSnowyConditions(BlockState state, LevelReader worldReader, BlockPos pos) {
		BlockPos blockpos = pos.above();
		BlockState blockstate = worldReader.getBlockState(blockpos);
		if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
			return true;
		} else if (blockstate.getFluidState().getAmount() == 8) {
			return false;
		} else {
			int i = LayerLightEngine.getLightBlockInto(worldReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(worldReader, blockpos));
			return i < worldReader.getMaxLightLevel();
		}
	}

	private static boolean isSnowyAndNotUnderwater(BlockState state, LevelReader worldReader, BlockPos pos) {
		BlockPos blockpos = pos.above();
		return isSnowyConditions(state, worldReader, pos) && !worldReader.getFluidState(blockpos).is(FluidTags.WATER);
	}

}
