package com.ketheroth.vanillaextension.stairs;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
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
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.stream.IntStream;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FallingStairs extends FallingBlock {

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
	public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape AABB_SLAB_TOP = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape AABB_SLAB_BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape NWD_CORNER = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 8.0D);
	protected static final VoxelShape SWD_CORNER = Block.box(0.0D, 0.0D, 8.0D, 8.0D, 8.0D, 16.0D);
	protected static final VoxelShape NWU_CORNER = Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D);
	protected static final VoxelShape SWU_CORNER = Block.box(0.0D, 8.0D, 8.0D, 8.0D, 16.0D, 16.0D);
	protected static final VoxelShape NED_CORNER = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
	protected static final VoxelShape SED_CORNER = Block.box(8.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape NEU_CORNER = Block.box(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
	protected static final VoxelShape SEU_CORNER = Block.box(8.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape[] SLAB_TOP_SHAPES = makeShapes(AABB_SLAB_TOP, NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER);
	protected static final VoxelShape[] SLAB_BOTTOM_SHAPES = makeShapes(AABB_SLAB_BOTTOM, NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER);
	private static final int[] PALETTE_SHAPE_MAP = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};
	private final Block modelBlock;
	private final BlockState modelState;

	public FallingStairs(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, Boolean.FALSE));
		this.modelBlock = Blocks.AIR; // These are unused, fields are redirected
		this.modelState = Blocks.AIR.defaultBlockState();
	}

	private static VoxelShape[] makeShapes(VoxelShape slabShape, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
		return IntStream.range(0, 16).mapToObj((bits) -> combineShapes(bits, slabShape, nwCorner, neCorner, swCorner, seCorner)).toArray(VoxelShape[]::new);
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

	protected static StairsShape getShapeProperty(BlockState state, BlockGetter worldIn, BlockPos pos) {
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

	protected static boolean isDifferentStairs(BlockState state, BlockGetter worldIn, BlockPos pos, Direction face) {
		BlockState blockstate = worldIn.getBlockState(pos.relative(face));
		return !isBlockStairs(blockstate) || blockstate.getValue(FACING) != state.getValue(FACING) || blockstate.getValue(HALF) != state.getValue(HALF);
	}

	public static boolean isBlockStairs(BlockState state) {
		return state.getBlock() instanceof StairBlock || state.getBlock() instanceof FallingStairs;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return (state.getValue(HALF) == Half.TOP ? SLAB_TOP_SHAPES : SLAB_BOTTOM_SHAPES)[PALETTE_SHAPE_MAP[this.getPaletteId(state)]];
	}

	private int getPaletteId(BlockState state) {
		return state.getValue(SHAPE).ordinal() * 4 + state.getValue(FACING).get2DDataValue();
	}

	@Override
	public void attack(BlockState state, Level worldIn, BlockPos pos, Player player) {
		this.modelState.attack(worldIn, pos, player);
	}

	@Override
	public void destroy(LevelAccessor worldIn, BlockPos pos, BlockState state) {
		this.modelBlock.destroy(worldIn, pos, state);
	}

	@Override
	public float getExplosionResistance() {
		return this.modelBlock.getExplosionResistance();
	}

	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		worldIn.scheduleTick(pos, this, this.getDelayAfterPlace());
		if (!state.is(state.getBlock())) {
			this.modelState.neighborChanged(worldIn, pos, Blocks.AIR, pos, false);
			this.modelBlock.onPlace(this.modelState, worldIn, pos, oldState, false);
		}
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			this.modelState.onRemove(worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
		this.modelBlock.stepOn(worldIn, pos, state, entityIn);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return this.modelBlock.isRandomlyTicking(state);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		this.modelBlock.randomTick(state, worldIn, pos, random);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		return this.modelState.use(worldIn, player, handIn, hit);
	}

	@Override
	public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn) {
		this.modelBlock.wasExploded(worldIn, pos, explosionIn);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		BlockPos blockpos = context.getClickedPos();
		FluidState fluidstate = context.getLevel().getFluidState(blockpos);
		BlockState blockstate = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(HALF, direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? Half.BOTTOM : Half.TOP).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		return blockstate.setValue(SHAPE, getShapeProperty(blockstate, context.getLevel(), blockpos));
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}
		worldIn.scheduleTick(currentPos, this, this.getDelayAfterPlace());
		return facing.getAxis().isHorizontal() ? stateIn.setValue(SHAPE, getShapeProperty(stateIn, worldIn, currentPos)) : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public void onLand(Level worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		worldIn.setBlockAndUpdate(pos, fallingState.setValue(SHAPE, getShapeProperty(fallingState, worldIn, pos)).setValue(WATERLOGGED, worldIn.getFluidState(pos).getType() == Fluids.WATER));
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
		builder.add(FACING, HALF, SHAPE, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public int getDustColor(BlockState state, BlockGetter reader, BlockPos pos) {
		return -16777216;
	}

}
