package com.ketheroth.vanillaextension.trapdoors;

import com.ketheroth.vanillaextension.init.VEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;

public class FlattenableTrapdoor extends TrapDoorBlock {

	public FlattenableTrapdoor(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
		if (toolAction.equals(ToolActions.SHOVEL_FLATTEN)) {
			return VEBlocks.dirt_path_trapdoor.get().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(OPEN, state.getValue(OPEN)).setValue(HALF, state.getValue(HALF)).setValue(POWERED, state.getValue(POWERED)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
		}
		return super.getToolModifiedState(state, world, pos, player, stack, toolAction);
	}

}
