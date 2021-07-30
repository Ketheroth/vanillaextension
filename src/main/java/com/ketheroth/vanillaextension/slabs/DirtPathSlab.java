package com.ketheroth.vanillaextension.slabs;

import com.ketheroth.vanillaextension.init.SlabInit;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DirtPathSlab extends SlabBlock {

	protected static final VoxelShape BOTTOM_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public DirtPathSlab(Properties builder) {
		super(builder);
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = context.getLevel().getBlockState(blockpos);
//		!this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) ?
//				SlabInit.dirt_slab.getDefaultState() :

		if (blockstate.is(this)) {
			return this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ?
					blockstate.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, Boolean.FALSE) :
					SlabInit.dirt_slab.get().defaultBlockState().setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, Boolean.FALSE);
		} else {
			FluidState fluidstate = context.getLevel().getFluidState(blockpos);
			BlockState bs = this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ?
					this.defaultBlockState() : SlabInit.dirt_slab.get().defaultBlockState();
			BlockState blockstate1 = bs.setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
			Direction direction = context.getClickedFace();
			return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.setValue(TYPE, SlabType.TOP);
		}
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.UP && !stateIn.canSurvive(worldIn, currentPos)) {
			worldIn.getBlockTicks().scheduleTick(currentPos, this, 1);
		}
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		if (!this.canSurvive(state, worldIn, pos)) {
			worldIn.setBlockAndUpdate(pos, pushEntitiesUp(state, SlabInit.dirt_slab.get().defaultBlockState()
					.setValue(TYPE, state.getValue(TYPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), worldIn, pos));
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.above());
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		SlabType slabtype = state.getValue(TYPE);
		return switch (slabtype) {
			case DOUBLE -> Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
			case TOP -> TOP_SHAPE;
			default -> BOTTOM_SHAPE;
		};
	}

}
