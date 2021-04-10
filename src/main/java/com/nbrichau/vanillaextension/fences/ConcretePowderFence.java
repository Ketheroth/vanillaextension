package com.nbrichau.vanillaextension.fences;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

import static com.nbrichau.vanillaextension.VanillaExtension.MODID;

public class ConcretePowderFence extends FallingBlock implements IWaterLoggable {
	public static final BooleanProperty NORTH = SixWayBlock.NORTH;
	public static final BooleanProperty EAST = SixWayBlock.EAST;
	public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
	public static final BooleanProperty WEST = SixWayBlock.WEST;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((p_199775_0_) -> {
		return p_199775_0_.getKey().getAxis().isHorizontal();
	}).collect(Util.toMap());
	protected final VoxelShape[] collisionShapes;
	protected final VoxelShape[] shapes;
	private final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap<>();
	private final VoxelShape[] renderShapes;

//	private final BlockState solidifiedState;

	public ConcretePowderFence(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
		this.renderShapes = this.makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
		this.collisionShapes = this.makeShapes(2.0F, 2.0F, 24.0F, 0.0F, 24.0F);
		this.shapes = this.makeShapes(2.0F, 2.0F, 16.0F, 0.0F, 16.0F);

		for (BlockState blockstate : this.stateDefinition.getPossibleStates()) {
			this.getIndex(blockstate);
		}
//		this.solidifiedState = ;
	}

	protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth, float nodeHeight, float extensionBottom, float extensionHeight) {
		float f = 8.0F - nodeWidth;
		float f1 = 8.0F + nodeWidth;
		float f2 = 8.0F - extensionWidth;
		float f3 = 8.0F + extensionWidth;
		VoxelShape voxelshape = Block.box((double) f, 0.0D, (double) f, (double) f1, (double) nodeHeight, (double) f1);
		VoxelShape voxelshape1 = Block.box((double) f2, (double) extensionBottom, 0.0D, (double) f3, (double) extensionHeight, (double) f3);
		VoxelShape voxelshape2 = Block.box((double) f2, (double) extensionBottom, (double) f2, (double) f3, (double) extensionHeight, 16.0D);
		VoxelShape voxelshape3 = Block.box(0.0D, (double) extensionBottom, (double) f2, (double) f3, (double) extensionHeight, (double) f3);
		VoxelShape voxelshape4 = Block.box((double) f2, (double) extensionBottom, (double) f2, 16.0D, (double) extensionHeight, (double) f3);
		VoxelShape voxelshape5 = VoxelShapes.or(voxelshape1, voxelshape4);
		VoxelShape voxelshape6 = VoxelShapes.or(voxelshape2, voxelshape3);
		VoxelShape[] avoxelshape = new VoxelShape[]{VoxelShapes.empty(), voxelshape2, voxelshape3, voxelshape6, voxelshape1, VoxelShapes.or(voxelshape2, voxelshape1), VoxelShapes.or(voxelshape3, voxelshape1), VoxelShapes.or(voxelshape6, voxelshape1), voxelshape4, VoxelShapes.or(voxelshape2, voxelshape4), VoxelShapes.or(voxelshape3, voxelshape4), VoxelShapes.or(voxelshape6, voxelshape4), voxelshape5, VoxelShapes.or(voxelshape2, voxelshape5), VoxelShapes.or(voxelshape3, voxelshape5), VoxelShapes.or(voxelshape6, voxelshape5)};

		for (int i = 0; i < 16; ++i) {
			avoxelshape[i] = VoxelShapes.or(voxelshape, avoxelshape[i]);
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

	private static int getMask(Direction facing) {
		return 1 << facing.get2DDataValue();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.shapes[this.getIndex(state)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.collisionShapes[this.getIndex(state)];
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return this.renderShapes[this.getIndex(state)];
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
		return this.getShape(state, reader, pos, context);
	}

	@Override
	public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	public boolean canConnect(BlockState state, boolean isSideSolid, Direction direction) {
		Block block = state.getBlock();
		boolean flag = this.isSameFence(block);
		boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction);
		return !isExceptionForConnection(block) && isSideSolid || flag || flag1;
	}

	private boolean isSameFence(Block block) {
		return block.is(BlockTags.FENCES) && block.is(BlockTags.WOODEN_FENCES) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isClientSide) {
			ItemStack itemstack = player.getItemInHand(handIn);
			return itemstack.getItem() == Items.LEAD ? ActionResultType.SUCCESS : ActionResultType.PASS;
		} else {
			return LeadItem.bindPlayerMobs(player, worldIn, pos);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IBlockReader iblockreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = iblockreader.getBlockState(blockpos);
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockState blockstate1 = iblockreader.getBlockState(blockpos1);
		BlockState blockstate2 = iblockreader.getBlockState(blockpos2);
		BlockState blockstate3 = iblockreader.getBlockState(blockpos3);
		BlockState blockstate4 = iblockreader.getBlockState(blockpos4);
		BlockState finalState = shouldSolidify(iblockreader, blockpos, blockstate) ? this.getSolidifiedState() : this.defaultBlockState();
		return finalState.setValue(NORTH, this.canConnect(blockstate1, blockstate1.isFaceSturdy(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH))
				.setValue(EAST, this.canConnect(blockstate2, blockstate2.isFaceSturdy(iblockreader, blockpos2, Direction.WEST), Direction.WEST))
				.setValue(SOUTH, this.canConnect(blockstate3, blockstate3.isFaceSturdy(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH))
				.setValue(WEST, this.canConnect(blockstate4, blockstate4.isFaceSturdy(iblockreader, blockpos4, Direction.EAST), Direction.EAST))
				.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
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
		BlockState state = isTouchingLiquid(worldIn, currentPos) ? this.getSolidifiedState().setValue(NORTH, stateIn.getValue(NORTH)).setValue(EAST, stateIn.getValue(EAST)).setValue(SOUTH, stateIn.getValue(SOUTH)).setValue(WEST, stateIn.getValue(WEST)).setValue(WATERLOGGED, stateIn.getValue(WATERLOGGED)) : stateIn;
		return facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL ?
				state.setValue(FACING_TO_PROPERTY_MAP.get(facing), this.canConnect(facingState, facingState.isFaceSturdy(worldIn, facingPos, facing.getOpposite()), facing.getOpposite())) :
				super.updateShape(state, facing, facingState, worldIn, currentPos, facingPos);
	}

	private BlockState getSolidifiedState() {
		String[] part = this.getRegistryName().getPath().split("_powder");
		String name = part[0] + part[1];
		return ForgeRegistries.BLOCKS.getValue(ResourceLocation.of(MODID + ":" + name, ':')).defaultBlockState();
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
	}

	@Override
	public void onLand(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		FluidState fluidstate = worldIn.getFluidState(pos);
		BlockPos blockpos1 = pos.north();
		BlockPos blockpos2 = pos.east();
		BlockPos blockpos3 = pos.south();
		BlockPos blockpos4 = pos.west();
		BlockState blockstate1 = ((IBlockReader) worldIn).getBlockState(blockpos1);
		BlockState blockstate2 = ((IBlockReader) worldIn).getBlockState(blockpos2);
		BlockState blockstate3 = ((IBlockReader) worldIn).getBlockState(blockpos3);
		BlockState blockstate4 = ((IBlockReader) worldIn).getBlockState(blockpos4);
		BlockState finalState = shouldSolidify(worldIn, pos, hitState) ? this.getSolidifiedState() : this.defaultBlockState();
		BlockState bs = finalState
				.setValue(NORTH, this.canConnect(blockstate1, blockstate1.isFaceSturdy(worldIn, blockpos1, Direction.SOUTH), Direction.SOUTH))
				.setValue(EAST, this.canConnect(blockstate2, blockstate2.isFaceSturdy(worldIn, blockpos2, Direction.WEST), Direction.WEST))
				.setValue(SOUTH, this.canConnect(blockstate3, blockstate3.isFaceSturdy(worldIn, blockpos3, Direction.NORTH), Direction.NORTH))
				.setValue(WEST, this.canConnect(blockstate4, blockstate4.isFaceSturdy(worldIn, blockpos4, Direction.EAST), Direction.EAST))
				.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);

		worldIn.setBlockAndUpdate(pos, bs);
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

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
}
