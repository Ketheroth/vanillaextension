package com.ketheroth.vanillaextension.slabs;

import com.ketheroth.vanillaextension.init.VEBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.lighting.LayerLightEngine;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.EAST;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.EAST_WALL;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HALF;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.NORTH;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.NORTH_WALL;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.OPEN;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.SNOWY;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.SOUTH;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.SOUTH_WALL;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.STAIRS_SHAPE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.UP;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WEST;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WEST_WALL;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GrassBlockSlab extends DirtSlab implements BonemealableBlock {

	public GrassBlockSlab(Properties properties) {
		super(properties);
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

	@Override
	public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return worldIn.getBlockState(pos.above()).isAir();
	}

	@Override
	public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
		if (state.getValue(TYPE).equals(SlabType.TOP) || state.getValue(TYPE).equals(SlabType.DOUBLE)) {
			BlockPos blockpos = pos.above();
			BlockState blockstate = Blocks.GRASS.defaultBlockState();
			label48:
			for (int i = 0; i < 128; ++i) {
				BlockPos blockpos1 = blockpos;
				for (int j = 0; j < i / 16; ++j) {
					blockpos1 = blockpos1.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
					if (!worldIn.getBlockState(blockpos1.below()).is(this) || worldIn.getBlockState(blockpos1).isCollisionShapeFullBlock(worldIn, blockpos1)) {
						continue label48;
					}
				}
				BlockState blockstate2 = worldIn.getBlockState(blockpos1);
				if (blockstate2.is(blockstate.getBlock()) && rand.nextInt(10) == 0) {
					((BonemealableBlock) blockstate.getBlock()).performBonemeal(worldIn, rand, blockpos1, blockstate2);
				}
				if (blockstate2.isAir()) {
					Holder<PlacedFeature> holder;
					if (rand.nextInt(8) == 0) {
						List<ConfiguredFeature<?, ?>> list = worldIn.getBiome(blockpos1).value().getGenerationSettings().getFlowerFeatures();
						if (list.isEmpty()) {
							continue;
						}

						holder = ((RandomPatchConfiguration)list.get(0).config()).feature();
					} else {
						holder = VegetationPlacements.GRASS_BONEMEAL;
					}

					holder.value().place(worldIn, worldIn.getChunkSource().getGenerator(), rand, blockpos1);
				}
			}
		}
	}

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		if (!isSnowyConditions(state, worldIn, pos)) {
			if (!worldIn.isAreaLoaded(pos, 3))
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
			worldIn.setBlockAndUpdate(pos, VEBlocks.dirt_slab.get().defaultBlockState().setValue(TYPE, state.getValue(TYPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
		} else {
			if (worldIn.getMaxLocalRawBrightness(pos.above()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					BlockState blockstate = worldIn.getBlockState(blockpos);
					if (isSnowyAndNotUnderwater(blockstate, worldIn, blockpos)) {
						if (blockstate.is(Blocks.DIRT)) {
							worldIn.setBlockAndUpdate(blockpos, Blocks.GRASS_BLOCK.defaultBlockState().setValue(SNOWY, worldIn.getBlockState(blockpos.above()).is(Blocks.SNOW)));
						} else if (blockstate.is(VEBlocks.dirt_stairs.get())) {
							worldIn.setBlockAndUpdate(blockpos, VEBlocks.grass_block_stairs.get().defaultBlockState().setValue(HORIZONTAL_FACING, blockstate.getValue(HORIZONTAL_FACING)).setValue(HALF, blockstate.getValue(HALF)).setValue(STAIRS_SHAPE, blockstate.getValue(STAIRS_SHAPE)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(VEBlocks.dirt_slab.get())) {
							worldIn.setBlockAndUpdate(blockpos, VEBlocks.grass_block_slab.get().defaultBlockState().setValue(TYPE, blockstate.getValue(TYPE)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(VEBlocks.dirt_fence.get())) {
							worldIn.setBlockAndUpdate(blockpos, VEBlocks.grass_block_fence.get().defaultBlockState().setValue(NORTH, blockstate.getValue(NORTH)).setValue(EAST, blockstate.getValue(EAST)).setValue(SOUTH, blockstate.getValue(SOUTH)).setValue(WEST, blockstate.getValue(WEST)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(VEBlocks.dirt_wall.get())) {
							worldIn.setBlockAndUpdate(blockpos, VEBlocks.grass_block_wall.get().defaultBlockState().setValue(UP, blockstate.getValue(UP)).setValue(NORTH_WALL, blockstate.getValue(NORTH_WALL)).setValue(EAST_WALL, blockstate.getValue(EAST_WALL)).setValue(SOUTH_WALL, blockstate.getValue(SOUTH_WALL)).setValue(WEST_WALL, blockstate.getValue(WEST_WALL)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						} else if (blockstate.is(VEBlocks.dirt_trapdoor.get())) {
							worldIn.setBlockAndUpdate(blockpos, VEBlocks.grass_block_trapdoor.get().defaultBlockState().setValue(HORIZONTAL_FACING, blockstate.getValue(HORIZONTAL_FACING)).setValue(OPEN, blockstate.getValue(OPEN)).setValue(HALF, blockstate.getValue(HALF)).setValue(POWERED, blockstate.getValue(POWERED)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED)));
						}
					}
				}
			}
		}
	}

}
