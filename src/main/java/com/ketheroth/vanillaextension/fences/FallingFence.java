package com.ketheroth.vanillaextension.fences;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FallingFence extends FallingBlock {

	public static final BooleanProperty NORTH = PipeBlock.NORTH;
	public static final BooleanProperty EAST = PipeBlock.EAST;
	public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
	public static final BooleanProperty WEST = PipeBlock.WEST;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((p_199775_0_) -> p_199775_0_.getKey().getAxis().isHorizontal()).collect(Util.toMap());
	protected final VoxelShape[] collisionShapes;
	protected final VoxelShape[] shapes;
	private final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap<>();
	private final VoxelShape[] renderShapes;

	public FallingFence(Properties properties) {
		super(properties);
		this.collisionShapes = this.makeShapes(2.0F, 2.0F, 24.0F, 0.0F, 24.0F);
		this.shapes = this.makeShapes(2.0F, 2.0F, 16.0F, 0.0F, 16.0F);

		for (BlockState blockstate : this.stateDefinition.getPossibleStates()) {
			this.getIndex(blockstate);
		}
		this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
		this.renderShapes = this.makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
	}

	private static int getMask(Direction facing) {
		return 1 << facing.get2DDataValue();
	}

	protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth, float p_196408_3_, float p_196408_4_, float p_196408_5_) {
		float f = 8.0F - nodeWidth;
		float f1 = 8.0F + nodeWidth;
		float f2 = 8.0F - extensionWidth;
		float f3 = 8.0F + extensionWidth;
		VoxelShape voxelshape = Block.box(f, 0.0D, f, f1, p_196408_3_, f1);
		VoxelShape voxelshape1 = Block.box(f2, p_196408_4_, 0.0D, f3, p_196408_5_, f3);
		VoxelShape voxelshape2 = Block.box(f2, p_196408_4_, f2, f3, p_196408_5_, 16.0D);
		VoxelShape voxelshape3 = Block.box(0.0D, p_196408_4_, f2, f3, p_196408_5_, f3);
		VoxelShape voxelshape4 = Block.box(f2, p_196408_4_, f2, 16.0D, p_196408_5_, f3);
		VoxelShape voxelshape5 = Shapes.or(voxelshape1, voxelshape4);
		VoxelShape voxelshape6 = Shapes.or(voxelshape2, voxelshape3);
		VoxelShape[] avoxelshape = new VoxelShape[]{Shapes.empty(), voxelshape2, voxelshape3, voxelshape6, voxelshape1, Shapes.or(voxelshape2, voxelshape1), Shapes.or(voxelshape3, voxelshape1), Shapes.or(voxelshape6, voxelshape1), voxelshape4, Shapes.or(voxelshape2, voxelshape4), Shapes.or(voxelshape3, voxelshape4), Shapes.or(voxelshape6, voxelshape4), voxelshape5, Shapes.or(voxelshape2, voxelshape5), Shapes.or(voxelshape3, voxelshape5), Shapes.or(voxelshape6, voxelshape5)};

		for (int i = 0; i < 16; ++i) {
			avoxelshape[i] = Shapes.or(voxelshape, avoxelshape[i]);
		}

		return avoxelshape;
	}

	protected int getIndex(BlockState state) {
		return this.stateToIndex.computeIntIfAbsent(state, (p_223007_0_) -> {
			int i = 0;
			if (p_223007_0_.getValue(NORTH)) {
				i |= getMask(Direction.NORTH);
			}

			if (p_223007_0_.getValue(EAST)) {
				i |= getMask(Direction.EAST);
			}

			if (p_223007_0_.getValue(SOUTH)) {
				i |= getMask(Direction.SOUTH);
			}

			if (p_223007_0_.getValue(WEST)) {
				i |= getMask(Direction.WEST);
			}

			return i;
		});
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return this.renderShapes[this.getIndex(state)];
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
		return this.getShape(state, reader, pos, context);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return !state.getValue(WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return this.shapes[this.getIndex(state)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return this.collisionShapes[this.getIndex(state)];
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (worldIn.isClientSide) {
			ItemStack itemstack = player.getItemInHand(handIn);
			return itemstack.getItem() == Items.LEAD ? InteractionResult.SUCCESS : InteractionResult.PASS;
		} else {
			return LeadItem.bindPlayerMobs(player, worldIn, pos);
		}
	}

	public boolean canConnect(BlockState state, boolean isSideSolid, Direction direction) {
		Block block = state.getBlock();
		boolean flag = this.isSameFence(state);
		boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction);
		return !isExceptionForConnection(state) && isSideSolid || flag || flag1;
	}

	private boolean isSameFence(BlockState state) {
		return state.is(BlockTags.FENCES) && state.is(BlockTags.WOODEN_FENCES) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter iblockreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockState blockstate = iblockreader.getBlockState(blockpos1);
		BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
		BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
		BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
		return super.getStateForPlacement(context).setValue(NORTH, this.canConnect(blockstate, blockstate.isFaceSturdy(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH)).setValue(EAST, this.canConnect(blockstate1, blockstate1.isFaceSturdy(iblockreader, blockpos2, Direction.WEST), Direction.WEST)).setValue(SOUTH, this.canConnect(blockstate2, blockstate2.isFaceSturdy(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH)).setValue(WEST, this.canConnect(blockstate3, blockstate3.isFaceSturdy(iblockreader, blockpos4, Direction.EAST), Direction.EAST)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		worldIn.getBlockTicks().scheduleTick(currentPos, this, this.getDelayAfterPlace());

		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		return facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? stateIn.setValue(FACING_TO_PROPERTY_MAP.get(facing), this.canConnect(facingState, facingState.isFaceSturdy(worldIn, facingPos, facing.getOpposite()), facing.getOpposite())) : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public void onLand(Level worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		FluidState fluidstate = worldIn.getFluidState(pos);
		BlockPos blockpos1 = pos.north();
		BlockPos blockpos2 = pos.east();
		BlockPos blockpos3 = pos.south();
		BlockPos blockpos4 = pos.west();
		BlockState blockstate = ((BlockGetter) worldIn).getBlockState(blockpos1);
		BlockState blockstate1 = ((BlockGetter) worldIn).getBlockState(blockpos2);
		BlockState blockstate2 = ((BlockGetter) worldIn).getBlockState(blockpos3);
		BlockState blockstate3 = ((BlockGetter) worldIn).getBlockState(blockpos4);
		BlockState bs = this.defaultBlockState().setValue(NORTH, this.canConnect(blockstate, blockstate.isFaceSturdy(worldIn, blockpos1, Direction.SOUTH), Direction.SOUTH)).setValue(EAST, this.canConnect(blockstate1, blockstate1.isFaceSturdy(worldIn, blockpos2, Direction.WEST), Direction.WEST)).setValue(SOUTH, this.canConnect(blockstate2, blockstate2.isFaceSturdy(worldIn, blockpos3, Direction.NORTH), Direction.NORTH)).setValue(WEST, this.canConnect(blockstate3, blockstate3.isFaceSturdy(worldIn, blockpos4, Direction.EAST), Direction.EAST)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		worldIn.setBlockAndUpdate(pos, bs);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return switch (rot) {
			case CLOCKWISE_180 -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
			case COUNTERCLOCKWISE_90 -> state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
			case CLOCKWISE_90 -> state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
			default -> state;
		};
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return switch (mirrorIn) {
			case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
			case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
			default -> super.mirror(state, mirrorIn);
		};
	}

}
