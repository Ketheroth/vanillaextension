package com.ketheroth.vanillaextension.walls;

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
public class CoarseDirtWall extends FlattenableWall {

	public CoarseDirtWall(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
//		if (toolAction.equals(ToolActions.HOE_TILL)) { // not yet in forge
		if (Set.of(Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE).contains(stack.getItem())) {
			return VEBlocks.dirt_wall.get().defaultBlockState().setValue(UP, state.getValue(UP)).setValue(NORTH_WALL, state.getValue(NORTH_WALL)).setValue(EAST_WALL, state.getValue(EAST_WALL)).setValue(SOUTH_WALL, state.getValue(SOUTH_WALL)).setValue(WEST_WALL, state.getValue(WEST_WALL)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
		}
		return super.getToolModifiedState(state, world, pos, player, stack, toolAction);
	}

}
