package com.nbrichau.vanillaextension.trapdoors;

import com.nbrichau.vanillaextension.init.FenceInit;
import com.nbrichau.vanillaextension.init.TrapdoorInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GrassPathTrapdoor extends TrapDoorBlock {

	protected static final VoxelShape EAST_OPEN_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 15.0D, 16.0D);
	protected static final VoxelShape WEST_OPEN_AABB = Block.makeCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape SOUTH_OPEN_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 3.0D);
	protected static final VoxelShape NORTH_OPEN_AABB = Block.makeCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape BOTTOM_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	protected static final VoxelShape TOP_AABB = Block.makeCuboidShape(0.0D, 13.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public GrassPathTrapdoor(Properties properties) {
		super(properties);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState blockstate = !this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) ?
				TrapdoorInit.dirt_trapdoor.getDefaultState() : this.getDefaultState();
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
		Direction direction = context.getFace();
		if (!context.replacingClickedOnBlock() && direction.getAxis().isHorizontal()) {
			blockstate = blockstate.with(HORIZONTAL_FACING, direction).with(HALF, context.getHitVec().y - (double) context.getPos().getY() > 0.5D ? Half.TOP : Half.BOTTOM);
		} else {
			blockstate = blockstate.with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(HALF, direction == Direction.UP ? Half.BOTTOM : Half.TOP);
		}

		if (context.getWorld().isBlockPowered(context.getPos())) {
			blockstate = blockstate.with(OPEN, Boolean.TRUE).with(POWERED, Boolean.TRUE);
		}

		return blockstate.with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.UP && !stateIn.isValidPosition(worldIn, currentPos)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (!this.isValidPosition(state, worldIn, pos)) {
			worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, FenceInit.dirt_fence.getDefaultState()
					.with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING)).with(OPEN, state.get(OPEN)).with(HALF, state.get(HALF))
					.with(POWERED, state.get(POWERED)).with(WATERLOGGED, state.get(WATERLOGGED)), worldIn, pos));
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.up());
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock;
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (!state.get(OPEN)) {
			return state.get(HALF) == Half.TOP ? TOP_AABB : BOTTOM_AABB;
		} else {
			switch ((Direction) state.get(HORIZONTAL_FACING)) {
				case NORTH:
				default:
					return NORTH_OPEN_AABB;
				case SOUTH:
					return SOUTH_OPEN_AABB;
				case WEST:
					return WEST_OPEN_AABB;
				case EAST:
					return EAST_OPEN_AABB;
			}
		}

	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		switch (type) {
			case LAND:
				return state.get(OPEN);
			case WATER:
				return state.get(WATERLOGGED);
			case AIR:
				return state.get(OPEN);
			default:
				return false;
		}
	}
}
