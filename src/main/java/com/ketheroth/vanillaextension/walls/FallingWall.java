package com.ketheroth.vanillaextension.walls;

import com.google.common.collect.ImmutableMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FallingWall extends FallingBlock {

	public static final BooleanProperty UP = BlockStateProperties.UP;
	public static final EnumProperty<WallSide> WALL_HEIGHT_EAST = BlockStateProperties.EAST_WALL;
	public static final EnumProperty<WallSide> WALL_HEIGHT_NORTH = BlockStateProperties.NORTH_WALL;
	public static final EnumProperty<WallSide> WALL_HEIGHT_SOUTH = BlockStateProperties.SOUTH_WALL;
	public static final EnumProperty<WallSide> WALL_HEIGHT_WEST = BlockStateProperties.WEST_WALL;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape POST_TEST = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape NORTH_TEST = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape SOUTH_TEST = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 16.0D);
	private static final VoxelShape WEST_TEST = Block.box(0.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape EAST_TEST = Block.box(7.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);
	private final Map<BlockState, VoxelShape> stateToShapeMap;
	private final Map<BlockState, VoxelShape> stateToCollisionShapeMap;

	public FallingWall(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.TRUE).setValue(WALL_HEIGHT_NORTH, WallSide.NONE).setValue(WALL_HEIGHT_EAST, WallSide.NONE).setValue(WALL_HEIGHT_SOUTH, WallSide.NONE).setValue(WALL_HEIGHT_WEST, WallSide.NONE).setValue(WATERLOGGED, Boolean.FALSE));
		this.stateToShapeMap = this.makeShapes(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
		this.stateToCollisionShapeMap = this.makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
	}

	private static VoxelShape applyWallShape(VoxelShape p_235631_0_, WallSide p_235631_1_, VoxelShape p_235631_2_, VoxelShape p_235631_3_) {
		if (p_235631_1_ == WallSide.TALL) {
			return Shapes.or(p_235631_0_, p_235631_3_);
		} else {
			return p_235631_1_ == WallSide.LOW ? Shapes.or(p_235631_0_, p_235631_2_) : p_235631_0_;
		}
	}

	private static boolean isConnected(BlockState p_235629_0_, Property<WallSide> p_235629_1_) {
		return p_235629_0_.getValue(p_235629_1_) != WallSide.NONE;
	}

	private static boolean isCovered(VoxelShape p_235632_0_, VoxelShape p_235632_1_) {
		return !Shapes.joinIsNotEmpty(p_235632_1_, p_235632_0_, BooleanOp.ONLY_FIRST);
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
							voxelshape9 = applyWallShape(voxelshape9, wallheight, voxelshape4, voxelshape8);
							voxelshape9 = applyWallShape(voxelshape9, wallheight2, voxelshape3, voxelshape7);
							voxelshape9 = applyWallShape(voxelshape9, wallheight1, voxelshape1, voxelshape5);
							voxelshape9 = applyWallShape(voxelshape9, wallheight3, voxelshape2, voxelshape6);
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

	private boolean connectsTo(BlockState state, boolean p_220113_2_, Direction direction) {
		Block block = state.getBlock();
		boolean flag = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction);
		return state.is(BlockTags.WALLS) || !isExceptionForConnection(state) && p_220113_2_ || block instanceof IronBarsBlock || flag;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		LevelReader iworldreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockPos blockpos5 = blockpos.above();
		BlockState blockstate = iworldreader.getBlockState(blockpos1);
		BlockState blockstate1 = iworldreader.getBlockState(blockpos2);
		BlockState blockstate2 = iworldreader.getBlockState(blockpos3);
		BlockState blockstate3 = iworldreader.getBlockState(blockpos4);
		BlockState blockstate4 = iworldreader.getBlockState(blockpos5);
		boolean flag = this.connectsTo(blockstate, blockstate.isFaceSturdy(iworldreader, blockpos1, Direction.SOUTH), Direction.SOUTH);
		boolean flag1 = this.connectsTo(blockstate1, blockstate1.isFaceSturdy(iworldreader, blockpos2, Direction.WEST), Direction.WEST);
		boolean flag2 = this.connectsTo(blockstate2, blockstate2.isFaceSturdy(iworldreader, blockpos3, Direction.NORTH), Direction.NORTH);
		boolean flag3 = this.connectsTo(blockstate3, blockstate3.isFaceSturdy(iworldreader, blockpos4, Direction.EAST), Direction.EAST);
		BlockState blockstate5 = this.defaultBlockState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		return this.updateShape(iworldreader, blockstate5, blockpos5, blockstate4, flag, flag1, flag2, flag3);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		worldIn.getBlockTicks().scheduleTick(currentPos, this, this.getDelayAfterPlace());

		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		if (facing == Direction.DOWN) {
			return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		} else {
			return facing == Direction.UP ? this.topUpdate(worldIn, stateIn, facingPos, facingState) : this.sideUpdate(worldIn, currentPos, stateIn, facingPos, facingState, facing);
		}
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
		boolean flag = this.connectsTo(blockstate, blockstate.isFaceSturdy(worldIn, blockpos1, Direction.SOUTH), Direction.SOUTH);
		boolean flag1 = this.connectsTo(blockstate1, blockstate1.isFaceSturdy(worldIn, blockpos2, Direction.WEST), Direction.WEST);
		boolean flag2 = this.connectsTo(blockstate2, blockstate2.isFaceSturdy(worldIn, blockpos3, Direction.NORTH), Direction.NORTH);
		boolean flag3 = this.connectsTo(blockstate3, blockstate3.isFaceSturdy(worldIn, blockpos4, Direction.EAST), Direction.EAST);
		BlockState blockstate5 = this.defaultBlockState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		BlockState bs = this.updateShape(worldIn, blockstate5, blockpos5, blockstate4, flag, flag1, flag2, flag3);
		worldIn.setBlockAndUpdate(pos, bs);
	}

	private BlockState topUpdate(LevelReader p_235625_1_, BlockState p_235625_2_, BlockPos p_235625_3_, BlockState p_235625_4_) {
		boolean flag = isConnected(p_235625_2_, WALL_HEIGHT_NORTH);
		boolean flag1 = isConnected(p_235625_2_, WALL_HEIGHT_EAST);
		boolean flag2 = isConnected(p_235625_2_, WALL_HEIGHT_SOUTH);
		boolean flag3 = isConnected(p_235625_2_, WALL_HEIGHT_WEST);
		return this.updateShape(p_235625_1_, p_235625_2_, p_235625_3_, p_235625_4_, flag, flag1, flag2, flag3);
	}

	private BlockState sideUpdate(LevelReader p_235627_1_, BlockPos p_235627_2_, BlockState p_235627_3_, BlockPos p_235627_4_, BlockState p_235627_5_, Direction p_235627_6_) {
		Direction direction = p_235627_6_.getOpposite();
		boolean flag = p_235627_6_ == Direction.NORTH ? this.connectsTo(p_235627_5_, p_235627_5_.isFaceSturdy(p_235627_1_, p_235627_4_, direction), direction) : isConnected(p_235627_3_, WALL_HEIGHT_NORTH);
		boolean flag1 = p_235627_6_ == Direction.EAST ? this.connectsTo(p_235627_5_, p_235627_5_.isFaceSturdy(p_235627_1_, p_235627_4_, direction), direction) : isConnected(p_235627_3_, WALL_HEIGHT_EAST);
		boolean flag2 = p_235627_6_ == Direction.SOUTH ? this.connectsTo(p_235627_5_, p_235627_5_.isFaceSturdy(p_235627_1_, p_235627_4_, direction), direction) : isConnected(p_235627_3_, WALL_HEIGHT_SOUTH);
		boolean flag3 = p_235627_6_ == Direction.WEST ? this.connectsTo(p_235627_5_, p_235627_5_.isFaceSturdy(p_235627_1_, p_235627_4_, direction), direction) : isConnected(p_235627_3_, WALL_HEIGHT_WEST);
		BlockPos blockpos = p_235627_2_.above();
		BlockState blockstate = p_235627_1_.getBlockState(blockpos);
		return this.updateShape(p_235627_1_, p_235627_3_, blockpos, blockstate, flag, flag1, flag2, flag3);
	}

	private BlockState updateShape(LevelReader p_235626_1_, BlockState p_235626_2_, BlockPos p_235626_3_, BlockState p_235626_4_, boolean p_235626_5_, boolean p_235626_6_, boolean p_235626_7_, boolean p_235626_8_) {
		VoxelShape voxelshape = p_235626_4_.getBlockSupportShape(p_235626_1_, p_235626_3_).getFaceShape(Direction.DOWN);
		BlockState blockstate = this.updateSides(p_235626_2_, p_235626_5_, p_235626_6_, p_235626_7_, p_235626_8_, voxelshape);
		return blockstate.setValue(UP, this.shouldRaisePost(blockstate, p_235626_4_, voxelshape));
	}

	private boolean shouldRaisePost(BlockState p_235628_1_, BlockState p_235628_2_, VoxelShape p_235628_3_) {
		boolean flag = p_235628_2_.getBlock() instanceof WallBlock && p_235628_2_.getValue(UP);
		if (flag) {
			return true;
		} else {
			WallSide wallheight = p_235628_1_.getValue(WALL_HEIGHT_NORTH);
			WallSide wallheight1 = p_235628_1_.getValue(WALL_HEIGHT_SOUTH);
			WallSide wallheight2 = p_235628_1_.getValue(WALL_HEIGHT_EAST);
			WallSide wallheight3 = p_235628_1_.getValue(WALL_HEIGHT_WEST);
			boolean flag1 = wallheight1 == WallSide.NONE;
			boolean flag2 = wallheight3 == WallSide.NONE;
			boolean flag3 = wallheight2 == WallSide.NONE;
			boolean flag4 = wallheight == WallSide.NONE;
			boolean flag5 = flag4 && flag1 && flag2 && flag3 || flag4 != flag1 || flag2 != flag3;
			if (flag5) {
				return true;
			} else {
				boolean flag6 = wallheight == WallSide.TALL && wallheight1 == WallSide.TALL || wallheight2 == WallSide.TALL && wallheight3 == WallSide.TALL;
				if (flag6) {
					return false;
				} else {
					return p_235628_2_.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(p_235628_3_, POST_TEST);
				}
			}
		}
	}

	private BlockState updateSides(BlockState p_235630_1_, boolean p_235630_2_, boolean p_235630_3_, boolean p_235630_4_, boolean p_235630_5_, VoxelShape p_235630_6_) {
		return p_235630_1_.setValue(WALL_HEIGHT_NORTH, this.makeWallState(p_235630_2_, p_235630_6_, NORTH_TEST)).setValue(WALL_HEIGHT_EAST, this.makeWallState(p_235630_3_, p_235630_6_, EAST_TEST)).setValue(WALL_HEIGHT_SOUTH, this.makeWallState(p_235630_4_, p_235630_6_, SOUTH_TEST)).setValue(WALL_HEIGHT_WEST, this.makeWallState(p_235630_5_, p_235630_6_, WEST_TEST));
	}

	private WallSide makeWallState(boolean p_235633_1_, VoxelShape p_235633_2_, VoxelShape p_235633_3_) {
		if (p_235633_1_) {
			return isCovered(p_235633_2_, p_235633_3_) ? WallSide.TALL : WallSide.LOW;
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

}
