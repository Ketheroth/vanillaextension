package com.nbrichau.vanillaextension.slabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class ConcretePowderSlab extends ConcretePowderBlock implements IWaterLoggable {

	public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private final BlockState solidifiedState;

	public ConcretePowderSlab(Block solidified, Properties properties) {
		super(solidified, properties);
		this.setDefaultState(this.getDefaultState().with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.FALSE));
		this.solidifiedState = solidified.getDefaultState();
	}

	@Override
	public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState) {
		if (shouldSolidify(worldIn, pos, hitState)) {
			worldIn.setBlockState(pos, this.solidifiedState.with(TYPE, fallingState.get(TYPE)).with(WATERLOGGED, fallingState.get(WATERLOGGED)), 3);
		}
	}

	private static boolean shouldSolidify(IBlockReader reader, BlockPos pos, BlockState state) {
		return causesSolidify(state) || isTouchingLiquid(reader, pos);
	}

	private static boolean isTouchingLiquid(IBlockReader reader, BlockPos pos) {
		boolean flag = false;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(pos);
		for (Direction direction : Direction.values()) {
			BlockState blockstate = reader.getBlockState(blockpos$mutable);
			if (direction != Direction.DOWN || causesSolidify(blockstate)) {
				blockpos$mutable.setPos(pos).move(direction);
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

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return isTouchingLiquid(worldIn, currentPos) ?
				this.solidifiedState.with(TYPE, stateIn.get(TYPE)).with(WATERLOGGED, Boolean.FALSE) :
				super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		SlabType slabtype = state.get(TYPE);
		switch (slabtype) {
			case DOUBLE:
				return VoxelShapes.fullCube();
			case TOP:
				return TOP_SHAPE;
			default:
				return BOTTOM_SHAPE;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IBlockReader iblockreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState blockstate = iblockreader.getBlockState(blockpos);

		if (blockstate.getBlock() == this) {
			return blockstate.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, Boolean.FALSE);
		} else {
			IFluidState fluidstate = context.getWorld().getFluidState(blockpos);
			BlockState blockstate1 = this.getDefaultState().with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER));
			BlockState blockstate2 = solidifiedState.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER));
			Direction direction = context.getFace();

			if (shouldSolidify(iblockreader, blockpos, blockstate)) {
				return direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double) blockpos.getY() > 0.5D)) ? blockstate2 : blockstate2.with(TYPE, SlabType.TOP);
			} else {
				return direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double) blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.with(TYPE, SlabType.TOP);
			}
		}
	}

	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		ItemStack itemstack = useContext.getItem();
		SlabType slabtype = state.get(TYPE);
		if (slabtype != SlabType.DOUBLE && itemstack.getItem() == this.asItem()) {
			if (useContext.replacingClickedOnBlock()) {
				boolean flag = useContext.getHitVec().y - (double) useContext.getPos().getY() > 0.5D;
				Direction direction = useContext.getFace();
				if (slabtype == SlabType.BOTTOM) {
					return direction == Direction.UP || flag && direction.getAxis().isHorizontal();
				} else {
					return direction == Direction.DOWN || !flag && direction.getAxis().isHorizontal();
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn) {
		return state.get(TYPE) != SlabType.DOUBLE && IWaterLoggable.super.receiveFluid(worldIn, pos, state, fluidStateIn);
	}

	@Override
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return state.get(TYPE) != SlabType.DOUBLE && IWaterLoggable.super.canContainFluid(worldIn, pos, state, fluidIn);
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		switch (type) {
			case WATER:
				return worldIn.getFluidState(pos).isTagged(FluidTags.WATER);
			case LAND:
			case AIR:
			default:
				return false;
		}
	}
}