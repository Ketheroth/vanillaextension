package com.nbrichau.vanillaextension.trapdoors;

import com.nbrichau.vanillaextension.init.TrapdoorInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class CoarseDirtTrapdoor extends TrapDoorBlock {
	public CoarseDirtTrapdoor(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote()) {
			ItemStack its = player.getHeldItem(handIn);
			if (its.isItemEqualIgnoreDurability(Items.WOODEN_HOE.getDefaultInstance()) ||
					its.isItemEqualIgnoreDurability(Items.STONE_HOE.getDefaultInstance()) ||
					its.isItemEqualIgnoreDurability(Items.IRON_HOE.getDefaultInstance()) ||
					its.isItemEqualIgnoreDurability(Items.GOLDEN_HOE.getDefaultInstance()) ||
					its.isItemEqualIgnoreDurability(Items.DIAMOND_HOE.getDefaultInstance())) {
				BlockState bs = TrapdoorInit.dirt_trapdoor.getDefaultState().with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING)).with(OPEN, state.get(OPEN)).with(HALF, state.get(HALF)).with(POWERED, state.get(POWERED)).with(WATERLOGGED, state.get(WATERLOGGED));
				worldIn.setBlockState(pos, bs, 11);
				worldIn.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				player.getHeldItem(handIn).damageItem(1, player, item -> item.sendBreakAnimation(handIn));
				return ActionResultType.SUCCESS;
			} else {
				return ActionResultType.PASS;
			}
		} else {
			return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		}
	}
}