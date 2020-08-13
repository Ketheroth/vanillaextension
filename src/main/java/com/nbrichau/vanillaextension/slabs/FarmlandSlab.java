package com.nbrichau.vanillaextension.slabs;

import com.nbrichau.vanillaextension.init.BlockInit;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import java.util.Random;


public class FarmlandSlab  extends FarmlandBlock implements IWaterLoggable {
	public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public FarmlandSlab(Properties builder) {
		super(builder);
		this.setDefaultState(this.getDefaultState().with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.FALSE).with(MOISTURE, 0));
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return state.get(TYPE) != SlabType.DOUBLE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED, MOISTURE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		SlabType slabtype = state.get(TYPE);
		switch(slabtype) {
			case DOUBLE:
				return VoxelShapes.fullCube();
			case TOP:
				return TOP_SHAPE;
			default:
				return BOTTOM_SHAPE;
		}
	}

	public BlockState getStateForPlacementSlab(BlockItemUseContext context) {
		BlockPos blockpos = context.getPos();
		BlockState blockstate = context.getWorld().getBlockState(blockpos);
		if (blockstate.isIn(this)) {
			return blockstate.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, Boolean.FALSE);
		} else {
			FluidState fluidstate = context.getWorld().getFluidState(blockpos);
			BlockState blockstate1 = this.getDefaultState().with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
			Direction direction = context.getFace();
			return direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double)blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.with(TYPE, SlabType.TOP);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return !this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) ? BlockInit.dirt_slab.getDefaultState() : this.getStateForPlacementSlab(context);
	}

	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		ItemStack itemstack = useContext.getItem();
		SlabType slabtype = state.get(TYPE);
		if (slabtype != SlabType.DOUBLE && itemstack.getItem() == this.asItem()) {
			if (useContext.replacingClickedOnBlock()) {
				boolean flag = useContext.getHitVec().y - (double)useContext.getPos().getY() > 0.5D;
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
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		return state.get(TYPE) != SlabType.DOUBLE ? IWaterLoggable.super.receiveFluid(worldIn, pos, state, fluidStateIn) : false;
	}

	@Override
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return state.get(TYPE) != SlabType.DOUBLE ? IWaterLoggable.super.canContainFluid(worldIn, pos, state, fluidIn) : false;
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific face passed in.
	 */
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		if (facing == Direction.UP && !stateIn.isValidPosition(worldIn, currentPos)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		switch(type) {
			case WATER:
				return worldIn.getFluidState(pos).isTagged(FluidTags.WATER);
			case LAND:
			case AIR:
			default:
				return false;
		}
	}

	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
		PlantType type = plantable.getPlantType(world, pos.offset(facing));
		return PlantType.CROP.equals(type);
	}

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		int i = state.get(MOISTURE);
		if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up())) {
			if (i > 0) {
				worldIn.setBlockState(pos, state.with(MOISTURE, Integer.valueOf(i - 1)), 2);
			} else if (!hasCrops(worldIn, pos)) {
				turnToDirtSlab(state, worldIn, pos);
			}
		} else if (i < 7) {
			worldIn.setBlockState(pos, state.with(MOISTURE, Integer.valueOf(7)), 2);
		}

	}

	public static void turnToDirtSlab(BlockState state, World worldIn, BlockPos pos) {
		BlockState bs = BlockInit.dirt_slab.getDefaultState().with(TYPE, state.get(TYPE)).with(WATERLOGGED, state.get(WATERLOGGED));
		worldIn.setBlockState(pos, bs);
	}

	private static boolean hasWater(IWorldReader worldIn, BlockPos pos) {
		for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
			if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER)) {
				return true;
			}
		}

		return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
	}

	private boolean hasCrops(IBlockReader worldIn, BlockPos pos) {
		BlockState state = worldIn.getBlockState(pos.up());
		return state.getBlock() instanceof net.minecraftforge.common.IPlantable && canSustainPlant(state, worldIn, pos, Direction.UP, (net.minecraftforge.common.IPlantable)state.getBlock());
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if (!worldIn.isRemote && net.minecraftforge.common.ForgeHooks.onFarmlandTrample(worldIn, pos, Blocks.DIRT.getDefaultState(), fallDistance, entityIn)) { // Forge: Move logic to Entity#canTrample
			turnToDirtSlab(worldIn.getBlockState(pos), worldIn, pos);
		}

		entityIn.onLivingFall(fallDistance, 1.0F);
	}
}
