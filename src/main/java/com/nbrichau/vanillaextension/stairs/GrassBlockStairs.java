package com.nbrichau.vanillaextension.stairs;

import com.nbrichau.vanillaextension.init.FenceInit;
import com.nbrichau.vanillaextension.init.SlabInit;
import com.nbrichau.vanillaextension.init.StairsInit;
import com.nbrichau.vanillaextension.init.WallInit;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;
import java.util.function.Supplier;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class GrassBlockStairs extends StairsBlock {
	public GrassBlockStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
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
				BlockState bs = StairsInit.farmland_stairs.getDefaultState().with(FACING, state.get(FACING)).with(HALF, state.get(HALF)).with(SHAPE, state.get(SHAPE)).with(WATERLOGGED, state.get(WATERLOGGED));
				worldIn.setBlockState(pos, bs);
				worldIn.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				player.getHeldItem(handIn).damageItem(1, player, item -> item.sendBreakAnimation(handIn));
				return ActionResultType.SUCCESS;
			}
			if (player.getHeldItem(handIn).getToolTypes().contains(ToolType.SHOVEL)) {
				BlockState bs = StairsInit.grass_path_stairs.getDefaultState().with(FACING, state.get(FACING)).with(HALF, state.get(HALF)).with(SHAPE, state.get(SHAPE)).with(WATERLOGGED, state.get(WATERLOGGED));
				worldIn.setBlockState(pos, bs);
				worldIn.playSound(null, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
				player.getHeldItem(handIn).damageItem(1, player, item -> item.sendBreakAnimation(handIn));
				return ActionResultType.SUCCESS;
			}
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
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
			worldIn.setBlockState(pos, StairsInit.dirt_stairs.getDefaultState().with(FACING, state.get(FACING)).with(HALF, state.get(HALF)).with(SHAPE, state.get(SHAPE)).with(WATERLOGGED, state.get(WATERLOGGED)));
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
							worldIn.setBlockState(blockpos, StairsInit.grass_block_stairs.getDefaultState().with(FACING, blockstate.get(FACING)).with(HALF, blockstate.get(HALF)).with(SHAPE, blockstate.get(SHAPE)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
						} else if (block == SlabInit.dirt_slab) {
							worldIn.setBlockState(blockpos, SlabInit.grass_block_slab.getDefaultState().with(SLAB_TYPE, blockstate.get(SLAB_TYPE)).with(WATERLOGGED, blockstate.get(WATERLOGGED)));
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