package com.nbrichau.vanillaextension.walls;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class ConcretePowderWall extends ConcretePowderBlock implements IWaterLoggable {
	private final BlockState solidifiedState;
	protected final VoxelShape[] collisionShapes;
	protected final VoxelShape[] shapes;
	private final Object2IntMap<BlockState> field_223008_i = new Object2IntOpenHashMap<>();
	private final VoxelShape[] wallShapes;
	private final VoxelShape[] wallCollisionShapes;

	public ConcretePowderWall(Block solidified, Properties properties) {
		super(solidified, properties);
		this.solidifiedState = solidified.getDefaultState();
		this.collisionShapes = this.makeShapes(0.0F, 3.0F, 24.0F, 0.0F, 24.0F);
		this.shapes = this.makeShapes(0.0F, 3.0F, 0.0F, 0.0F, 14.0F);

		this.setDefaultState(this.stateContainer.getBaseState().with(UP, Boolean.TRUE).with(NORTH, Boolean.FALSE).with(EAST, Boolean.FALSE).with(SOUTH, Boolean.FALSE).with(WEST, Boolean.FALSE).with(WATERLOGGED, Boolean.FALSE));
		this.wallShapes = this.makeShapes(4.0F, 3.0F, 16.0F, 0.0F, 14.0F);
		this.wallCollisionShapes = this.makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F);
	}

	protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth, float p_196408_3_, float p_196408_4_, float p_196408_5_) {
		float f = 8.0F - nodeWidth;
		float f1 = 8.0F + nodeWidth;
		float f2 = 8.0F - extensionWidth;
		float f3 = 8.0F + extensionWidth;
		VoxelShape voxelshape = Block.makeCuboidShape((double) f, 0.0D, (double) f, (double) f1, (double) p_196408_3_, (double) f1);
		VoxelShape voxelshape1 = Block.makeCuboidShape((double) f2, (double) p_196408_4_, 0.0D, (double) f3, (double) p_196408_5_, (double) f3);
		VoxelShape voxelshape2 = Block.makeCuboidShape((double) f2, (double) p_196408_4_, (double) f2, (double) f3, (double) p_196408_5_, 16.0D);
		VoxelShape voxelshape3 = Block.makeCuboidShape(0.0D, (double) p_196408_4_, (double) f2, (double) f3, (double) p_196408_5_, (double) f3);
		VoxelShape voxelshape4 = Block.makeCuboidShape((double) f2, (double) p_196408_4_, (double) f2, 16.0D, (double) p_196408_5_, (double) f3);
		VoxelShape voxelshape5 = VoxelShapes.or(voxelshape1, voxelshape4);
		VoxelShape voxelshape6 = VoxelShapes.or(voxelshape2, voxelshape3);
		VoxelShape[] avoxelshape = new VoxelShape[]{VoxelShapes.empty(), voxelshape2, voxelshape3, voxelshape6, voxelshape1, VoxelShapes.or(voxelshape2, voxelshape1), VoxelShapes.or(voxelshape3, voxelshape1), VoxelShapes.or(voxelshape6, voxelshape1), voxelshape4, VoxelShapes.or(voxelshape2, voxelshape4), VoxelShapes.or(voxelshape3, voxelshape4), VoxelShapes.or(voxelshape6, voxelshape4), voxelshape5, VoxelShapes.or(voxelshape2, voxelshape5), VoxelShapes.or(voxelshape3, voxelshape5), VoxelShapes.or(voxelshape6, voxelshape5)};

		for (int i = 0; i < 16; ++i) {
			avoxelshape[i] = VoxelShapes.or(voxelshape, avoxelshape[i]);
		}

		return avoxelshape;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.get(UP) ? this.wallShapes[this.getIndex(state)] : this.shapes[this.getIndex(state)];
	}

	private static int getMask(Direction facing) {
		return 1 << facing.getHorizontalIndex();
	}

	protected int getIndex(BlockState state) {
		return this.field_223008_i.computeIntIfAbsent(state, (p_223007_0_) -> {
			int i = 0;
			if (p_223007_0_.get(NORTH)) {
				i |= getMask(Direction.NORTH);
			}

			if (p_223007_0_.get(EAST)) {
				i |= getMask(Direction.EAST);
			}

			if (p_223007_0_.get(SOUTH)) {
				i |= getMask(Direction.SOUTH);
			}

			if (p_223007_0_.get(WEST)) {
				i |= getMask(Direction.WEST);
			}

			return i;
		});
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.get(UP) ? this.wallCollisionShapes[this.getIndex(state)] : this.collisionShapes[this.getIndex(state)];
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IWorldReader iworldreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockState blockstate1 = iworldreader.getBlockState(blockpos1);
		BlockState blockstate2 = iworldreader.getBlockState(blockpos2);
		BlockState blockstate3 = iworldreader.getBlockState(blockpos3);
		BlockState blockstate4 = iworldreader.getBlockState(blockpos4);
		boolean flag = this.shouldConnect(blockstate1, blockstate1.isSolidSide(iworldreader, blockpos1, Direction.SOUTH), Direction.SOUTH);
		boolean flag1 = this.shouldConnect(blockstate2, blockstate2.isSolidSide(iworldreader, blockpos2, Direction.WEST), Direction.WEST);
		boolean flag2 = this.shouldConnect(blockstate3, blockstate3.isSolidSide(iworldreader, blockpos3, Direction.NORTH), Direction.NORTH);
		boolean flag3 = this.shouldConnect(blockstate4, blockstate4.isSolidSide(iworldreader, blockpos4, Direction.EAST), Direction.EAST);
		boolean flag4 = (!flag || flag1 || !flag2 || flag3) && (flag || !flag1 || flag2 || !flag3);
		return (shouldSolidify(iworldreader, blockpos, iworldreader.getBlockState(blockpos)) ? this.solidifiedState : super.getStateForPlacement(context))
				.with(UP, flag4 || !iworldreader.isAirBlock(blockpos.up())).with(NORTH, flag)
				.with(EAST, flag1).with(SOUTH, flag2).with(WEST, flag3).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
	}

	private boolean shouldConnect(BlockState state, boolean sideSolid, Direction directionIn) {
		Block block = state.getBlock();
		boolean flag = block.isIn(BlockTags.WALLS) || block instanceof FenceGateBlock && FenceGateBlock.isParallel(state, directionIn);
		return !cannotAttach(block) && sideSolid || flag;
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState blockstate = isTouchingLiquid(worldIn, currentPos) ? this.solidifiedState : stateIn;

		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		if (facing == Direction.DOWN) {
			return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		} else {
			Direction direction = facing.getOpposite();
			boolean flag = facing == Direction.NORTH ? this.shouldConnect(facingState, facingState.isSolidSide(worldIn, facingPos, direction), direction) : stateIn.get(NORTH);
			boolean flag1 = facing == Direction.EAST ? this.shouldConnect(facingState, facingState.isSolidSide(worldIn, facingPos, direction), direction) : stateIn.get(EAST);
			boolean flag2 = facing == Direction.SOUTH ? this.shouldConnect(facingState, facingState.isSolidSide(worldIn, facingPos, direction), direction) : stateIn.get(SOUTH);
			boolean flag3 = facing == Direction.WEST ? this.shouldConnect(facingState, facingState.isSolidSide(worldIn, facingPos, direction), direction) : stateIn.get(WEST);
			boolean flag4 = (!flag || flag1 || !flag2 || flag3) && (flag || !flag1 || flag2 || !flag3);
			return blockstate.with(UP, flag4 || !worldIn.isAirBlock(currentPos.up())).with(NORTH, flag).with(EAST, flag1).with(SOUTH, flag2).with(WEST, flag3);
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(UP, NORTH, EAST, WEST, SOUTH, WATERLOGGED);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return !state.get(WATERLOGGED);
	}

	@Override
	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		switch (rot) {
			case CLOCKWISE_180:
				return state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
			case COUNTERCLOCKWISE_90:
				return state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
			case CLOCKWISE_90:
				return state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
			default:
				return state;
		}
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		switch (mirrorIn) {
			case LEFT_RIGHT:
				return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
			case FRONT_BACK:
				return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
			default:
				return super.mirror(state, mirrorIn);
		}
	}

	@Override
	public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState) {
		if (shouldSolidify(worldIn, pos, hitState)) {
			worldIn.setBlockState(pos, this.solidifiedState, 3);
		}
	}

	private static boolean shouldSolidify(IBlockReader p_230137_0_, BlockPos p_230137_1_, BlockState p_230137_2_) {
		return causesSolidify(p_230137_2_) || isTouchingLiquid(p_230137_0_, p_230137_1_);
	}

	private static boolean isTouchingLiquid(IBlockReader p_196441_0_, BlockPos p_196441_1_) {
		boolean flag = false;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_196441_1_);
		for (Direction direction : Direction.values()) {
			BlockState blockstate = p_196441_0_.getBlockState(blockpos$mutable);
			if (direction != Direction.DOWN || causesSolidify(blockstate)) {
				blockpos$mutable.setPos(p_196441_1_).move(direction);
				blockstate = p_196441_0_.getBlockState(blockpos$mutable);
				if (causesSolidify(blockstate) && !blockstate.isSolidSide(p_196441_0_, p_196441_1_, direction.getOpposite())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	private static boolean causesSolidify(BlockState p_212566_0_) {
		return p_212566_0_.getFluidState().isTagged(FluidTags.WATER);
	}
}