package com.nbrichau.vanillaextension.trapdoors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static com.nbrichau.vanillaextension.VanillaExtension.MODID;

public class ConcretePowderTrapdoor extends FallingBlock implements SimpleWaterloggedBlock {

	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
	public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape EAST_OPEN_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
	protected static final VoxelShape WEST_OPEN_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape SOUTH_OPEN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
	protected static final VoxelShape NORTH_OPEN_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
	protected static final VoxelShape TOP_AABB = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);


	public ConcretePowderTrapdoor(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(OPEN, Boolean.FALSE).setValue(HALF, Half.BOTTOM).setValue(POWERED, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
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

	@Override
	public void onLand(Level worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		if (shouldSolidify(worldIn, pos, hitState)) {
			worldIn.setBlock(pos, this.getSolidifiedState().setValue(HORIZONTAL_FACING, fallingState.getValue(HORIZONTAL_FACING)).setValue(OPEN, fallingState.getValue(OPEN)).setValue(HALF, fallingState.getValue(HALF)).setValue(POWERED, fallingState.getValue(POWERED)).setValue(WATERLOGGED, fallingState.getValue(WATERLOGGED)), 3);
		}
	}

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		if (!state.getValue(OPEN)) {
			return state.getValue(HALF) == Half.TOP ? TOP_AABB : BOTTOM_AABB;
		} else {
			switch ((Direction) state.getValue(HORIZONTAL_FACING)) {
				case NORTH:
				default:
					return NORTH_OPEN_AABB;
				case SOUTH:
					return SOUTH_OPEN_AABB;
				case WEST:
					return WEST_OPEN_AABB;
				case EAST:
					return EAST_OPEN_AABB;
			}
		}
	}

	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		switch (type) {
			case LAND:
				return state.getValue(OPEN);
			case WATER:
				return state.getValue(WATERLOGGED);
			case AIR:
				return state.getValue(OPEN);
			default:
				return false;
		}
	}

	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (this.material == Material.METAL) {
			return InteractionResult.PASS;
		} else {
			state = state.cycle(OPEN);
			worldIn.setBlock(pos, state, 2);
			if (state.getValue(WATERLOGGED)) {
				worldIn.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
			}

			this.playSound(player, worldIn, pos, state.getValue(OPEN));
			return InteractionResult.sidedSuccess(worldIn.isClientSide);
		}
	}

	protected void playSound(@Nullable Player player, Level worldIn, BlockPos pos, boolean p_185731_4_) {
		if (p_185731_4_) {
			int i = this.material == Material.METAL ? 1037 : 1007;
			worldIn.levelEvent(player, i, pos, 0);
		} else {
			int j = this.material == Material.METAL ? 1036 : 1013;
			worldIn.levelEvent(player, j, pos, 0);
		}

	}

	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isClientSide) {
			boolean flag = worldIn.hasNeighborSignal(pos);
			if (flag != state.getValue(POWERED)) {
				if (state.getValue(OPEN) != flag) {
					state = state.setValue(OPEN, flag);
					this.playSound((Player) null, worldIn, pos, flag);
				}

				worldIn.setBlock(pos, state.setValue(POWERED, flag), 2);
				if (state.getValue(WATERLOGGED)) {
					worldIn.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
				}
			}

		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, OPEN, HALF, POWERED, WATERLOGGED);
	}

	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean isLadder(BlockState state, net.minecraft.world.level.LevelReader world, BlockPos pos, net.minecraft.world.entity.LivingEntity entity) {
		if (state.getValue(OPEN)) {
			BlockState down = world.getBlockState(pos.below());
			if (down.getBlock() == net.minecraft.world.level.block.Blocks.LADDER)
				return down.getValue(LadderBlock.FACING) == state.getValue(HORIZONTAL_FACING);
		}
		return false;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter iblockreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate0 = iblockreader.getBlockState(blockpos);
		BlockState blockstate = shouldSolidify(iblockreader, blockpos, blockstate0) ? this.getSolidifiedState() : this.defaultBlockState();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		Direction direction = context.getClickedFace();
		if (!context.replacingClickedOnBlock() && direction.getAxis().isHorizontal()) {
			blockstate = blockstate.setValue(HORIZONTAL_FACING, direction).setValue(HALF, context.getClickLocation().y - (double) context.getClickedPos().getY() > 0.5D ? Half.TOP : Half.BOTTOM);
		} else {
			blockstate = blockstate.setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()).setValue(HALF, direction == Direction.UP ? Half.BOTTOM : Half.TOP);
		}

		if (context.getLevel().hasNeighborSignal(context.getClickedPos())) {
			blockstate = blockstate.setValue(OPEN, Boolean.TRUE).setValue(POWERED, Boolean.TRUE);
		}

		return blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
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
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		return isTouchingLiquid(worldIn, currentPos) ? this.getSolidifiedState().setValue(HORIZONTAL_FACING, stateIn.getValue(HORIZONTAL_FACING)).setValue(OPEN, stateIn.getValue(OPEN)).setValue(HALF, stateIn.getValue(HALF)).setValue(POWERED, stateIn.getValue(POWERED)).setValue(WATERLOGGED, stateIn.getValue(WATERLOGGED)) : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

}
