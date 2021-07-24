package com.nbrichau.vanillaextension.stairs;

import com.nbrichau.vanillaextension.init.StairsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class DirtPathStairs extends StairBlock {

	protected static final VoxelShape AABB_SLAB_TOP = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape AABB_SLAB_BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
	protected static final VoxelShape NWD_CORNER = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 7.0D, 8.0D);
	protected static final VoxelShape SWD_CORNER = Block.box(0.0D, 0.0D, 8.0D, 8.0D, 7.0D, 16.0D);
	protected static final VoxelShape NWU_CORNER = Block.box(0.0D, 7.0D, 0.0D, 8.0D, 15.0D, 8.0D);
	protected static final VoxelShape SWU_CORNER = Block.box(0.0D, 7.0D, 8.0D, 8.0D, 15.0D, 16.0D);
	protected static final VoxelShape NED_CORNER = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 7.0D, 8.0D);
	protected static final VoxelShape SED_CORNER = Block.box(8.0D, 0.0D, 8.0D, 16.0D, 7.0D, 16.0D);
	protected static final VoxelShape NEU_CORNER = Block.box(8.0D, 7.0D, 0.0D, 16.0D, 15.0D, 8.0D);
	protected static final VoxelShape SEU_CORNER = Block.box(8.0D, 7.0D, 8.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape[] SLAB_TOP_SHAPES = makeShapesCustom(AABB_SLAB_TOP, NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER);
	protected static final VoxelShape[] SLAB_BOTTOM_SHAPES = makeShapesCustom(AABB_SLAB_BOTTOM, NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER);
	private static final int[] PALETTE_SHAPE_MAP = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};

	public DirtPathStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}

	private static VoxelShape[] makeShapesCustom(VoxelShape slabShape, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
		return IntStream.range(0, 16).mapToObj((bits) -> {
			return combineShapes(bits, slabShape, nwCorner, neCorner, swCorner, seCorner);
		}).toArray(VoxelShape[]::new);
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

	private static StairsShape getShapeProperty(BlockState state, BlockGetter worldIn, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		BlockState blockstate = worldIn.getBlockState(pos.relative(direction));
		if (isBlockStairs(blockstate) && state.getValue(HALF) == blockstate.getValue(HALF)) {
			Direction direction1 = blockstate.getValue(FACING);
			if (direction1.getAxis() != state.getValue(FACING).getAxis() && isDifferentStairs(state, worldIn, pos, direction1.getOpposite())) {
				if (direction1 == direction.getCounterClockWise()) {
					return StairsShape.OUTER_LEFT;
				}

				return StairsShape.OUTER_RIGHT;
			}
		}

		BlockState blockstate1 = worldIn.getBlockState(pos.relative(direction.getOpposite()));
		if (isBlockStairs(blockstate1) && state.getValue(HALF) == blockstate1.getValue(HALF)) {
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
		return !isBlockStairs(blockstate) || blockstate.getValue(FACING) != state.getValue(FACING) || blockstate.getValue(HALF) != state.getValue(HALF);
	}

	public static boolean isBlockStairs(BlockState state) {
		return state.getBlock() instanceof StairBlock;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState bs = !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? StairsInit.dirt_stairs.get().defaultBlockState() : this.defaultBlockState();

		Direction direction = context.getClickedFace();
		BlockPos blockpos = context.getClickedPos();
		FluidState fluidstate = context.getLevel().getFluidState(blockpos);
		BlockState blockstate = bs.setValue(FACING, context.getHorizontalDirection())
				.setValue(HALF, direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? Half.BOTTOM : Half.TOP)
				.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		return blockstate.setValue(SHAPE, getShapeProperty(blockstate, context.getLevel(), blockpos));
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.UP && !stateIn.canSurvive(worldIn, currentPos)) {
			worldIn.getBlockTicks().scheduleTick(currentPos, this, 1);
		}

		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}


	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		if (!this.canSurvive(state, worldIn, pos)) {
			worldIn.setBlockAndUpdate(pos, pushEntitiesUp(state, StairsInit.dirt_stairs.get().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(HALF, state.getValue(HALF))
							.setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(SHAPE, state.getValue(SHAPE)),
					worldIn, pos));
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.above());
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return (state.getValue(HALF) == Half.TOP ? SLAB_TOP_SHAPES : SLAB_BOTTOM_SHAPES)[PALETTE_SHAPE_MAP[this.getPaletteId(state)]];
	}

	private int getPaletteId(BlockState state) {
		return state.getValue(SHAPE).ordinal() * 4 + state.getValue(FACING).get2DDataValue();
	}

}
