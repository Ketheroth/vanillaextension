package com.nbrichau.vanillaextension.walls;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Map;

import net.minecraft.block.AbstractBlock.Properties;

public class ConcretePowderWall extends ConcretePowderBlock implements IWaterLoggable {

	public static final BooleanProperty UP = BlockStateProperties.UP;
	public static final EnumProperty<WallHeight> WALL_HEIGHT_EAST = BlockStateProperties.EAST_WALL;
	public static final EnumProperty<WallHeight> WALL_HEIGHT_NORTH = BlockStateProperties.NORTH_WALL;
	public static final EnumProperty<WallHeight> WALL_HEIGHT_SOUTH = BlockStateProperties.SOUTH_WALL;
	public static final EnumProperty<WallHeight> WALL_HEIGHT_WEST = BlockStateProperties.WEST_WALL;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private final Map<BlockState, VoxelShape> stateToShapeMap;
	private final Map<BlockState, VoxelShape> stateToCollisionShapeMap;
	private static final VoxelShape CENTER_POLE_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape WALL_CONNECTION_NORTH_SIDE_SHAPE = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape WALL_CONNECTION_SOUTH_SIDE_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 16.0D);
	private static final VoxelShape WALL_CONNECTION_WEST_SIDE_SHAPE = Block.box(0.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape WALL_CONNECTION_EAST_SIDE_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);
	private final BlockState solidifiedState;

	public ConcretePowderWall(Block solidified, Properties properties) {
		super(solidified, properties);
		this.solidifiedState = solidified.defaultBlockState();
		this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.TRUE).setValue(WALL_HEIGHT_NORTH, WallHeight.NONE).setValue(WALL_HEIGHT_EAST, WallHeight.NONE).setValue(WALL_HEIGHT_SOUTH, WallHeight.NONE).setValue(WALL_HEIGHT_WEST, WallHeight.NONE).setValue(WATERLOGGED, Boolean.FALSE));
		this.stateToShapeMap = this.makeShapes(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
		this.stateToCollisionShapeMap = this.makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);

	}

	private static VoxelShape getHeightAlteredShape(VoxelShape baseShape, WallHeight height, VoxelShape lowShape, VoxelShape tallShape) {
		if (height == WallHeight.TALL) {
			return VoxelShapes.or(baseShape, tallShape);
		} else {
			return height == WallHeight.LOW ? VoxelShapes.or(baseShape, lowShape) : baseShape;
		}
	}

	private Map<BlockState, VoxelShape> makeShapes(float p_235624_1_, float p_235624_2_, float p_235624_3_, float p_235624_4_, float p_235624_5_, float p_235624_6_) {
		float f = 8.0F - p_235624_1_;
		float f1 = 8.0F + p_235624_1_;
		float f2 = 8.0F - p_235624_2_;
		float f3 = 8.0F + p_235624_2_;
		VoxelShape voxelshape = Block.box((double) f, 0.0D, (double) f, (double) f1, (double) p_235624_3_, (double) f1);
		VoxelShape voxelshape1 = Block.box((double) f2, (double) p_235624_4_, 0.0D, (double) f3, (double) p_235624_5_, (double) f3);
		VoxelShape voxelshape2 = Block.box((double) f2, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_5_, 16.0D);
		VoxelShape voxelshape3 = Block.box(0.0D, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_5_, (double) f3);
		VoxelShape voxelshape4 = Block.box((double) f2, (double) p_235624_4_, (double) f2, 16.0D, (double) p_235624_5_, (double) f3);
		VoxelShape voxelshape5 = Block.box((double) f2, (double) p_235624_4_, 0.0D, (double) f3, (double) p_235624_6_, (double) f3);
		VoxelShape voxelshape6 = Block.box((double) f2, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_6_, 16.0D);
		VoxelShape voxelshape7 = Block.box(0.0D, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_6_, (double) f3);
		VoxelShape voxelshape8 = Block.box((double) f2, (double) p_235624_4_, (double) f2, 16.0D, (double) p_235624_6_, (double) f3);
		ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

		for (Boolean obool : UP.getPossibleValues()) {
			for (WallHeight wallheight : WALL_HEIGHT_EAST.getPossibleValues()) {
				for (WallHeight wallheight1 : WALL_HEIGHT_NORTH.getPossibleValues()) {
					for (WallHeight wallheight2 : WALL_HEIGHT_WEST.getPossibleValues()) {
						for (WallHeight wallheight3 : WALL_HEIGHT_SOUTH.getPossibleValues()) {
							VoxelShape voxelshape9 = VoxelShapes.empty();
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight, voxelshape4, voxelshape8);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight2, voxelshape3, voxelshape7);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight1, voxelshape1, voxelshape5);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight3, voxelshape2, voxelshape6);
							if (obool) {
								voxelshape9 = VoxelShapes.or(voxelshape9, voxelshape);
							}

							BlockState blockstate = this.defaultBlockState().setValue(UP, obool).setValue(WALL_HEIGHT_EAST, wallheight).setValue(WALL_HEIGHT_WEST, wallheight2).setValue(WALL_HEIGHT_NORTH, wallheight1).setValue(WALL_HEIGHT_SOUTH, wallheight3);
							builder.put(blockstate.setValue(WATERLOGGED, Boolean.FALSE), voxelshape9);
							builder.put(blockstate.setValue(WATERLOGGED, Boolean.TRUE), voxelshape9);
						}
					}
				}
			}
		}

		return builder.build();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.stateToShapeMap.get(state);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.stateToCollisionShapeMap.get(state);
	}

	@Override
	public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	private boolean shouldConnect(BlockState state, boolean sideSolid, Direction direction) {
		Block block = state.getBlock();
		boolean flag = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction);
		return state.is(BlockTags.WALLS) || !isExceptionForConnection(block) && sideSolid || block instanceof PaneBlock || flag;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IWorldReader iworldreader = context.getLevel();
		IBlockReader iblockreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		BlockState blockstate = iblockreader.getBlockState(blockpos);

		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockPos blockpos5 = blockpos.above();
		BlockState blockstate1 = iworldreader.getBlockState(blockpos1);
		BlockState blockstate2 = iworldreader.getBlockState(blockpos2);
		BlockState blockstate3 = iworldreader.getBlockState(blockpos3);
		BlockState blockstate4 = iworldreader.getBlockState(blockpos4);
		BlockState blockstate5 = iworldreader.getBlockState(blockpos5);
		boolean flag = this.shouldConnect(blockstate1, blockstate1.isFaceSturdy(iworldreader, blockpos1, Direction.SOUTH), Direction.SOUTH);
		boolean flag1 = this.shouldConnect(blockstate2, blockstate2.isFaceSturdy(iworldreader, blockpos2, Direction.WEST), Direction.WEST);
		boolean flag2 = this.shouldConnect(blockstate3, blockstate3.isFaceSturdy(iworldreader, blockpos3, Direction.NORTH), Direction.NORTH);
		boolean flag3 = this.shouldConnect(blockstate4, blockstate4.isFaceSturdy(iworldreader, blockpos4, Direction.EAST), Direction.EAST);
		BlockState blockstate6 = shouldSolidify(iblockreader, blockpos, blockstate) ?
			this.solidifiedState.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER) :
			this.defaultBlockState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		return this.updateShape(iworldreader, blockstate6, blockpos5, blockstate5, flag, flag1, flag2, flag3);
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific face passed in.
	 */
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}
		BlockState blockstate = isTouchingLiquid(worldIn, currentPos) ? this.solidifiedState.setValue(UP, stateIn.getValue(UP)).setValue(WALL_HEIGHT_NORTH, stateIn.getValue(WALL_HEIGHT_NORTH)).setValue(WALL_HEIGHT_EAST, stateIn.getValue(WALL_HEIGHT_EAST)).setValue(WALL_HEIGHT_SOUTH, stateIn.getValue(WALL_HEIGHT_SOUTH)).setValue(WALL_HEIGHT_WEST, stateIn.getValue(WALL_HEIGHT_WEST)).setValue(WATERLOGGED, stateIn.getValue(WATERLOGGED)) : stateIn;
		if (facing == Direction.DOWN) {
			return super.updateShape(blockstate, facing, facingState, worldIn, currentPos, facingPos);
		} else {
			return facing == Direction.UP ? this.topUpdate(worldIn, blockstate, facingPos, facingState) : this.sideUpdate(worldIn, currentPos, blockstate, facingPos, facingState, facing);
		}


	}

	private static boolean hasHeightForProperty(BlockState state, Property<WallHeight> heightProperty) {
		return state.getValue(heightProperty) != WallHeight.NONE;
	}

	private static boolean compareShapes(VoxelShape shape1, VoxelShape shape2) {
		return !VoxelShapes.joinIsNotEmpty(shape2, shape1, IBooleanFunction.ONLY_FIRST);
	}

	private BlockState topUpdate(IWorldReader reader, BlockState state1, BlockPos pos, BlockState state2) {
		boolean flag = hasHeightForProperty(state1, WALL_HEIGHT_NORTH);
		boolean flag1 = hasHeightForProperty(state1, WALL_HEIGHT_EAST);
		boolean flag2 = hasHeightForProperty(state1, WALL_HEIGHT_SOUTH);
		boolean flag3 = hasHeightForProperty(state1, WALL_HEIGHT_WEST);
		return this.updateShape(reader, state1, pos, state2, flag, flag1, flag2, flag3);
	}

	private BlockState sideUpdate(IWorldReader reader, BlockPos p_235627_2_, BlockState p_235627_3_, BlockPos p_235627_4_, BlockState p_235627_5_, Direction directionIn) {
		Direction direction = directionIn.getOpposite();
		boolean flag = directionIn == Direction.NORTH ? this.shouldConnect(p_235627_5_, p_235627_5_.isFaceSturdy(reader, p_235627_4_, direction), direction) : hasHeightForProperty(p_235627_3_, WALL_HEIGHT_NORTH);
		boolean flag1 = directionIn == Direction.EAST ? this.shouldConnect(p_235627_5_, p_235627_5_.isFaceSturdy(reader, p_235627_4_, direction), direction) : hasHeightForProperty(p_235627_3_, WALL_HEIGHT_EAST);
		boolean flag2 = directionIn == Direction.SOUTH ? this.shouldConnect(p_235627_5_, p_235627_5_.isFaceSturdy(reader, p_235627_4_, direction), direction) : hasHeightForProperty(p_235627_3_, WALL_HEIGHT_SOUTH);
		boolean flag3 = directionIn == Direction.WEST ? this.shouldConnect(p_235627_5_, p_235627_5_.isFaceSturdy(reader, p_235627_4_, direction), direction) : hasHeightForProperty(p_235627_3_, WALL_HEIGHT_WEST);
		BlockPos blockpos = p_235627_2_.above();
		BlockState blockstate = reader.getBlockState(blockpos);
		return this.updateShape(reader, p_235627_3_, blockpos, blockstate, flag, flag1, flag2, flag3);
	}

	private BlockState updateShape(IWorldReader reader, BlockState state, BlockPos pos, BlockState collisionState, boolean connectedSouth, boolean connectedWest, boolean connectedNorth, boolean connectedEast) {
		VoxelShape voxelshape = collisionState.getBlockSupportShape(reader, pos).getFaceShape(Direction.DOWN);
		BlockState blockstate = this.updateSides(state, connectedSouth, connectedWest, connectedNorth, connectedEast, voxelshape);
		return blockstate.setValue(UP, this.shouldRaisePost(blockstate, collisionState, voxelshape));
	}

	private boolean shouldRaisePost(BlockState p_235628_1_, BlockState p_235628_2_, VoxelShape shape) {
		boolean flag = p_235628_2_.getBlock() instanceof WallBlock && p_235628_2_.getValue(UP);
		if (flag) {
			return true;
		} else {
			WallHeight wallheight = p_235628_1_.getValue(WALL_HEIGHT_NORTH);
			WallHeight wallheight1 = p_235628_1_.getValue(WALL_HEIGHT_SOUTH);
			WallHeight wallheight2 = p_235628_1_.getValue(WALL_HEIGHT_EAST);
			WallHeight wallheight3 = p_235628_1_.getValue(WALL_HEIGHT_WEST);
			boolean flag1 = wallheight1 == WallHeight.NONE;
			boolean flag2 = wallheight3 == WallHeight.NONE;
			boolean flag3 = wallheight2 == WallHeight.NONE;
			boolean flag4 = wallheight == WallHeight.NONE;
			boolean flag5 = flag4 && flag1 && flag2 && flag3 || flag4 != flag1 || flag2 != flag3;
			if (flag5) {
				return true;
			} else {
				boolean flag6 = wallheight == WallHeight.TALL && wallheight1 == WallHeight.TALL || wallheight2 == WallHeight.TALL && wallheight3 == WallHeight.TALL;
				if (flag6) {
					return false;
				} else {
					return p_235628_2_.getBlock().is(BlockTags.WALL_POST_OVERRIDE) || compareShapes(shape, CENTER_POLE_SHAPE);
				}
			}
		}
	}

	private BlockState updateSides(BlockState state, boolean connectedSouth, boolean connectedWest, boolean connectedNorth, boolean connectedEast, VoxelShape shape) {
		return state.setValue(WALL_HEIGHT_NORTH, this.makeWallState(connectedSouth, shape, WALL_CONNECTION_NORTH_SIDE_SHAPE)).setValue(WALL_HEIGHT_EAST, this.makeWallState(connectedWest, shape, WALL_CONNECTION_EAST_SIDE_SHAPE)).setValue(WALL_HEIGHT_SOUTH, this.makeWallState(connectedNorth, shape, WALL_CONNECTION_SOUTH_SIDE_SHAPE)).setValue(WALL_HEIGHT_WEST, this.makeWallState(connectedEast, shape, WALL_CONNECTION_WEST_SIDE_SHAPE));
	}

	private WallHeight makeWallState(boolean p_235633_1_, VoxelShape p_235633_2_, VoxelShape p_235633_3_) {
		if (p_235633_1_) {
			return compareShapes(p_235633_2_, p_235633_3_) ? WallHeight.TALL : WallHeight.LOW;
		} else {
			return WallHeight.NONE;
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return !state.getValue(WATERLOGGED);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(UP, WALL_HEIGHT_NORTH, WALL_HEIGHT_EAST, WALL_HEIGHT_WEST, WALL_HEIGHT_SOUTH, WATERLOGGED);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		switch (rot) {
			case CLOCKWISE_180:
				return state.setValue(WALL_HEIGHT_NORTH, state.getValue(WALL_HEIGHT_SOUTH)).setValue(WALL_HEIGHT_EAST, state.getValue(WALL_HEIGHT_WEST)).setValue(WALL_HEIGHT_SOUTH, state.getValue(WALL_HEIGHT_NORTH)).setValue(WALL_HEIGHT_WEST, state.getValue(WALL_HEIGHT_EAST));
			case COUNTERCLOCKWISE_90:
				return state.setValue(WALL_HEIGHT_NORTH, state.getValue(WALL_HEIGHT_EAST)).setValue(WALL_HEIGHT_EAST, state.getValue(WALL_HEIGHT_SOUTH)).setValue(WALL_HEIGHT_SOUTH, state.getValue(WALL_HEIGHT_WEST)).setValue(WALL_HEIGHT_WEST, state.getValue(WALL_HEIGHT_NORTH));
			case CLOCKWISE_90:
				return state.setValue(WALL_HEIGHT_NORTH, state.getValue(WALL_HEIGHT_WEST)).setValue(WALL_HEIGHT_EAST, state.getValue(WALL_HEIGHT_NORTH)).setValue(WALL_HEIGHT_SOUTH, state.getValue(WALL_HEIGHT_EAST)).setValue(WALL_HEIGHT_WEST, state.getValue(WALL_HEIGHT_SOUTH));
			default:
				return state;
		}
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		switch (mirrorIn) {
			case LEFT_RIGHT:
				return state.setValue(WALL_HEIGHT_NORTH, state.getValue(WALL_HEIGHT_SOUTH)).setValue(WALL_HEIGHT_SOUTH, state.getValue(WALL_HEIGHT_NORTH));
			case FRONT_BACK:
				return state.setValue(WALL_HEIGHT_EAST, state.getValue(WALL_HEIGHT_WEST)).setValue(WALL_HEIGHT_WEST, state.getValue(WALL_HEIGHT_EAST));
			default:
				return super.mirror(state, mirrorIn);
		}
	}

	@Override
	public void onLand(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		if (shouldSolidify(worldIn, pos, hitState)) {
			FluidState fluidstate = worldIn.getFluidState(pos);
			BlockPos blockpos1 = pos.north();
			BlockPos blockpos2 = pos.east();
			BlockPos blockpos3 = pos.south();
			BlockPos blockpos4 = pos.west();
			BlockPos blockpos5 = pos.above();
			BlockState blockstate = ((IWorldReader) worldIn).getBlockState(blockpos1);
			BlockState blockstate1 = ((IWorldReader) worldIn).getBlockState(blockpos2);
			BlockState blockstate2 = ((IWorldReader) worldIn).getBlockState(blockpos3);
			BlockState blockstate3 = ((IWorldReader) worldIn).getBlockState(blockpos4);
			BlockState blockstate4 = ((IWorldReader) worldIn).getBlockState(blockpos5);
			boolean flag = this.shouldConnect(blockstate, blockstate.isFaceSturdy(worldIn, blockpos1, Direction.SOUTH), Direction.SOUTH);
			boolean flag1 = this.shouldConnect(blockstate1, blockstate1.isFaceSturdy(worldIn, blockpos2, Direction.WEST), Direction.WEST);
			boolean flag2 = this.shouldConnect(blockstate2, blockstate2.isFaceSturdy(worldIn, blockpos3, Direction.NORTH), Direction.NORTH);
			boolean flag3 = this.shouldConnect(blockstate3, blockstate3.isFaceSturdy(worldIn, blockpos4, Direction.EAST), Direction.EAST);
			BlockState blockstate5 = this.solidifiedState.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
			BlockState bs = this.updateShape(worldIn, blockstate5, blockpos5, blockstate4, flag, flag1, flag2, flag3);
			worldIn.setBlockAndUpdate(pos, bs);
		}
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
}
