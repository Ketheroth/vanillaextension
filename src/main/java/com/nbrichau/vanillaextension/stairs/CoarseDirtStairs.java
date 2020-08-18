package com.nbrichau.vanillaextension.stairs;

import com.nbrichau.vanillaextension.init.BlockInit;
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

public class CoarseDirtStairs extends StairsBlock {
	public CoarseDirtStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote()) {
			if (player.getHeldItem(handIn).getToolTypes().contains(ToolType.HOE)) {
				BlockState bs = BlockInit.dirt_stairs.getDefaultState().with(FACING, state.get(FACING)).with(HALF, state.get(HALF)).with(SHAPE, state.get(SHAPE)).with(WATERLOGGED, state.get(WATERLOGGED));
				worldIn.setBlockState(pos, bs, 11);
				worldIn.playSound(null,pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F,1.0F);
				player.getHeldItem(handIn).damageItem(1, player, item->item.sendBreakAnimation(handIn));
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}
