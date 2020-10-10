package com.nbrichau.vanillaextension.slabs;

import com.nbrichau.vanillaextension.init.SlabInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GrassPathSlab extends SlabBlock {

	protected static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public GrassPathSlab(Properties builder) {
		super(builder);
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos blockpos = context.getPos();
		BlockState blockstate = context.getWorld().getBlockState(blockpos);

		if (blockstate.getBlock() == this) {
			return this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) ?
					blockstate.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, Boolean.FALSE) :
					SlabInit.dirt_slab.getDefaultState().with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, Boolean.FALSE);
		} else {
			IFluidState fluidstate = context.getWorld().getFluidState(blockpos);
			BlockState bs = this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) ?
					this.getDefaultState() : SlabInit.dirt_slab.getDefaultState();
			BlockState blockstate1 = bs.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
			Direction direction = context.getFace();
			return direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double) blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.with(TYPE, SlabType.TOP);
		}
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
			worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, SlabInit.dirt_slab.getDefaultState()
					.with(TYPE, state.get(TYPE)).with(WATERLOGGED, state.get(WATERLOGGED)), worldIn, pos));
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.up());
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		SlabType slabtype = state.get(TYPE);
		switch (slabtype) {
			case DOUBLE:
				return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
			case TOP:
				return TOP_SHAPE;
			default:
				return BOTTOM_SHAPE;
		}
	}

}