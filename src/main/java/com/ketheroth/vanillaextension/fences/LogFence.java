package com.ketheroth.vanillaextension.fences;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class LogFence extends FenceBlock {

	public LogFence(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
		if (toolAction.equals(ToolActions.AXE_STRIP)) {
			Block stripped = strippedOf(state.getBlock());
			return (stripped == null) ? null : stripped.defaultBlockState().setValue(NORTH, state.getValue(NORTH)).setValue(EAST, state.getValue(EAST)).setValue(SOUTH, state.getValue(SOUTH)).setValue(WEST, state.getValue(WEST)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
		}
		return super.getToolModifiedState(state, world, pos, player, stack, toolAction);
	}

	@Nullable
	private Block strippedOf(Block block) {
		return ForgeRegistries.BLOCKS.getValue(ResourceLocation.of(block.getRegistryName().getNamespace() + ":stripped_" + block.getRegistryName().getPath(), ':'));
	}

}
