package com.nbrichau.vanillaextension.stairs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class LogStairs extends StairsBlock {

	public LogStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}


	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		Block block = getBlockStripping(state.getBlock());
		if (block != null && player.getHeldItem(handIn).getToolTypes().contains(ToolType.AXE)) {
			worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if (!worldIn.isRemote) {
				worldIn.setBlockState(pos, block.getDefaultState().with(FACING, state.get(FACING)).with(HALF, state.get(HALF)).with(SHAPE, state.get(SHAPE)).with(WATERLOGGED, state.get(WATERLOGGED)), 11);
				if (player != null) {
					player.getHeldItem(handIn).damageItem(1, player, item->item.sendBreakAnimation(handIn));
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	private static Block getBlockStripping(Block blockIn){
		System.out.println(blockIn.getRegistryName().getNamespace());
		System.out.println(blockIn.getRegistryName().getPath());
		System.out.println(ResourceLocation.create(blockIn.getRegistryName().getNamespace()+":stripped_"+blockIn.getRegistryName().getPath(),':'));
		return ForgeRegistries.BLOCKS.getValue(ResourceLocation.create(blockIn.getRegistryName().getNamespace()+":stripped_"+blockIn.getRegistryName().getPath(),':'));
	}
}
