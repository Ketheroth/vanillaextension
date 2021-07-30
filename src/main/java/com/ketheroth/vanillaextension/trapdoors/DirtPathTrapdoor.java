package com.ketheroth.vanillaextension.trapdoors;

import com.ketheroth.vanillaextension.init.TrapdoorInit;
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
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DirtPathTrapdoor extends TrapDoorBlock {

	protected static final VoxelShape EAST_OPEN_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 15.0D, 16.0D);
	protected static final VoxelShape WEST_OPEN_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape SOUTH_OPEN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 3.0D);
	protected static final VoxelShape NORTH_OPEN_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	protected static final VoxelShape TOP_AABB = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public DirtPathTrapdoor(Properties properties) {
		super(properties);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockstate = !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ?
				TrapdoorInit.dirt_trapdoor.get().defaultBlockState() : this.defaultBlockState();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		Direction direction = context.getClickedFace();
		if (!context.replacingClickedOnBlock() && direction.getAxis().isHorizontal()) {
			blockstate = blockstate.setValue(FACING, direction).setValue(HALF, context.getClickLocation().y - (double) context.getClickedPos().getY() > 0.5D ? Half.TOP : Half.BOTTOM);
		} else {
			blockstate = blockstate.setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(HALF, direction == Direction.UP ? Half.BOTTOM : Half.TOP);
		}

		if (context.getLevel().hasNeighborSignal(context.getClickedPos())) {
			blockstate = blockstate.setValue(OPEN, Boolean.TRUE).setValue(POWERED, Boolean.TRUE);
		}

		return blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
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
			worldIn.setBlockAndUpdate(pos, pushEntitiesUp(state, TrapdoorInit.dirt_trapdoor.get().defaultBlockState()
					.setValue(FACING, state.getValue(FACING)).setValue(OPEN, state.getValue(OPEN)).setValue(HALF, state.getValue(HALF))
					.setValue(POWERED, state.getValue(POWERED)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), worldIn, pos));
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.above());
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock;
	}

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		if (!state.getValue(OPEN)) {
			return state.getValue(HALF) == Half.TOP ? TOP_AABB : BOTTOM_AABB;
		} else {
			return switch (state.getValue(FACING)) {
				default -> NORTH_OPEN_AABB;
				case SOUTH -> SOUTH_OPEN_AABB;
				case WEST -> WEST_OPEN_AABB;
				case EAST -> EAST_OPEN_AABB;
			};
		}

	}

	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		return switch (type) {
			case LAND, AIR -> state.getValue(OPEN);
			case WATER -> state.getValue(WATERLOGGED);
		};
	}

}
