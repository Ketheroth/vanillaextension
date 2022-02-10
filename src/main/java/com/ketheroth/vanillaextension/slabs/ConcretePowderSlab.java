package com.ketheroth.vanillaextension.slabs;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.ketheroth.vanillaextension.VanillaExtension.MODID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConcretePowderSlab extends FallingBlock implements SimpleWaterloggedBlock {

	public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape BOTTOM_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape TOP_SHAPE = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public ConcretePowderSlab(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, Boolean.FALSE));
	}

	private static boolean shouldSolidify(BlockGetter reader, BlockPos pos, BlockState state) {
		return canSolidify(state) || touchesLiquid(reader, pos);
	}

	private static boolean touchesLiquid(BlockGetter reader, BlockPos pos) {
		boolean flag = false;
		BlockPos.MutableBlockPos mutable = pos.mutable();
		for (Direction direction : Direction.values()) {
			BlockState blockstate = reader.getBlockState(mutable);
			if (direction != Direction.DOWN || canSolidify(blockstate)) {
				mutable.setWithOffset(pos, direction);
				blockstate = reader.getBlockState(mutable);
				if (canSolidify(blockstate) && !blockstate.isFaceSturdy(reader, pos, direction.getOpposite())) {
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

	private BlockState getSolidifiedState() {
		String[] part = this.getRegistryName().getPath().split("_powder");
		String name = part[0] + part[1];
		return ForgeRegistries.BLOCKS.getValue(ResourceLocation.of(MODID + ":" + name, ':')).defaultBlockState();
	}

	@Override
	public void onLand(Level worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		if (shouldSolidify(worldIn, pos, hitState)) {
			worldIn.setBlock(pos, this.getSolidifiedState().setValue(TYPE, fallingState.getValue(TYPE)).setValue(WATERLOGGED, fallingState.getValue(WATERLOGGED)), 3);
		}
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		return touchesLiquid(worldIn, currentPos) ?
				this.getSolidifiedState().setValue(TYPE, stateIn.getValue(TYPE)).setValue(WATERLOGGED, Boolean.FALSE) :
				super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		SlabType slabtype = state.getValue(TYPE);
		return switch (slabtype) {
			case DOUBLE -> Shapes.block();
			case TOP -> TOP_SHAPE;
			default -> BOTTOM_SHAPE;
		};
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter iblockreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = iblockreader.getBlockState(blockpos);

		if (blockstate.is(this)) {
			return blockstate.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, Boolean.FALSE);
		} else {
			FluidState fluidstate = context.getLevel().getFluidState(blockpos);
			BlockState blockstate1 = this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
			BlockState blockstate2 = this.getSolidifiedState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
			Direction direction = context.getClickedFace();

			if (shouldSolidify(iblockreader, blockpos, blockstate)) {
				return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? blockstate2 : blockstate2.setValue(TYPE, SlabType.TOP);
			} else {
				return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.setValue(TYPE, SlabType.TOP);
			}
		}
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		ItemStack itemstack = useContext.getItemInHand();
		SlabType slabtype = state.getValue(TYPE);
		if (slabtype != SlabType.DOUBLE && itemstack.getItem() == this.asItem()) {
			if (useContext.replacingClickedOnBlock()) {
				boolean flag = useContext.getClickLocation().y - (double) useContext.getClickedPos().getY() > 0.5D;
				Direction direction = useContext.getClickedFace();
				if (slabtype == SlabType.BOTTOM) {
					return direction == Direction.UP || flag && direction.getAxis().isHorizontal();
				} else {
					return direction == Direction.DOWN || !flag && direction.getAxis().isHorizontal();
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		return state.getValue(TYPE) != SlabType.DOUBLE && SimpleWaterloggedBlock.super.placeLiquid(worldIn, pos, state, fluidStateIn);
	}

	@Override
	public boolean canPlaceLiquid(BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return state.getValue(TYPE) != SlabType.DOUBLE && SimpleWaterloggedBlock.super.canPlaceLiquid(worldIn, pos, state, fluidIn);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		return type == PathComputationType.WATER && worldIn.getFluidState(pos).is(FluidTags.WATER);
	}

}
