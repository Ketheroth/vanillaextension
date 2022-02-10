package com.ketheroth.vanillaextension.fences;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
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
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

import static com.ketheroth.vanillaextension.VanillaExtension.MODID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConcretePowderFence extends FallingBlock implements SimpleWaterloggedBlock {

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

	public ConcretePowderFence(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
		this.renderShapes = this.makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
		this.collisionShapes = this.makeShapes(2.0F, 2.0F, 24.0F, 0.0F, 24.0F);
		this.shapes = this.makeShapes(2.0F, 2.0F, 16.0F, 0.0F, 16.0F);

		for (BlockState blockstate : this.stateDefinition.getPossibleStates()) {
			this.getIndex(blockstate);
		}
	}

	private static int getMask(Direction facing) {
		return 1 << facing.get2DDataValue();
	}

	private static boolean shouldSolidify(BlockGetter getter, BlockPos pos, BlockState state) {
		return canSolidify(state) || touchesLiquid(getter, pos);
	}

	private static boolean touchesLiquid(BlockGetter getter, BlockPos pos) {
		boolean flag = false;
		BlockPos.MutableBlockPos mutable = pos.mutable();
		for (Direction direction : Direction.values()) {
			BlockState blockstate = getter.getBlockState(mutable);
			if (direction != Direction.DOWN || canSolidify(blockstate)) {
				mutable.setWithOffset(pos, direction);
				blockstate = getter.getBlockState(mutable);
				if (canSolidify(blockstate) && !blockstate.isFaceSturdy(getter, pos, direction.getOpposite())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	private static boolean canSolidify(BlockState state) {
		return state.getFluidState().is(FluidTags.WATER);
	}

	protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth, float nodeHeight, float extensionBottom, float extensionHeight) {
		float f = 8.0F - nodeWidth;
		float f1 = 8.0F + nodeWidth;
		float f2 = 8.0F - extensionWidth;
		float f3 = 8.0F + extensionWidth;
		VoxelShape voxelshape = Block.box(f, 0.0D, f, f1, nodeHeight, f1);
		VoxelShape voxelshape1 = Block.box(f2, extensionBottom, 0.0D, f3, extensionHeight, f3);
		VoxelShape voxelshape2 = Block.box(f2, extensionBottom, f2, f3, extensionHeight, 16.0D);
		VoxelShape voxelshape3 = Block.box(0.0D, extensionBottom, f2, f3, extensionHeight, f3);
		VoxelShape voxelshape4 = Block.box(f2, extensionBottom, f2, 16.0D, extensionHeight, f3);
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
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return this.shapes[this.getIndex(state)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return this.collisionShapes[this.getIndex(state)];
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter getter, BlockPos pos) {
		return this.renderShapes[this.getIndex(state)];
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return this.getShape(state, getter, pos, context);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
		return false;
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
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide) {
			ItemStack itemstack = player.getItemInHand(hand);
			return itemstack.getItem() == Items.LEAD ? InteractionResult.SUCCESS : InteractionResult.PASS;
		} else {
			return LeadItem.bindPlayerMobs(player, level, pos);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter getter = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = getter.getBlockState(blockpos);
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockState blockstate1 = getter.getBlockState(blockpos1);
		BlockState blockstate2 = getter.getBlockState(blockpos2);
		BlockState blockstate3 = getter.getBlockState(blockpos3);
		BlockState blockstate4 = getter.getBlockState(blockpos4);
		BlockState finalState = shouldSolidify(getter, blockpos, blockstate) ? this.getSolidifiedState() : this.defaultBlockState();
		return finalState.setValue(NORTH, this.canConnect(blockstate1, blockstate1.isFaceSturdy(getter, blockpos1, Direction.SOUTH), Direction.SOUTH))
				.setValue(EAST, this.canConnect(blockstate2, blockstate2.isFaceSturdy(getter, blockpos2, Direction.WEST), Direction.WEST))
				.setValue(SOUTH, this.canConnect(blockstate3, blockstate3.isFaceSturdy(getter, blockpos3, Direction.NORTH), Direction.NORTH))
				.setValue(WEST, this.canConnect(blockstate4, blockstate4.isFaceSturdy(getter, blockpos4, Direction.EAST), Direction.EAST))
				.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific face passed in.
	 */
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		}
		BlockState state = touchesLiquid(accessor, currentPos) ? this.getSolidifiedState().setValue(NORTH, stateIn.getValue(NORTH)).setValue(EAST, stateIn.getValue(EAST)).setValue(SOUTH, stateIn.getValue(SOUTH)).setValue(WEST, stateIn.getValue(WEST)).setValue(WATERLOGGED, stateIn.getValue(WATERLOGGED)) : stateIn;
		return facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL ?
				state.setValue(FACING_TO_PROPERTY_MAP.get(facing), this.canConnect(facingState, facingState.isFaceSturdy(accessor, facingPos, facing.getOpposite()), facing.getOpposite())) :
				super.updateShape(state, facing, facingState, accessor, currentPos, facingPos);
	}

	private BlockState getSolidifiedState() {
		String[] part = this.getRegistryName().getPath().split("_powder");
		String name = part[0] + part[1];
		return ForgeRegistries.BLOCKS.getValue(ResourceLocation.of(MODID + ":" + name, ':')).defaultBlockState();
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
	}

	@Override
	public void onLand(Level level, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		FluidState fluidstate = level.getFluidState(pos);
		BlockPos blockpos1 = pos.north();
		BlockPos blockpos2 = pos.east();
		BlockPos blockpos3 = pos.south();
		BlockPos blockpos4 = pos.west();
		BlockState blockstate1 = ((BlockGetter) level).getBlockState(blockpos1);
		BlockState blockstate2 = ((BlockGetter) level).getBlockState(blockpos2);
		BlockState blockstate3 = ((BlockGetter) level).getBlockState(blockpos3);
		BlockState blockstate4 = ((BlockGetter) level).getBlockState(blockpos4);
		BlockState finalState = shouldSolidify(level, pos, hitState) ? this.getSolidifiedState() : this.defaultBlockState();
		BlockState bs = finalState
				.setValue(NORTH, this.canConnect(blockstate1, blockstate1.isFaceSturdy(level, blockpos1, Direction.SOUTH), Direction.SOUTH))
				.setValue(EAST, this.canConnect(blockstate2, blockstate2.isFaceSturdy(level, blockpos2, Direction.WEST), Direction.WEST))
				.setValue(SOUTH, this.canConnect(blockstate3, blockstate3.isFaceSturdy(level, blockpos3, Direction.NORTH), Direction.NORTH))
				.setValue(WEST, this.canConnect(blockstate4, blockstate4.isFaceSturdy(level, blockpos4, Direction.EAST), Direction.EAST))
				.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);

		level.setBlockAndUpdate(pos, bs);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

}
