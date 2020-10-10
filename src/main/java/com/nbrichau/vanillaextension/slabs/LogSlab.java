package com.nbrichau.vanillaextension.slabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

public class LogSlab extends SlabBlock {

	public LogSlab(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		Block block = getBlockStripping(state.getBlock());
		if (block != null && player.getHeldItem(handIn).getToolTypes().contains(ToolType.AXE)) {
			worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if (!worldIn.isRemote) {
				worldIn.setBlockState(pos, block.getDefaultState().with(TYPE, state.get(TYPE)).with(WATERLOGGED, state.get(WATERLOGGED)), 11);
				if (player != null) {
					player.getHeldItem(handIn).damageItem(1, player, item -> item.sendBreakAnimation(handIn));
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	private static Block getBlockStripping(Block blockIn) {
		return ForgeRegistries.BLOCKS.getValue(ResourceLocation.create(blockIn.getRegistryName().getNamespace() + ":stripped_" + blockIn.getRegistryName().getPath(), ':'));
	}
}