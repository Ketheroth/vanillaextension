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

import net.minecraft.block.AbstractBlock.Properties;

public class ConcretePowderStairs extends FallingStairs implements IWaterLoggable {
	private final BlockState solidifiedState;

	public ConcretePowderStairs(Block solidified, Properties properties) {
		super(properties);
		this.solidifiedState = solidified.defaultBlockState();
	}

	private static boolean shouldSolidify(IBlockReader reader, BlockPos pos, BlockState state) {
		return causesSolidify(state) || isTouchingLiquid(reader, pos);
	}

	private static boolean isTouchingLiquid(IBlockReader reader, BlockPos pos) {
		boolean flag = false;
		BlockPos.Mutable blockpos$mutable = pos.mutable();
		for (Direction direction : Direction.values()) {
			BlockState blockstate = reader.getBlockState(blockpos$mutable);
			if (direction != Direction.DOWN || causesSolidify(blockstate)) {
				blockpos$mutable.setWithOffset(pos, direction);
				blockstate = reader.getBlockState(blockpos$mutable);
				if (causesSolidify(blockstate) && !blockstate.isFaceSturdy(reader, pos, direction.getOpposite())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	private static boolean causesSolidify(BlockState state) {
		return state.getFluidState().is(FluidTags.WATER);
	}

	public void onLand(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		BlockState newState = shouldSolidify(worldIn, pos, hitState) ? this.solidifiedState.setValue(FACING, fallingState.getValue(FACING)).setValue(HALF, fallingState.getValue(HALF)) : fallingState;
		worldIn.setBlockAndUpdate(pos, newState.setValue(SHAPE, getShapeProperty(fallingState, worldIn, pos)).setValue(WATERLOGGED, worldIn.getFluidState(pos).getType() == Fluids.WATER));
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getClickedFace();
		BlockPos blockpos = context.getClickedPos();
		FluidState fluidstate = context.getLevel().getFluidState(blockpos);
		IBlockReader iblockreader = context.getLevel();
		BlockState defaultState = shouldSolidify(iblockreader, blockpos, iblockreader.getBlockState(blockpos)) ? this.solidifiedState : this.defaultBlockState();
		BlockState blockstate = defaultState.setValue(FACING, context.getHorizontalDirection()).setValue(HALF, direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? Half.BOTTOM : Half.TOP).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		return blockstate.setValue(SHAPE, getShapeProperty(blockstate, context.getLevel(), blockpos));
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState blockstate = isTouchingLiquid(worldIn, currentPos) ? this.solidifiedState.setValue(FACING, stateIn.getValue(FACING)).setValue(HALF, stateIn.getValue(HALF)).setValue(SHAPE, stateIn.getValue(SHAPE)) : stateIn;
		return super.updateShape(blockstate, facing, facingState, worldIn, currentPos, facingPos);
	}
}
