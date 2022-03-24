package com.ketheroth.vanillaextension.fences;

import com.ketheroth.vanillaextension.init.VEBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CoarseDirtFence extends FlattenableFence {

	public CoarseDirtFence(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
//		if (toolAction.equals(ToolActions.HOE_TILL)) { // not yet in forge
		if (Set.of(Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE).contains(stack.getItem())) {
			return VEBlocks.dirt_fence.get().defaultBlockState().setValue(NORTH, state.getValue(NORTH)).setValue(EAST, state.getValue(EAST)).setValue(SOUTH, state.getValue(SOUTH)).setValue(WEST, state.getValue(WEST)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
		}
		return super.getToolModifiedState(state, world, pos, player, stack, toolAction);
	}

}
