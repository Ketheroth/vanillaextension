package com.nbrichau.vanillaextension.trapdoors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

public class LogTrapdoor extends TrapDoorBlock {

	public LogTrapdoor(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		Block block = getBlockStripping(state.getBlock());
		if (block != null && player.getHeldItem(handIn).getToolTypes().contains(ToolType.AXE) && !player.isCrouching()) {
			worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if (!worldIn.isRemote) {
				worldIn.setBlockState(pos, block.getDefaultState().with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING)).with(OPEN, state.get(OPEN)).with(HALF, state.get(HALF)).with(POWERED, state.get(POWERED)).with(WATERLOGGED, state.get(WATERLOGGED)), 11);
				if (player != null) {
					player.getHeldItem(handIn).damageItem(1, player, item -> item.sendBreakAnimation(handIn));
				}
				return ActionResultType.SUCCESS;
			} else {
				return ActionResultType.PASS;
			}
		} else {
			state = state.func_235896_a_(OPEN);
			worldIn.setBlockState(pos, state, 2);
			if (state.get(WATERLOGGED)) {
				worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
			}
			this.playSound(player, worldIn, pos, state.get(OPEN));
			return ActionResultType.func_233537_a_(worldIn.isRemote);
		}
	}

	private static Block getBlockStripping(Block blockIn) {
		return ForgeRegistries.BLOCKS.getValue(ResourceLocation.create(blockIn.getRegistryName().getNamespace() + ":stripped_" + blockIn.getRegistryName().getPath(), ':'));
	}
}
