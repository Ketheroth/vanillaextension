package com.ketheroth.vanillaextension.stairs;

import com.ketheroth.vanillaextension.init.StairsInit;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CoarseDirtStairs extends FlattenableStairs {

	public CoarseDirtStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}

	@Nullable
	@Override
	public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
//		if (toolAction.equals(ToolActions.HOE_TILL)) { // not yet in forge
		if (stack.is(Tag.fromSet(Set.of(Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE)))) {
			return StairsInit.dirt_stairs.get().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(HALF, state.getValue(HALF)).setValue(SHAPE, state.getValue(SHAPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
		}
		return super.getToolModifiedState(state, world, pos, player, stack, toolAction);
	}

}
