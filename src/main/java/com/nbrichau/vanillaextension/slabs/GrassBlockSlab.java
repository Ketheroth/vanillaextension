package com.nbrichau.vanillaextension.slabs;

import com.nbrichau.vanillaextension.init.FenceInit;
import com.nbrichau.vanillaextension.init.SlabInit;
import com.nbrichau.vanillaextension.init.StairsInit;
import com.nbrichau.vanillaextension.init.WallInit;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.Random;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class GrassBlockSlab extends SlabBlock implements IGrowable {
	public GrassBlockSlab(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote()) {
			ItemStack its = player.getHeldItem(handIn);
			if (its.isItemEqualIgnoreDurability(Items.WOODEN_HOE.getDefaultInstance()) ||
					its.isItemEqualIgnoreDurability(Items.STONE_HOE.getDefaultInstance()) ||
					its.isItemEqualIgnoreDurability(Items.IRON_HOE.getDefaultInstance()) ||
					its.isItemEqualIgnoreDurability(Items.GOLDEN_HOE.getDefaultInstance()) ||
					its.isItemEqualIgnoreDurability(Items.DIAMOND_HOE.getDefaultInstance())) {
				BlockState bs = SlabInit.farmland_slab.getDefaultState().with(TYPE, state.get(TYPE)).with(WATERLOGGED, state.get(WATERLOGGED));
				worldIn.setBlockState(pos, bs);
				worldIn.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				player.getHeldItem(handIn).damageItem(1, player, item -> item.sendBreakAnimation(handIn));
				return ActionResultType.SUCCESS;
			}
			if (player.getHeldItem(handIn).getToolTypes().contains(ToolType.SHOVEL)) {
				BlockState bs = SlabInit.grass_path_slab.getDefaultState().with(TYPE, state.get(TYPE)).with(WATERLOGGED, state.get(WATERLOGGED));
				worldIn.setBlockState(pos, bs);
				worldIn.playSound(null, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
				player.getHeldItem(handIn).damageItem(1, player, item -> item.sendBreakAnimation(handIn));
				return ActionResultType.SUCCESS;
			}
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return worldIn.getBlockState(pos.up()).isAir();
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		if (state.get(TYPE).equals(SlabType.TOP) || state.get(TYPE).equals(SlabType.DOUBLE)) {
			BlockPos blockpos = pos.up();
			BlockState blockstate = Blocks.GRASS.getDefaultState();

			for (int i = 0; i < 128; ++i) {
				BlockPos blockpos1 = blockpos;
				int j = 0;

				while (true) {
					if (j >= i / 16) {
						BlockState blockstate2 = worldIn.getBlockState(blockpos1);
						if (blockstate2.getBlock() == blockstate.getBlock() && rand.nextInt(10) == 0) {
							((IGrowable) blockstate.getBlock()).grow(worldIn, rand, blockpos1, blockstate2);
						}

						if (!blockstate2.isAir()) {
							break;
						}

						BlockState blockstate1;
						if (rand.nextInt(8) == 0) {
							List<ConfiguredFeature<?, ?>> list = worldIn.getBiome(blockpos1).getFlowers();
							if (list.isEmpty()) {
								break;
							}

							ConfiguredFeature<?, ?> configuredfeature = ((DecoratedFeatureConfig) (list.get(0)).config).feature;
							blockstate1 = ((FlowersFeature) configuredfeature.feature).getFlowerToPlace(rand, blockpos1, configuredfeature.config);
						} else {
							blockstate1 = blockstate;
						}

						if (blockstate1.isValidPosition(worldIn, blockpos1)) {
							worldIn.setBlockState(blockpos1, blockstate1, 3);
						}
						break;
					}

					blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
					if (worldIn.getBlockState(blockpos1.down()).getBlock() != this || worldIn.getBlockState(blockpos1).isCollisionShapeOpaque(worldIn, blockpos1)) {
						break;
					}

					++j;
				}
			}
		}
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

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!isSnowyConditions(state, worldIn, pos)) {
			if (!worldIn.isAreaLoaded(pos, 3))
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
			worldIn.setBlockState(pos, SlabInit.dirt_slab.getDefaultState().with(TYPE, state.get(TYPE)).with(WATERLOGGED, state.get(WATERLOGGED)));
		} else {
			if (worldIn.getLight(pos.up()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					BlockState blockstate = worldIn.getBlockState(blockpos);
					Block block = worldIn.getBlockState(blockpos).getBlock();
					if (isSnowyAndNotUnderwater(blockstate, worldIn, blockpos)) {
						if (block == Blocks.DIRT) {
							worldIn.setBlockState(blockpos, Blocks.GRASS_BLOCK.getDefaultState().with(SNOWY, worldIn.getBlockState(blockpos.up()).getBlock() == Blocks.SNOW));
						} else if (block == StairsInit.dirt_stairs) {
							worldIn.setBlockState(blockpos, StairsInit.grass_block_stairs.getDefaultState().with(HORIZONTAL_FACING, blockstate.get(HORIZONTAL_FACING)).with(HALF, blockstate.get(HALF)).with(STAIRS_SHAPE, blockstate.get(STAIRS_SHAPE)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} else if (block == SlabInit.dirt_slab) {
							worldIn.setBlockState(blockpos, SlabInit.grass_block_slab.getDefaultState().with(TYPE, blockstate.get(TYPE)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} else if (block == FenceInit.dirt_fence) {
							worldIn.setBlockState(blockpos, FenceInit.grass_block_fence.getDefaultState().with(NORTH, blockstate.get(NORTH)).with(EAST, blockstate.get(EAST)).with(SOUTH, blockstate.get(SOUTH)).with(WEST, blockstate.get(WEST)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} else if (block == WallInit.dirt_wall) {
							worldIn.setBlockState(blockpos, WallInit.grass_block_wall.getDefaultState().with(UP, blockstate.get(UP)).with(NORTH, blockstate.get(NORTH)).with(EAST, blockstate.get(EAST)).with(SOUTH, blockstate.get(SOUTH)).with(WEST, blockstate.get(WEST)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} /* else if (block == TrapdoorInit.dirt_trapdoor) {
							worldIn.setBlockState(blockpos, TrapdoorInit.grass_block_trapdoor.getDefaultState().with(HORIZONTAL_FACING, blockstate.get(HORIZONTAL_FACING)).with(OPEN, blockstate.get(OPEN)).with(HALF, blockstate.get(HALF)).with(POWERED, blockstate.get(POWERED)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} */
					}
				}
			}

		}
	}
}