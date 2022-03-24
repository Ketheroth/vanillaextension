package com.ketheroth.vanillaextension.walls;

import com.google.common.collect.ImmutableMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

import static com.ketheroth.vanillaextension.VanillaExtension.MODID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConcretePowderWall extends FallingBlock implements SimpleWaterloggedBlock {

	public static final BooleanProperty UP = BlockStateProperties.UP;
	public static final EnumProperty<WallSide> WALL_HEIGHT_EAST = BlockStateProperties.EAST_WALL;
	public static final EnumProperty<WallSide> WALL_HEIGHT_NORTH = BlockStateProperties.NORTH_WALL;
	public static final EnumProperty<WallSide> WALL_HEIGHT_SOUTH = BlockStateProperties.SOUTH_WALL;
	public static final EnumProperty<WallSide> WALL_HEIGHT_WEST = BlockStateProperties.WEST_WALL;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape CENTER_POLE_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape WALL_CONNECTION_NORTH_SIDE_SHAPE = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape WALL_CONNECTION_SOUTH_SIDE_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 16.0D);
	private static final VoxelShape WALL_CONNECTION_WEST_SIDE_SHAPE = Block.box(0.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape WALL_CONNECTION_EAST_SIDE_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);
	private final Map<BlockState, VoxelShape> stateToShapeMap;
	private final Map<BlockState, VoxelShape> stateToCollisionShapeMap;

	public ConcretePowderWall(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.TRUE).setValue(WALL_HEIGHT_NORTH, WallSide.NONE).setValue(WALL_HEIGHT_EAST, WallSide.NONE).setValue(WALL_HEIGHT_SOUTH, WallSide.NONE).setValue(WALL_HEIGHT_WEST, WallSide.NONE).setValue(WATERLOGGED, Boolean.FALSE));
		this.stateToShapeMap = this.makeShapes(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
		this.stateToCollisionShapeMap = this.makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);

	}

	private static VoxelShape getHeightAlteredShape(VoxelShape baseShape, WallSide height, VoxelShape lowShape, VoxelShape tallShape) {
		if (height == WallSide.TALL) {
			return Shapes.or(baseShape, tallShape);
		} else {
			return height == WallSide.LOW ? Shapes.or(baseShape, lowShape) : baseShape;
		}
	}

	private static boolean hasHeightForProperty(BlockState state, Property<WallSide> heightProperty) {
		return state.getValue(heightProperty) != WallSide.NONE;
	}

	private static boolean compareShapes(VoxelShape shape1, VoxelShape shape2) {
		return !Shapes.joinIsNotEmpty(shape2, shape1, BooleanOp.ONLY_FIRST);
	}

	private static boolean shouldSolidify(BlockGetter reader, BlockPos pos, BlockState state) {
		return causesSolidify(state) || isTouchingLiquid(reader, pos);
	}

	private static boolean isTouchingLiquid(BlockGetter reader, BlockPos pos) {
		boolean flag = false;
		BlockPos.MutableBlockPos blockpos$mutable = pos.mutable();
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

	private BlockState getSolidifiedState() {
		String[] part = this.getRegistryName().getPath().split("_powder");
		String name = part[0] + part[1];
		return ForgeRegistries.BLOCKS.getValue(ResourceLocation.of(MODID + ":" + name, ':')).defaultBlockState();
	}

	private Map<BlockState, VoxelShape> makeShapes(float p_235624_1_, float p_235624_2_, float p_235624_3_, float p_235624_4_, float p_235624_5_, float p_235624_6_) {
		float f = 8.0F - p_235624_1_;
		float f1 = 8.0F + p_235624_1_;
		float f2 = 8.0F - p_235624_2_;
		float f3 = 8.0F + p_235624_2_;
		VoxelShape voxelshape = Block.box(f, 0.0D, f, f1, p_235624_3_, f1);
		VoxelShape voxelshape1 = Block.box(f2, p_235624_4_, 0.0D, f3, p_235624_5_, f3);
		VoxelShape voxelshape2 = Block.box(f2, p_235624_4_, f2, f3, p_235624_5_, 16.0D);
		VoxelShape voxelshape3 = Block.box(0.0D, p_235624_4_, f2, f3, p_235624_5_, f3);
		VoxelShape voxelshape4 = Block.box(f2, p_235624_4_, f2, 16.0D, p_235624_5_, f3);
		VoxelShape voxelshape5 = Block.box(f2, p_235624_4_, 0.0D, f3, p_235624_6_, f3);
		VoxelShape voxelshape6 = Block.box(f2, p_235624_4_, f2, f3, p_235624_6_, 16.0D);
		VoxelShape voxelshape7 = Block.box(0.0D, p_235624_4_, f2, f3, p_235624_6_, f3);
		VoxelShape voxelshape8 = Block.box(f2, p_235624_4_, f2, 16.0D, p_235624_6_, f3);
		ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

		for (Boolean obool : UP.getPossibleValues()) {
			for (WallSide wallheight : WALL_HEIGHT_EAST.getPossibleValues()) {
				for (WallSide wallheight1 : WALL_HEIGHT_NORTH.getPossibleValues()) {
					for (WallSide wallheight2 : WALL_HEIGHT_WEST.getPossibleValues()) {
						for (WallSide wallheight3 : WALL_HEIGHT_SOUTH.getPossibleValues()) {
							VoxelShape voxelshape9 = Shapes.empty();
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight, voxelshape4, voxelshape8);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight2, voxelshape3, voxelshape7);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight1, voxelshape1, voxelshape5);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight3, voxelshape2, voxelshape6);
							if (obool) {
								voxelshape9 = Shapes.or(voxelshape9, voxelshape);
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
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return this.stateToShapeMap.get(state);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return this.stateToCollisionShapeMap.get(state);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		return false;
	}

	private boolean shouldConnect(BlockState state, boolean sideSolid, Direction direction) {
		Block block = state.getBlock();
		boolean flag = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction);
		return state.is(BlockTags.WALLS) || !isExceptionForConnection(state) && sideSolid || block instanceof IronBarsBlock || flag;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		LevelReader iworldreader = context.getLevel();
		BlockGetter iblockreader = context.getLevel();
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
				this.getSolidifiedState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER) :
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
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}
		BlockState blockstate = isTouchingLiquid(worldIn, currentPos) ? this.getSolidifiedState().setValue(UP, stateIn.getValue(UP)).setValue(WALL_HEIGHT_NORTH, stateIn.getValue(WALL_HEIGHT_NORTH)).setValue(WALL_HEIGHT_EAST, stateIn.getValue(WALL_HEIGHT_EAST)).setValue(WALL_HEIGHT_SOUTH, stateIn.getValue(WALL_HEIGHT_SOUTH)).setValue(WALL_HEIGHT_WEST, stateIn.getValue(WALL_HEIGHT_WEST)).setValue(WATERLOGGED, stateIn.getValue(WATERLOGGED)) : stateIn;
		if (facing == Direction.DOWN) {
			return super.updateShape(blockstate, facing, facingState, worldIn, currentPos, facingPos);
		} else {
			return facing == Direction.UP ? this.topUpdate(worldIn, blockstate, facingPos, facingState) : this.sideUpdate(worldIn, currentPos, blockstate, facingPos, facingState, facing);
		}

	}

	private BlockState topUpdate(LevelReader reader, BlockState state1, BlockPos pos, BlockState state2) {
		boolean flag = hasHeightForProperty(state1, WALL_HEIGHT_NORTH);
		boolean flag1 = hasHeightForProperty(state1, WALL_HEIGHT_EAST);
		boolean flag2 = hasHeightForProperty(state1, WALL_HEIGHT_SOUTH);
		boolean flag3 = hasHeightForProperty(state1, WALL_HEIGHT_WEST);
		return this.updateShape(reader, state1, pos, state2, flag, flag1, flag2, flag3);
	}

	private BlockState sideUpdate(LevelReader reader, BlockPos p_235627_2_, BlockState p_235627_3_, BlockPos p_235627_4_, BlockState p_235627_5_, Direction directionIn) {
		Direction direction = directionIn.getOpposite();
		boolean flag = directionIn == Direction.NORTH ? this.shouldConnect(p_235627_5_, p_235627_5_.isFaceSturdy(reader, p_235627_4_, direction), direction) : hasHeightForProperty(p_235627_3_, WALL_HEIGHT_NORTH);
		boolean flag1 = directionIn == Direction.EAST ? this.shouldConnect(p_235627_5_, p_235627_5_.isFaceSturdy(reader, p_235627_4_, direction), direction) : hasHeightForProperty(p_235627_3_, WALL_HEIGHT_EAST);
		boolean flag2 = directionIn == Direction.SOUTH ? this.shouldConnect(p_235627_5_, p_235627_5_.isFaceSturdy(reader, p_235627_4_, direction), direction) : hasHeightForProperty(p_235627_3_, WALL_HEIGHT_SOUTH);
		boolean flag3 = directionIn == Direction.WEST ? this.shouldConnect(p_235627_5_, p_235627_5_.isFaceSturdy(reader, p_235627_4_, direction), direction) : hasHeightForProperty(p_235627_3_, WALL_HEIGHT_WEST);
		BlockPos blockpos = p_235627_2_.above();
		BlockState blockstate = reader.getBlockState(blockpos);
		return this.updateShape(reader, p_235627_3_, blockpos, blockstate, flag, flag1, flag2, flag3);
	}

	private BlockState updateShape(LevelReader reader, BlockState state, BlockPos pos, BlockState collisionState, boolean connectedSouth, boolean connectedWest, boolean connectedNorth, boolean connectedEast) {
		VoxelShape voxelshape = collisionState.getBlockSupportShape(reader, pos).getFaceShape(Direction.DOWN);
		BlockState blockstate = this.updateSides(state, connectedSouth, connectedWest, connectedNorth, connectedEast, voxelshape);
		return blockstate.setValue(UP, this.shouldRaisePost(blockstate, collisionState, voxelshape));
	}

	private boolean shouldRaisePost(BlockState state, BlockState state1, VoxelShape shape) {
		boolean flag = state1.getBlock() instanceof WallBlock && state1.getValue(UP);
		if (flag) {
			return true;
		} else {
			WallSide sideNorth = state.getValue(WALL_HEIGHT_NORTH);
			WallSide sideSouth = state.getValue(WALL_HEIGHT_SOUTH);
			WallSide sideEast = state.getValue(WALL_HEIGHT_EAST);
			WallSide sideWest = state.getValue(WALL_HEIGHT_WEST);
			boolean noSouthSide = sideSouth == WallSide.NONE;
			boolean noWestSide = sideWest == WallSide.NONE;
			boolean noEastSide = sideEast == WallSide.NONE;
			boolean noNorthSide = sideNorth == WallSide.NONE;
			boolean flag5 = noNorthSide && noSouthSide && noWestSide && noEastSide || noNorthSide != noSouthSide || noWestSide != noEastSide;
			if (flag5) {
				return true;
			} else {
				boolean flag6 = sideNorth == WallSide.TALL && sideSouth == WallSide.TALL || sideEast == WallSide.TALL && sideWest == WallSide.TALL;
				if (flag6) {
					return false;
				} else {
					return state1.is(BlockTags.WALL_POST_OVERRIDE) || compareShapes(shape, CENTER_POLE_SHAPE);
				}
			}
		}
	}

	private BlockState updateSides(BlockState state, boolean connectedSouth, boolean connectedWest, boolean connectedNorth, boolean connectedEast, VoxelShape shape) {
		return state.setValue(WALL_HEIGHT_NORTH, this.makeWallState(connectedSouth, shape, WALL_CONNECTION_NORTH_SIDE_SHAPE)).setValue(WALL_HEIGHT_EAST, this.makeWallState(connectedWest, shape, WALL_CONNECTION_EAST_SIDE_SHAPE)).setValue(WALL_HEIGHT_SOUTH, this.makeWallState(connectedNorth, shape, WALL_CONNECTION_SOUTH_SIDE_SHAPE)).setValue(WALL_HEIGHT_WEST, this.makeWallState(connectedEast, shape, WALL_CONNECTION_WEST_SIDE_SHAPE));
	}

	private WallSide makeWallState(boolean p_235633_1_, VoxelShape p_235633_2_, VoxelShape p_235633_3_) {
		if (p_235633_1_) {
			return compareShapes(p_235633_2_, p_235633_3_) ? WallSide.TALL : WallSide.LOW;
		} else {
			return WallSide.NONE;
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return !state.getValue(WATERLOGGED);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(UP, WALL_HEIGHT_NORTH, WALL_HEIGHT_EAST, WALL_HEIGHT_WEST, WALL_HEIGHT_SOUTH, WATERLOGGED);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return switch (rot) {
			case CLOCKWISE_180 -> state.setValue(WALL_HEIGHT_NORTH, state.getValue(WALL_HEIGHT_SOUTH)).setValue(WALL_HEIGHT_EAST, state.getValue(WALL_HEIGHT_WEST)).setValue(WALL_HEIGHT_SOUTH, state.getValue(WALL_HEIGHT_NORTH)).setValue(WALL_HEIGHT_WEST, state.getValue(WALL_HEIGHT_EAST));
			case COUNTERCLOCKWISE_90 -> state.setValue(WALL_HEIGHT_NORTH, state.getValue(WALL_HEIGHT_EAST)).setValue(WALL_HEIGHT_EAST, state.getValue(WALL_HEIGHT_SOUTH)).setValue(WALL_HEIGHT_SOUTH, state.getValue(WALL_HEIGHT_WEST)).setValue(WALL_HEIGHT_WEST, state.getValue(WALL_HEIGHT_NORTH));
			case CLOCKWISE_90 -> state.setValue(WALL_HEIGHT_NORTH, state.getValue(WALL_HEIGHT_WEST)).setValue(WALL_HEIGHT_EAST, state.getValue(WALL_HEIGHT_NORTH)).setValue(WALL_HEIGHT_SOUTH, state.getValue(WALL_HEIGHT_EAST)).setValue(WALL_HEIGHT_WEST, state.getValue(WALL_HEIGHT_SOUTH));
			default -> state;
		};
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return switch (mirrorIn) {
			case LEFT_RIGHT -> state.setValue(WALL_HEIGHT_NORTH, state.getValue(WALL_HEIGHT_SOUTH)).setValue(WALL_HEIGHT_SOUTH, state.getValue(WALL_HEIGHT_NORTH));
			case FRONT_BACK -> state.setValue(WALL_HEIGHT_EAST, state.getValue(WALL_HEIGHT_WEST)).setValue(WALL_HEIGHT_WEST, state.getValue(WALL_HEIGHT_EAST));
			default -> super.mirror(state, mirrorIn);
		};
	}

	@Override
	public void onLand(Level worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		FluidState fluidstate = worldIn.getFluidState(pos);
		BlockPos blockpos1 = pos.north();
		BlockPos blockpos2 = pos.east();
		BlockPos blockpos3 = pos.south();
		BlockPos blockpos4 = pos.west();
		BlockPos blockpos5 = pos.above();
		BlockState blockstate = ((LevelReader) worldIn).getBlockState(blockpos1);
		BlockState blockstate1 = ((LevelReader) worldIn).getBlockState(blockpos2);
		BlockState blockstate2 = ((LevelReader) worldIn).getBlockState(blockpos3);
		BlockState blockstate3 = ((LevelReader) worldIn).getBlockState(blockpos4);
		BlockState blockstate4 = ((LevelReader) worldIn).getBlockState(blockpos5);
		boolean flag = this.shouldConnect(blockstate, blockstate.isFaceSturdy(worldIn, blockpos1, Direction.SOUTH), Direction.SOUTH);
		boolean flag1 = this.shouldConnect(blockstate1, blockstate1.isFaceSturdy(worldIn, blockpos2, Direction.WEST), Direction.WEST);
		boolean flag2 = this.shouldConnect(blockstate2, blockstate2.isFaceSturdy(worldIn, blockpos3, Direction.NORTH), Direction.NORTH);
		boolean flag3 = this.shouldConnect(blockstate3, blockstate3.isFaceSturdy(worldIn, blockpos4, Direction.EAST), Direction.EAST);
		BlockState blockstate5 = shouldSolidify(worldIn, pos, hitState) ? this.getSolidifiedState() : this.defaultBlockState();
		blockstate5.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		BlockState bs = this.updateShape(worldIn, blockstate5, blockpos5, blockstate4, flag, flag1, flag2, flag3);
		worldIn.setBlockAndUpdate(pos, bs);
	}

}
