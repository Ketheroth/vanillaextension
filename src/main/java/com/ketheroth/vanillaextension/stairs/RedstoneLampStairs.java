package com.ketheroth.vanillaextension.stairs;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class RedstoneLampStairs extends StairBlock {

	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public RedstoneLampStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
		this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.FALSE));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isClientSide) {
			boolean flag = state.getValue(LIT);
			if (flag != worldIn.hasNeighborSignal(pos)) {
				if (flag) {
					worldIn.getBlockTicks().scheduleTick(pos, this, 4);
				} else {
					worldIn.setBlock(pos, state.cycle(LIT), 2);
				}
			}

		}
	}

	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		if (state.getValue(LIT) && !worldIn.hasNeighborSignal(pos)) {
			worldIn.setBlock(pos, state.cycle(LIT), 2);
		}

	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT);
	}

}
