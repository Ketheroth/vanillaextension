package com.nbrichau.vanillaextension.fences;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LeadItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Map;

public class ConcretePowderFence extends ConcretePowderBlock implements IWaterLoggable {
	public static final BooleanProperty NORTH = SixWayBlock.NORTH;
	public static final BooleanProperty EAST = SixWayBlock.EAST;
	public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
	public static final BooleanProperty WEST = SixWayBlock.WEST;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.FACING_TO_PROPERTY_MAP.entrySet().stream().filter((p_199775_0_) -> {
		return p_199775_0_.getKey().getAxis().isHorizontal();
	}).collect(Util.toMapCollector());
	protected final VoxelShape[] collisionShapes;
	protected final VoxelShape[] shapes;
	private final Object2IntMap<BlockState> field_223008_i = new Object2IntOpenHashMap<>();
	private final VoxelShape[] renderShapes;

	private final BlockState solidifiedState;

	public ConcretePowderFence(Block solidified, Properties properties) {
		super(solidified, properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, Boolean.FALSE).with(EAST, Boolean.FALSE).with(SOUTH, Boolean.FALSE).with(WEST, Boolean.FALSE).with(WATERLOGGED, Boolean.FALSE));
		this.renderShapes = this.makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
		this.collisionShapes = this.makeShapes(2.0F, 2.0F, 24.0F, 0.0F, 24.0F);
		this.shapes = this.makeShapes(2.0F, 2.0F, 16.0F, 0.0F, 16.0F);

		for (BlockState blockstate : this.stateContainer.getValidStates()) {
			this.getIndex(blockstate);
		}
		this.solidifiedState = solidified.getDefaultState();
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

	private static int getMask(Direction facing) {
		return 1 << facing.getHorizontalIndex();
	}

	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return this.renderShapes[this.getIndex(state)];
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	public boolean canConnect(BlockState state, boolean isSideSolid, Direction direction) {
		Block block = state.getBlock();
		boolean flag = this.func_235493_c_(block);
		boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.isParallel(state, direction);
		return !cannotAttach(block) && isSideSolid || flag || flag1;
	}

	private boolean func_235493_c_(Block block) {
		return block.isIn(BlockTags.FENCES) && block.isIn(BlockTags.WOODEN_FENCES) == this.getDefaultState().isIn(BlockTags.WOODEN_FENCES);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote) {
			ItemStack itemstack = player.getHeldItem(handIn);
			return itemstack.getItem() == Items.LEAD ? ActionResultType.SUCCESS : ActionResultType.PASS;
		} else {
			return LeadItem.bindPlayerMobs(player, worldIn, pos);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IBlockReader iblockreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState blockstate = iblockreader.getBlockState(blockpos);
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockState blockstate1 = iblockreader.getBlockState(blockpos1);
		BlockState blockstate2 = iblockreader.getBlockState(blockpos2);
		BlockState blockstate3 = iblockreader.getBlockState(blockpos3);
		BlockState blockstate4 = iblockreader.getBlockState(blockpos4);
		BlockState finalState = shouldSolidify(iblockreader, blockpos, blockstate) ? this.solidifiedState : this.getDefaultState();
		return finalState.with(NORTH, this.canConnect(blockstate1, blockstate1.isSolidSide(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH))
				.with(EAST, this.canConnect(blockstate2, blockstate2.isSolidSide(iblockreader, blockpos2, Direction.WEST), Direction.WEST))
				.with(SOUTH, this.canConnect(blockstate3, blockstate3.isSolidSide(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH))
				.with(WEST, this.canConnect(blockstate4, blockstate4.isSolidSide(iblockreader, blockpos4, Direction.EAST), Direction.EAST))
				.with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		BlockState state = isTouchingLiquid(worldIn, currentPos) ? this.solidifiedState.with(NORTH, stateIn.get(NORTH)).with(EAST, stateIn.get(EAST)).with(SOUTH, stateIn.get(SOUTH)).with(WEST, stateIn.get(WEST)).with(WATERLOGGED, stateIn.get(WATERLOGGED)) : stateIn;
		return facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL ?
				state.with(FACING_TO_PROPERTY_MAP.get(facing), this.canConnect(facingState, facingState.isSolidSide(worldIn, facingPos, facing.getOpposite()), facing.getOpposite())) :
				super.updatePostPlacement(state, facing, facingState, worldIn, currentPos, facingPos);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
	}

	@Override
	public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState) {
		if (shouldSolidify(worldIn, pos, hitState)) {
			IFluidState ifluidstate = worldIn.getFluidState(pos);
			BlockPos blockpos1 = pos.north();
			BlockPos blockpos2 = pos.east();
			BlockPos blockpos3 = pos.south();
			BlockPos blockpos4 = pos.west();
			BlockState blockstate1 = ((IBlockReader) worldIn).getBlockState(blockpos1);
			BlockState blockstate2 = ((IBlockReader) worldIn).getBlockState(blockpos2);
			BlockState blockstate3 = ((IBlockReader) worldIn).getBlockState(blockpos3);
			BlockState blockstate4 = ((IBlockReader) worldIn).getBlockState(blockpos4);
			BlockState bs = this.solidifiedState
					.with(NORTH, this.canConnect(blockstate1, blockstate1.isSolidSide(worldIn, blockpos1, Direction.SOUTH), Direction.SOUTH))
					.with(EAST, this.canConnect(blockstate2, blockstate2.isSolidSide(worldIn, blockpos2, Direction.WEST), Direction.WEST))
					.with(SOUTH, this.canConnect(blockstate3, blockstate3.isSolidSide(worldIn, blockpos3, Direction.NORTH), Direction.NORTH))
					.with(WEST, this.canConnect(blockstate4, blockstate4.isSolidSide(worldIn, blockpos4, Direction.EAST), Direction.EAST))
					.with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);

			worldIn.setBlockState(pos, bs);
		}
	}

	private static boolean shouldSolidify(IBlockReader reader, BlockPos pos, BlockState state) {
		return causesSolidify(state) || isTouchingLiquid(reader, pos);
	}

	private static boolean isTouchingLiquid(IBlockReader reader, BlockPos pos) {
		boolean flag = false;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(pos);
		for (Direction direction : Direction.values()) {
			BlockState blockstate = reader.getBlockState(blockpos$mutable);
			if (direction != Direction.DOWN || causesSolidify(blockstate)) {
				blockpos$mutable.setPos(pos).move(direction);
				blockstate = reader.getBlockState(blockpos$mutable);
				if (causesSolidify(blockstate) && !blockstate.isSolidSide(reader, pos, direction.getOpposite())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	private static boolean causesSolidify(BlockState state) {
		return state.getFluidState().isTagged(FluidTags.WATER);
	}

}