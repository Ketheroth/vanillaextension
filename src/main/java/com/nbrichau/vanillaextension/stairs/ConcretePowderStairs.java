package com.nbrichau.vanillaextension.stairs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.Half;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class ConcretePowderStairs extends FallingStairs implements IWaterLoggable {
	private final BlockState solidifiedState;

	public ConcretePowderStairs(Block solidified, Properties properties) {
		super(properties);
		this.solidifiedState = solidified.getDefaultState();
	}

	private static boolean shouldSolidify(IBlockReader reader, BlockPos pos, BlockState state) {
		return causesSolidify(state) || isTouchingLiquid(reader, pos);
	}

	private static boolean isTouchingLiquid(IBlockReader reader, BlockPos pos) {
		boolean flag = false;
		BlockPos.Mutable blockpos$mutable = pos.toMutable();
		for (Direction direction : Direction.values()) {
			BlockState blockstate = reader.getBlockState(blockpos$mutable);
			if (direction != Direction.DOWN || causesSolidify(blockstate)) {
				blockpos$mutable.setAndMove(pos, direction);
				blockstate = reader.getBlockState(blockpos$mutable);
				if (causesSolidify(blockstate) && !blockstate.isSolidSide(reader, pos, direction.getOpposite())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	private static boolean causesSolidify(BlockState state) {
		return state.getFluidState().isTagged(FluidTags.WATER);
	}

	public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		BlockState newState = shouldSolidify(worldIn, pos, hitState) ? this.solidifiedState.with(FACING, fallingState.get(FACING)).with(HALF, fallingState.get(HALF)) : fallingState;
		worldIn.setBlockState(pos, newState.with(SHAPE, getShapeProperty(fallingState, worldIn, pos)).with(WATERLOGGED, worldIn.getFluidState(pos).getFluid() == Fluids.WATER));
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace();
		BlockPos blockpos = context.getPos();
		FluidState fluidstate = context.getWorld().getFluidState(blockpos);
		IBlockReader iblockreader = context.getWorld();
		BlockState defaultState = shouldSolidify(iblockreader, blockpos, iblockreader.getBlockState(blockpos)) ? this.solidifiedState : this.getDefaultState();
		BlockState blockstate = defaultState.with(FACING, context.getPlacementHorizontalFacing()).with(HALF, direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double) blockpos.getY() > 0.5D)) ? Half.BOTTOM : Half.TOP).with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
		return blockstate.with(SHAPE, getShapeProperty(blockstate, context.getWorld(), blockpos));
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState blockstate = isTouchingLiquid(worldIn, currentPos) ? this.solidifiedState.with(FACING, stateIn.get(FACING)).with(HALF, stateIn.get(HALF)).with(SHAPE, stateIn.get(SHAPE)) : stateIn;
		return super.updatePostPlacement(blockstate, facing, facingState, worldIn, currentPos, facingPos);
	}
}
