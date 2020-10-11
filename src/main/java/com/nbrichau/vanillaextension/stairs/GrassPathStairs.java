package com.nbrichau.vanillaextension.stairs;

import com.nbrichau.vanillaextension.init.FenceInit;
import com.nbrichau.vanillaextension.init.StairsInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class GrassPathStairs extends StairsBlock {

	protected static final VoxelShape AABB_SLAB_TOP = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape AABB_SLAB_BOTTOM = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
	protected static final VoxelShape NWD_CORNER = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 7.0D, 8.0D);
	protected static final VoxelShape SWD_CORNER = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 8.0D, 7.0D, 16.0D);
	protected static final VoxelShape NWU_CORNER = Block.makeCuboidShape(0.0D, 7.0D, 0.0D, 8.0D, 15.0D, 8.0D);
	protected static final VoxelShape SWU_CORNER = Block.makeCuboidShape(0.0D, 7.0D, 8.0D, 8.0D, 15.0D, 16.0D);
	protected static final VoxelShape NED_CORNER = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 7.0D, 8.0D);
	protected static final VoxelShape SED_CORNER = Block.makeCuboidShape(8.0D, 0.0D, 8.0D, 16.0D, 7.0D, 16.0D);
	protected static final VoxelShape NEU_CORNER = Block.makeCuboidShape(8.0D, 7.0D, 0.0D, 16.0D, 15.0D, 8.0D);
	protected static final VoxelShape SEU_CORNER = Block.makeCuboidShape(8.0D, 7.0D, 8.0D, 16.0D, 15.0D, 16.0D);
	protected static final VoxelShape[] SLAB_TOP_SHAPES = makeShapes(AABB_SLAB_TOP, NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER);
	protected static final VoxelShape[] SLAB_BOTTOM_SHAPES = makeShapes(AABB_SLAB_BOTTOM, NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER);
	private static final int[] PALETTE_SHAPE_MAP = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};

	private static VoxelShape[] makeShapes(VoxelShape slabShape, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
		return IntStream.range(0, 16).mapToObj((bits) -> {
			return combineShapes(bits, slabShape, nwCorner, neCorner, swCorner, seCorner);
		}).toArray((id) -> {
			return new VoxelShape[id];
		});
	}

	private static VoxelShape combineShapes(int bitfield, VoxelShape slabShape, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
		VoxelShape voxelshape = slabShape;
		if ((bitfield & 1) != 0) {
			voxelshape = VoxelShapes.or(slabShape, nwCorner);
		}

		if ((bitfield & 2) != 0) {
			voxelshape = VoxelShapes.or(voxelshape, neCorner);
		}

		if ((bitfield & 4) != 0) {
			voxelshape = VoxelShapes.or(voxelshape, swCorner);
		}

		if ((bitfield & 8) != 0) {
			voxelshape = VoxelShapes.or(voxelshape, seCorner);
		}

		return voxelshape;
	}

	public GrassPathStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState bs = !this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) ? StairsInit.dirt_stairs.getDefaultState() : this.getDefaultState();

		Direction direction = context.getFace();
		BlockPos blockpos = context.getPos();
		IFluidState fluidstate = context.getWorld().getFluidState(blockpos);
		BlockState blockstate = bs.with(FACING, context.getPlacementHorizontalFacing())
				.with(HALF, direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double) blockpos.getY() > 0.5D)) ? Half.BOTTOM : Half.TOP)
				.with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
		return blockstate.with(SHAPE, getShapeProperty(blockstate, context.getWorld(), blockpos));
	}

	private static StairsShape getShapeProperty(BlockState state, IBlockReader worldIn, BlockPos pos) {
		Direction direction = state.get(FACING);
		BlockState blockstate = worldIn.getBlockState(pos.offset(direction));
		if (isBlockStairs(blockstate) && state.get(HALF) == blockstate.get(HALF)) {
			Direction direction1 = blockstate.get(FACING);
			if (direction1.getAxis() != state.get(FACING).getAxis() && isDifferentStairs(state, worldIn, pos, direction1.getOpposite())) {
				if (direction1 == direction.rotateYCCW()) {
					return StairsShape.OUTER_LEFT;
				}

				return StairsShape.OUTER_RIGHT;
			}
		}

		BlockState blockstate1 = worldIn.getBlockState(pos.offset(direction.getOpposite()));
		if (isBlockStairs(blockstate1) && state.get(HALF) == blockstate1.get(HALF)) {
			Direction direction2 = blockstate1.get(FACING);
			if (direction2.getAxis() != state.get(FACING).getAxis() && isDifferentStairs(state, worldIn, pos, direction2)) {
				if (direction2 == direction.rotateYCCW()) {
					return StairsShape.INNER_LEFT;
				}

				return StairsShape.INNER_RIGHT;
			}
		}

		return StairsShape.STRAIGHT;
	}

	private static boolean isDifferentStairs(BlockState state, IBlockReader worldIn, BlockPos pos, Direction face) {
		BlockState blockstate = worldIn.getBlockState(pos.offset(face));
		return !isBlockStairs(blockstate) || blockstate.get(FACING) != state.get(FACING) || blockstate.get(HALF) != state.get(HALF);
	}

	public static boolean isBlockStairs(BlockState state) {
		return state.getBlock() instanceof StairsBlock;
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.UP && !stateIn.isValidPosition(worldIn, currentPos)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}


	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (!this.isValidPosition(state, worldIn, pos)) {
			worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, FenceInit.dirt_fence.getDefaultState().with(FACING, state.get(FACING)).with(HALF, state.get(HALF))
							.with(WATERLOGGED, state.get(WATERLOGGED)).with(SHAPE, state.get(SHAPE)),
					worldIn, pos));
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.up());
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return (state.get(HALF) == Half.TOP ? SLAB_TOP_SHAPES : SLAB_BOTTOM_SHAPES)[PALETTE_SHAPE_MAP[this.getPaletteId(state)]];
	}

	private int getPaletteId(BlockState state) {
		return state.get(SHAPE).ordinal() * 4 + state.get(FACING).getHorizontalIndex();
	}

}