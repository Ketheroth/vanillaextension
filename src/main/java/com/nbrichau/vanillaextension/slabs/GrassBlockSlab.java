package com.nbrichau.vanillaextension.slabs;

import com.nbrichau.vanillaextension.init.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.Random;

import static net.minecraft.state.properties.BlockStateProperties.*;

import net.minecraft.block.AbstractBlock.Properties;

public class GrassBlockSlab extends SlabBlock implements IGrowable {
	public GrassBlockSlab(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isClientSide()) {
			if (player.getItemInHand(handIn).getToolTypes().contains(ToolType.HOE)) {
				BlockState bs = SlabInit.farmland_slab.defaultBlockState().setValue(TYPE, state.getValue(TYPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
				worldIn.setBlockAndUpdate(pos, bs);
				worldIn.playSound(null, pos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				player.getItemInHand(handIn).hurtAndBreak(1, player, item -> item.broadcastBreakEvent(handIn));
				return ActionResultType.SUCCESS;
			}
			if (player.getItemInHand(handIn).getToolTypes().contains(ToolType.SHOVEL)) {
				BlockState bs = SlabInit.grass_path_slab.defaultBlockState().setValue(TYPE, state.getValue(TYPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
				worldIn.setBlockAndUpdate(pos, bs);
				worldIn.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
				player.getItemInHand(handIn).hurtAndBreak(1, player, item -> item.broadcastBreakEvent(handIn));
				return ActionResultType.SUCCESS;
			}
		}
		return super.use(state, worldIn, pos, player, handIn, hit);
	}

	@Override
	public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return worldIn.getBlockState(pos.above()).isAir();
	}

	@Override
	public boolean isBonemealSuccess(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		if (state.getValue(TYPE).equals(SlabType.TOP) || state.getValue(TYPE).equals(SlabType.DOUBLE)) {
			BlockPos blockpos = pos.above();
			BlockState blockstate = Blocks.GRASS.defaultBlockState();
			label48:
			for(int i = 0; i < 128; ++i) {
				BlockPos blockpos1 = blockpos;
				for(int j = 0; j < i / 16; ++j) {
					blockpos1 = blockpos1.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
					if (!worldIn.getBlockState(blockpos1.below()).is(this) || worldIn.getBlockState(blockpos1).isCollisionShapeFullBlock(worldIn, blockpos1)) {
						continue label48;
					}
				}
				BlockState blockstate2 = worldIn.getBlockState(blockpos1);
				if (blockstate2.is(blockstate.getBlock()) && rand.nextInt(10) == 0) {
					((IGrowable)blockstate.getBlock()).performBonemeal(worldIn, rand, blockpos1, blockstate2);
				}
				if (blockstate2.isAir()) {
					BlockState blockstate1;
					if (rand.nextInt(8) == 0) {
						List<ConfiguredFeature<?, ?>> list = worldIn.getBiome(blockpos1).getGenerationSettings().getFlowerFeatures();
						if (list.isEmpty()) {
							continue;
						}
						ConfiguredFeature<?, ?> configuredfeature = list.get(0);
						FlowersFeature flowersfeature = (FlowersFeature)configuredfeature.feature;
						blockstate1 = flowersfeature.getRandomFlower(rand, blockpos1, configuredfeature.config());
					} else {
						blockstate1 = blockstate;
					}
					if (blockstate1.canSurvive(worldIn, blockpos1)) {
						worldIn.setBlock(blockpos1, blockstate1, 3);
					}
				}
			}
		}
	}

	private static boolean isSnowyConditions(BlockState state, IWorldReader worldReader, BlockPos pos) {
		BlockPos blockpos = pos.above();
		BlockState blockstate = worldReader.getBlockState(blockpos);
		if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowBlock.LAYERS) == 1) {
			return true;
		} else if (blockstate.getFluidState().getAmount() == 8) {
			return false;
		} else {
			int i = LightEngine.getLightBlockInto(worldReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(worldReader, blockpos));
			return i < worldReader.getMaxLightLevel();
		}
	}

	private static boolean isSnowyAndNotUnderwater(BlockState state, IWorldReader worldReader, BlockPos pos) {
		BlockPos blockpos = pos.above();
		return isSnowyConditions(state, worldReader, pos) && !worldReader.getFluidState(blockpos).is(FluidTags.WATER);
	}

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!isSnowyConditions(state, worldIn, pos)) {
			if (!worldIn.isAreaLoaded(pos, 3))
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
			worldIn.setBlockAndUpdate(pos, SlabInit.dirt_slab.defaultBlockState().setValue(TYPE, state.getValue(TYPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
		} else {
			if (worldIn.getMaxLocalRawBrightness(pos.above()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					BlockState blockstate = worldIn.getBlockState(blockpos);
					if (isSnowyAndNotUnderwater(blockstate, worldIn, blockpos)) {
						if (blockstate.is(Blocks.DIRT)) {
							worldIn.setBlockAndUpdate(blockpos, Blocks.GRASS_BLOCK.defaultBlockState().setValue(SNOWY, worldIn.getBlockState(blockpos.above()).is(Blocks.SNOW)));
						} else if (blockstate.is(StairsInit.dirt_stairs)) {
							worldIn.setBlockAndUpdate(blockpos, StairsInit.grass_block_stairs.defaultBlockState().setValue(HORIZONTAL_FACING, blockstate.getValue(HORIZONTAL_FACING)).setValue(HALF, blockstate.getValue(HALF)).setValue(STAIRS_SHAPE, blockstate.getValue(STAIRS_SHAPE)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(SlabInit.dirt_slab)) {
							worldIn.setBlockAndUpdate(blockpos, SlabInit.grass_block_slab.defaultBlockState().setValue(TYPE, blockstate.getValue(TYPE)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(FenceInit.dirt_fence)) {
							worldIn.setBlockAndUpdate(blockpos, FenceInit.grass_block_fence.defaultBlockState().setValue(NORTH, blockstate.getValue(NORTH)).setValue(EAST, blockstate.getValue(EAST)).setValue(SOUTH, blockstate.getValue(SOUTH)).setValue(WEST, blockstate.getValue(WEST)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(WallInit.dirt_wall)) {
							worldIn.setBlockAndUpdate(blockpos, WallInit.grass_block_wall.defaultBlockState().setValue(UP, blockstate.getValue(UP)).setValue(NORTH_WALL, blockstate.getValue(NORTH_WALL)).setValue(EAST_WALL, blockstate.getValue(EAST_WALL)).setValue(SOUTH_WALL, blockstate.getValue(SOUTH_WALL)).setValue(WEST_WALL, blockstate.getValue(WEST_WALL)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(TrapdoorInit.dirt_trapdoor)) {
							worldIn.setBlockAndUpdate(blockpos, TrapdoorInit.grass_block_trapdoor.defaultBlockState().setValue(HORIZONTAL_FACING, blockstate.getValue(HORIZONTAL_FACING)).setValue(OPEN, blockstate.getValue(OPEN)).setValue(HALF, blockstate.getValue(HALF)).setValue(POWERED, blockstate.getValue(POWERED)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						}
					}
				}
			}

		}
	}
}
