package com.ketheroth.vanillaextension.stairs;

import com.ketheroth.vanillaextension.init.VEBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.stream.IntStream;

import static net.minecraft.world.level.block.StairBlock.isStairs;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FarmStairs extends FarmBlock implements SimpleWaterloggedBlock {

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
	public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape AABB_SLAB_TOP = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape AABB_SLAB_BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape NWD_CORNER = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 8.0D);
	protected static final VoxelShape SWD_CORNER = Block.box(0.0D, 0.0D, 8.0D, 8.0D, 8.0D, 16.0D);
	protected static final VoxelShape NWU_CORNER = Block.box(0.0D, 8.0D, 0.0D, 8.0D, 15.0D, 8.0D);
	protected static final VoxelShape SWU_CORNER = Block.box(0.0D, 8.0D, 8.0D, 8.0D, 15.0D, 16.0D);
	protected static final VoxelShape NED_CORNER = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
	protected static final VoxelShape SED_CORNER = Block.box(8.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape NEU_CORNER = Block.box(8.0D, 8.0D, 0.0D, 16.0D, 15.0D, 8.0D);
	protected static final VoxelShape SEU_CORNER = Block.box(8.0D, 8.0D, 8.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape[] SLAB_TOP_SHAPES = makeShapes(AABB_SLAB_TOP, NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER);
	protected static final VoxelShape[] SLAB_BOTTOM_SHAPES = makeShapes(AABB_SLAB_BOTTOM, NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER);
	private static final int[] SHAPE_BY_STATE = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};

	public FarmStairs(BlockBehaviour.Properties builder) {
		super(builder);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(SHAPE, StairsShape.STRAIGHT).setValue(MOISTURE, 0).setValue(WATERLOGGED, false));
	}

	private static VoxelShape[] makeShapes(VoxelShape slabShape, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
		return IntStream.range(0, 16).mapToObj((p_199780_5_) -> combineShapes(p_199780_5_, slabShape, nwCorner, neCorner, swCorner, seCorner)).toArray(VoxelShape[]::new);
	}

	private static VoxelShape combineShapes(int bitfield, VoxelShape slabShape, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
		VoxelShape voxelshape = slabShape;
		if ((bitfield & 1) != 0) {
			voxelshape = Shapes.or(slabShape, nwCorner);
		}

		if ((bitfield & 2) != 0) {
			voxelshape = Shapes.or(voxelshape, neCorner);
		}

		if ((bitfield & 4) != 0) {
			voxelshape = Shapes.or(voxelshape, swCorner);
		}

		if ((bitfield & 8) != 0) {
			voxelshape = Shapes.or(voxelshape, seCorner);
		}

		return voxelshape;
	}

	/**
	 * Returns a stair shape property based on the surrounding stairs from the given blockstate and position
	 */
	private static StairsShape getShapeProperty(BlockState state, BlockGetter worldIn, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		BlockState blockstate = worldIn.getBlockState(pos.relative(direction));
		if (isStairs(blockstate) && state.getValue(HALF) == blockstate.getValue(HALF)) {
			Direction direction1 = blockstate.getValue(FACING);
			if (direction1.getAxis() != state.getValue(FACING).getAxis() && isDifferentStairs(state, worldIn, pos, direction1.getOpposite())) {
				if (direction1 == direction.getCounterClockWise()) {
					return StairsShape.OUTER_LEFT;
				}

				return StairsShape.OUTER_RIGHT;
			}
		}

		BlockState blockstate1 = worldIn.getBlockState(pos.relative(direction.getOpposite()));
		if (isStairs(blockstate1) && state.getValue(HALF) == blockstate1.getValue(HALF)) {
			Direction direction2 = blockstate1.getValue(FACING);
			if (direction2.getAxis() != state.getValue(FACING).getAxis() && isDifferentStairs(state, worldIn, pos, direction2)) {
				if (direction2 == direction.getCounterClockWise()) {
					return StairsShape.INNER_LEFT;
				}

				return StairsShape.INNER_RIGHT;
			}
		}

		return StairsShape.STRAIGHT;
	}

	private static boolean isDifferentStairs(BlockState state, BlockGetter worldIn, BlockPos pos, Direction face) {
		BlockState blockstate = worldIn.getBlockState(pos.relative(face));
		return !isStairs(blockstate) || blockstate.getValue(FACING) != state.getValue(FACING) || blockstate.getValue(HALF) != state.getValue(HALF);
	}

	public static void turnToDirtStairs(BlockState state, Level worldIn, BlockPos pos) {
		BlockState bs = VEBlocks.dirt_stairs.get().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(HALF, state.getValue(HALF)).setValue(SHAPE, state.getValue(SHAPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
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
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return (state.getValue(HALF) == Half.TOP ? SLAB_TOP_SHAPES : SLAB_BOTTOM_SHAPES)[SHAPE_BY_STATE[this.getShapeIndex(state)]];
	}

	private int getShapeIndex(BlockState state) {
		return state.getValue(SHAPE).ordinal() * 4 + state.getValue(FACING).get2DDataValue();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		BlockPos blockpos = context.getClickedPos();
		FluidState fluidstate = context.getLevel().getFluidState(blockpos);
		BlockState blockstate = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(HALF, direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? Half.BOTTOM : Half.TOP).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? VEBlocks.dirt_stairs.get().defaultBlockState() : blockstate.setValue(SHAPE, getShapeProperty(blockstate, context.getLevel(), blockpos));
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.UP && !stateIn.canSurvive(worldIn, currentPos)) {
			worldIn.scheduleTick(currentPos, this, 1);
		}

		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		return facing.getAxis().isHorizontal() ? stateIn.setValue(SHAPE, getShapeProperty(stateIn, worldIn, currentPos)) : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		Direction direction = state.getValue(FACING);
		StairsShape stairsshape = state.getValue(SHAPE);
		switch (mirrorIn) {
			case LEFT_RIGHT:
				if (direction.getAxis() == Direction.Axis.Z) {
					return switch (stairsshape) {
						case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
						case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
						case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
						case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
						default -> state.rotate(Rotation.CLOCKWISE_180);
					};
				}
				break;
			case FRONT_BACK:
				if (direction.getAxis() == Direction.Axis.X) {
					return switch (stairsshape) {
						case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
						case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
						case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
						case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
						case STRAIGHT -> state.rotate(Rotation.CLOCKWISE_180);
					};
				}
		}

		return super.mirror(state, mirrorIn);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, SHAPE, WATERLOGGED, MOISTURE);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
		PlantType type = plantable.getPlantType(world, pos.relative(facing));
		return PlantType.CROP.equals(type);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		int i = state.getValue(MOISTURE);
		if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.above())) {
			if (i > 0) {
				worldIn.setBlock(pos, state.setValue(MOISTURE, i - 1), 2);
			} else if (!hasCrops(worldIn, pos)) {
				turnToDirtStairs(state, worldIn, pos);
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
			turnToDirtStairs(worldIn.getBlockState(pos), worldIn, pos);
		}
		super.fallOn(worldIn, state, pos, entityIn, fallDistance);
	}

}
