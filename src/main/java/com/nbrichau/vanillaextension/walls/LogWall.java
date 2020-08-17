package com.nbrichau.vanillaextension.walls;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

public class LogWall extends WallBlock {

	public LogWall(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		Block block = getBlockStripping(state.getBlock());
		if (block != null && player.getHeldItem(handIn).getToolTypes().contains(ToolType.AXE)) {
			worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if (!worldIn.isRemote) {
				worldIn.setBlockState(pos, block.getDefaultState().with(UP, state.get(UP)).with(WALL_HEIGHT_NORTH, state.get(WALL_HEIGHT_NORTH)).with(WALL_HEIGHT_EAST, state.get(WALL_HEIGHT_EAST)).with(WALL_HEIGHT_SOUTH, state.get(WALL_HEIGHT_SOUTH)).with(WALL_HEIGHT_WEST, state.get(WALL_HEIGHT_WEST)).with(WATERLOGGED, state.get(WATERLOGGED)), 11);
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
