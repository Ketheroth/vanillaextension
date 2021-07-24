package com.nbrichau.vanillaextension.stairs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nbrichau.vanillaextension.VanillaExtension.MODID;

public class ConcretePowderStairs extends FallingStairs implements SimpleWaterloggedBlock {

	public ConcretePowderStairs(Properties properties) {
		super(properties);
	}

	private static boolean shouldSolidify(BlockGetter reader, BlockPos pos, BlockState state) {
		return canSolidify(state) || touchesLiquid(reader, pos);
	}

	private static boolean touchesLiquid(BlockGetter reader, BlockPos pos) {
		boolean flag = false;
		BlockPos.MutableBlockPos blockpos$mutable = pos.mutable();
		for (Direction direction : Direction.values()) {
			BlockState blockstate = reader.getBlockState(blockpos$mutable);
			if (direction != Direction.DOWN || canSolidify(blockstate)) {
				blockpos$mutable.setWithOffset(pos, direction);
				blockstate = reader.getBlockState(blockpos$mutable);
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

	public void onLand(Level worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
		BlockState newState = shouldSolidify(worldIn, pos, hitState) ? this.getSolidifiedState().setValue(FACING, fallingState.getValue(FACING)).setValue(HALF, fallingState.getValue(HALF)) : fallingState;
		worldIn.setBlockAndUpdate(pos, newState.setValue(SHAPE, getShapeProperty(fallingState, worldIn, pos)).setValue(WATERLOGGED, worldIn.getFluidState(pos).getType() == Fluids.WATER));
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		BlockPos blockpos = context.getClickedPos();
		FluidState fluidstate = context.getLevel().getFluidState(blockpos);
		BlockGetter iblockreader = context.getLevel();
		BlockState defaultState = shouldSolidify(iblockreader, blockpos, iblockreader.getBlockState(blockpos)) ? this.getSolidifiedState() : this.defaultBlockState();
		BlockState blockstate = defaultState.setValue(FACING, context.getHorizontalDirection()).setValue(HALF, direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? Half.BOTTOM : Half.TOP).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		return blockstate.setValue(SHAPE, getShapeProperty(blockstate, context.getLevel(), blockpos));
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState blockstate = touchesLiquid(worldIn, currentPos) ? this.getSolidifiedState().setValue(FACING, stateIn.getValue(FACING)).setValue(HALF, stateIn.getValue(HALF)).setValue(SHAPE, stateIn.getValue(SHAPE)) : stateIn;
		return super.updateShape(blockstate, facing, facingState, worldIn, currentPos, facingPos);
	}

}
