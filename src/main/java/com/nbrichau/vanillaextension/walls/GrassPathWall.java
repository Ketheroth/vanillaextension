package com.nbrichau.vanillaextension.walls;

import com.google.common.collect.ImmutableMap;
import com.nbrichau.vanillaextension.init.WallInit;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class GrassPathWall extends WallBlock {

	private final Map<BlockState, VoxelShape> stateToShapeMap;
	private final Map<BlockState, VoxelShape> stateToCollisionShapeMap;
	private static final VoxelShape CENTER_POLE_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 15.0D, 9.0D);
	private static final VoxelShape WALL_CONNECTION_NORTH_SIDE_SHAPE = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 15.0D, 9.0D);
	private static final VoxelShape WALL_CONNECTION_SOUTH_SIDE_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 15.0D, 16.0D);
	private static final VoxelShape WALL_CONNECTION_WEST_SIDE_SHAPE = Block.box(0.0D, 0.0D, 7.0D, 9.0D, 15.0D, 9.0D);
	private static final VoxelShape WALL_CONNECTION_EAST_SIDE_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 16.0D, 15.0D, 9.0D);

	public GrassPathWall(Properties properties) {
		super(properties);
		this.stateToShapeMap = this.makeShapes(4.0F, 3.0F, 15.0F, 0.0F, 13.0F, 16.0F);
		this.stateToCollisionShapeMap = this.makeShapes(4.0F, 3.0F, 23.0F, 0.0F, 23.0F, 23.0F);

	}

	private Map<BlockState, VoxelShape> makeShapes(float p_235624_1_, float p_235624_2_, float p_235624_3_, float p_235624_4_, float p_235624_5_, float p_235624_6_) {
		float f = 8.0F - p_235624_1_;
		float f1 = 8.0F + p_235624_1_;
		float f2 = 8.0F - p_235624_2_;
		float f3 = 8.0F + p_235624_2_;
		VoxelShape voxelshape = Block.box((double)f, 0.0D, (double)f, (double)f1, (double)p_235624_3_, (double)f1);
		VoxelShape voxelshape1 = Block.box((double)f2, (double)p_235624_4_, 0.0D, (double)f3, (double)p_235624_5_, (double)f3);
		VoxelShape voxelshape2 = Block.box((double)f2, (double)p_235624_4_, (double)f2, (double)f3, (double)p_235624_5_, 16.0D);
		VoxelShape voxelshape3 = Block.box(0.0D, (double)p_235624_4_, (double)f2, (double)f3, (double)p_235624_5_, (double)f3);
		VoxelShape voxelshape4 = Block.box((double)f2, (double)p_235624_4_, (double)f2, 16.0D, (double)p_235624_5_, (double)f3);
		VoxelShape voxelshape5 = Block.box((double)f2, (double)p_235624_4_, 0.0D, (double)f3, (double)p_235624_6_, (double)f3);
		VoxelShape voxelshape6 = Block.box((double)f2, (double)p_235624_4_, (double)f2, (double)f3, (double)p_235624_6_, 16.0D);
		VoxelShape voxelshape7 = Block.box(0.0D, (double)p_235624_4_, (double)f2, (double)f3, (double)p_235624_6_, (double)f3);
		VoxelShape voxelshape8 = Block.box((double)f2, (double)p_235624_4_, (double)f2, 16.0D, (double)p_235624_6_, (double)f3);
		ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

		for(Boolean obool : UP.getPossibleValues()) {
			for(WallHeight wallheight : EAST_WALL.getPossibleValues()) {
				for(WallHeight wallheight1 : NORTH_WALL.getPossibleValues()) {
					for(WallHeight wallheight2 : WEST_WALL.getPossibleValues()) {
						for(WallHeight wallheight3 : SOUTH_WALL.getPossibleValues()) {
							VoxelShape voxelshape9 = VoxelShapes.empty();
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight, voxelshape4, voxelshape8);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight2, voxelshape3, voxelshape7);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight1, voxelshape1, voxelshape5);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight3, voxelshape2, voxelshape6);
							if (obool) {
								voxelshape9 = VoxelShapes.or(voxelshape9, voxelshape);
							}

							BlockState blockstate = this.defaultBlockState().setValue(UP, obool).setValue(EAST_WALL, wallheight).setValue(WEST_WALL, wallheight2).setValue(NORTH_WALL, wallheight1).setValue(SOUTH_WALL, wallheight3);
							builder.put(blockstate.setValue(WATERLOGGED, Boolean.FALSE), voxelshape9);
							builder.put(blockstate.setValue(WATERLOGGED, Boolean.TRUE), voxelshape9);
						}
					}
				}
			}
		}

		return builder.build();
	}

	private static VoxelShape getHeightAlteredShape(VoxelShape baseShape, WallHeight height, VoxelShape lowShape, VoxelShape tallShape) {
		if (height == WallHeight.TALL) {
			return VoxelShapes.or(baseShape, tallShape);
		} else {
			return height == WallHeight.LOW ? VoxelShapes.or(baseShape, lowShape) : baseShape;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IWorldReader iworldreader = context.getLevel();
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
		boolean flag = this.shouldConnect(blockstate, blockstate.isFaceSturdy(iworldreader, blockpos1, Direction.SOUTH), Direction.SOUTH);
		boolean flag1 = this.shouldConnect(blockstate1, blockstate1.isFaceSturdy(iworldreader, blockpos2, Direction.WEST), Direction.WEST);
		boolean flag2 = this.shouldConnect(blockstate2, blockstate2.isFaceSturdy(iworldreader, blockpos3, Direction.NORTH), Direction.NORTH);
		boolean flag3 = this.shouldConnect(blockstate3, blockstate3.isFaceSturdy(iworldreader, blockpos4, Direction.EAST), Direction.EAST);
		BlockState blockstate5 = (!this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? WallInit.dirt_wall.defaultBlockState() : this.defaultBlockState()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		return this.updateShape(iworldreader, blockstate5, blockpos5, blockstate4, flag, flag1, flag2, flag3);
	}
	private boolean shouldConnect(BlockState state, boolean sideSolid, Direction direction) {
		Block block = state.getBlock();
		boolean flag = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction);
		return state.is(BlockTags.WALLS) || !isExceptionForConnection(block) && sideSolid || block instanceof PaneBlock || flag;
	}
	private BlockState updateShape(IWorldReader reader, BlockState state, BlockPos pos, BlockState collisionState, boolean connectedSouth, boolean connectedWest, boolean connectedNorth, boolean connectedEast) {
		VoxelShape voxelshape = collisionState.getBlockSupportShape(reader, pos).getFaceShape(Direction.DOWN);
		BlockState blockstate = this.updateSides(state, connectedSouth, connectedWest, connectedNorth, connectedEast, voxelshape);
		return blockstate.setValue(UP, Boolean.valueOf(this.shouldRaisePost(blockstate, collisionState, voxelshape)));
	}
	private boolean shouldRaisePost(BlockState p_235628_1_, BlockState p_235628_2_, VoxelShape shape) {
		boolean flag = p_235628_2_.getBlock() instanceof WallBlock && p_235628_2_.getValue(UP);
		if (flag) {
			return true;
		} else {
			WallHeight wallheight = p_235628_1_.getValue(NORTH_WALL);
			WallHeight wallheight1 = p_235628_1_.getValue(SOUTH_WALL);
			WallHeight wallheight2 = p_235628_1_.getValue(EAST_WALL);
			WallHeight wallheight3 = p_235628_1_.getValue(WEST_WALL);
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
		return state.setValue(NORTH_WALL, this.makeWallState(connectedSouth, shape, WALL_CONNECTION_NORTH_SIDE_SHAPE)).setValue(EAST_WALL, this.makeWallState(connectedWest, shape, WALL_CONNECTION_EAST_SIDE_SHAPE)).setValue(SOUTH_WALL, this.makeWallState(connectedNorth, shape, WALL_CONNECTION_SOUTH_SIDE_SHAPE)).setValue(WEST_WALL, this.makeWallState(connectedEast, shape, WALL_CONNECTION_WEST_SIDE_SHAPE));
	}
	private WallHeight makeWallState(boolean p_235633_1_, VoxelShape p_235633_2_, VoxelShape p_235633_3_) {
		if (p_235633_1_) {
			return compareShapes(p_235633_2_, p_235633_3_) ? WallHeight.TALL : WallHeight.LOW;
		} else {
			return WallHeight.NONE;
		}
	}
	private static boolean compareShapes(VoxelShape shape1, VoxelShape shape2) {
		return !VoxelShapes.joinIsNotEmpty(shape2, shape1, IBooleanFunction.ONLY_FIRST);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.UP && !stateIn.canSurvive(worldIn, currentPos)) {
			worldIn.getBlockTicks().scheduleTick(currentPos, this, 1);
		}

		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (!this.canSurvive(state, worldIn, pos)) {
			worldIn.setBlockAndUpdate(pos, pushEntitiesUp(state, WallInit.dirt_wall.defaultBlockState()
					.setValue(UP, state.getValue(UP)).setValue(NORTH_WALL, state.getValue(NORTH_WALL)).setValue(EAST_WALL, state.getValue(EAST_WALL))
					.setValue(SOUTH_WALL, state.getValue(SOUTH_WALL)).setValue(WEST_WALL, state.getValue(WEST_WALL)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)),
					worldIn, pos));
		}
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.above());
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock;
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.stateToShapeMap.get(state);
	}

	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.stateToCollisionShapeMap.get(state);
	}

}
