package com.nbrichau.vanillaextension.stairs;

import com.nbrichau.vanillaextension.init.StairsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolType;

import java.util.function.Supplier;

public class DirtStairs extends StairBlock {

	public DirtStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!worldIn.isClientSide()) {
			if (player.getItemInHand(handIn).getToolTypes().contains(ToolType.HOE)) {
				BlockState bs = StairsInit.farmland_stairs.get().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(HALF, state.getValue(HALF)).setValue(SHAPE, state.getValue(SHAPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
				worldIn.setBlockAndUpdate(pos, bs);
				worldIn.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				player.getItemInHand(handIn).hurtAndBreak(1, player, item -> item.broadcastBreakEvent(handIn));
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(state, worldIn, pos, player, handIn, hit);
	}

}
