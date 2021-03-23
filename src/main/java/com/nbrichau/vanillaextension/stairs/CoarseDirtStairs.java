package com.nbrichau.vanillaextension.stairs;

import com.nbrichau.vanillaextension.init.StairsInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock.Properties;

public class CoarseDirtStairs extends StairsBlock {
	public CoarseDirtStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isClientSide()) {
			if (player.getItemInHand(handIn).getToolTypes().contains(ToolType.HOE)) {
				BlockState bs = StairsInit.dirt_stairs.defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(HALF, state.getValue(HALF)).setValue(SHAPE, state.getValue(SHAPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
				worldIn.setBlock(pos, bs, 11);
				worldIn.playSound(null,pos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F,1.0F);
				player.getItemInHand(handIn).hurtAndBreak(1, player, item->item.broadcastBreakEvent(handIn));
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}
