package com.ketheroth.vanillaextension.slabs;

import com.ketheroth.vanillaextension.init.VEBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FarmSlab extends FarmBlock implements SimpleWaterloggedBlock {

	public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape BOTTOM_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public FarmSlab(Properties builder) {
		super(builder);
		this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, Boolean.FALSE).setValue(MOISTURE, 0));
	}

	public static void turnToDirtSlab(BlockState state, Level worldIn, BlockPos pos) {
		BlockState bs = VEBlocks.dirt_slab.get().defaultBlockState().setValue(TYPE, state.getValue(TYPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
		worldIn.setBlockAndUpdate(pos, bs);
	}

	private static boolean hasWater(LevelReader worldIn, BlockPos pos) {
		for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
			if (worldIn.getFluidState(blockpos).is(FluidTags.WATER)) {
				return true;
			}
		}

		return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return state.getValue(TYPE) != SlabType.DOUBLE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED, MOISTURE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		SlabType slabtype = state.getValue(TYPE);
		return switch (slabtype) {
			case DOUBLE -> Shapes.block();
			case TOP -> TOP_SHAPE;
			default -> BOTTOM_SHAPE;
		};
	}

	public BlockState getStateForPlacementSlab(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = context.getLevel().getBlockState(blockpos);
		if (blockstate.is(this)) {
			return blockstate.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, Boolean.FALSE);
		} else {
			FluidState fluidstate = context.getLevel().getFluidState(blockpos);
			BlockState blockstate1 = this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
			Direction direction = context.getClickedFace();
			return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.setValue(TYPE, SlabType.TOP);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? VEBlocks.dirt_slab.get().defaultBlockState() : this.getStateForPlacementSlab(context);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		ItemStack itemstack = useContext.getItemInHand();
		SlabType slabtype = state.getValue(TYPE);
		if (slabtype != SlabType.DOUBLE && itemstack.getItem() == this.asItem()) {
			if (useContext.replacingClickedOnBlock()) {
				boolean flag = useContext.getClickLocation().y - (double) useContext.getClickedPos().getY() > 0.5D;
				Direction direction = useContext.getClickedFace();
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
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		return state.getValue(TYPE) != SlabType.DOUBLE && SimpleWaterloggedBlock.super.placeLiquid(worldIn, pos, state, fluidStateIn);
	}

	@Override
	public boolean canPlaceLiquid(BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return state.getValue(TYPE) != SlabType.DOUBLE && SimpleWaterloggedBlock.super.canPlaceLiquid(worldIn, pos, state, fluidIn);
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific face passed in.
	 */
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		if (facing == Direction.UP && !stateIn.canSurvive(worldIn, currentPos)) {
			worldIn.scheduleTick(currentPos, this, 1);
		}

		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		return type == PathComputationType.WATER && worldIn.getFluidState(pos).is(FluidTags.WATER);
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
		PlantType type = plantable.getPlantType(world, pos.relative(facing));
		return PlantType.CROP.equals(type);
	}

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		int i = state.getValue(MOISTURE);
		if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.above())) {
			if (i > 0) {
				worldIn.setBlock(pos, state.setValue(MOISTURE, i - 1), 2);
			} else if (!hasCrops(worldIn, pos)) {
				turnToDirtSlab(state, worldIn, pos);
			}
		} else if (i < 7) {
			worldIn.setBlock(pos, state.setValue(MOISTURE, 7), 2);
		}

	}

	private boolean hasCrops(BlockGetter worldIn, BlockPos pos) {
		BlockState state = worldIn.getBlockState(pos.above());
		return state.getBlock() instanceof net.minecraftforge.common.IPlantable && canSustainPlant(state, worldIn, pos, Direction.UP, (net.minecraftforge.common.IPlantable) state.getBlock());
	}

	@Override
	public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
		if (!worldIn.isClientSide && net.minecraftforge.common.ForgeHooks.onFarmlandTrample(worldIn, pos, Blocks.DIRT.defaultBlockState(), fallDistance, entityIn)) { // Forge: Move logic to Entity#canTrample
			turnToDirtSlab(worldIn.getBlockState(pos), worldIn, pos);
		}
		super.fallOn(worldIn, state, pos, entityIn, fallDistance);
	}

}
