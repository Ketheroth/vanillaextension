package com.nbrichau.vanillaextension.walls;

import com.nbrichau.vanillaextension.init.WallInit;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolType;

public class CoarseDirtWall extends WallBlock {

	public CoarseDirtWall(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!worldIn.isClientSide()) {
			if (player.getItemInHand(handIn).getToolTypes().contains(ToolType.HOE)) {
				BlockState bs = WallInit.dirt_wall.get().defaultBlockState().setValue(UP, state.getValue(UP)).setValue(NORTH_WALL, state.getValue(NORTH_WALL)).setValue(EAST_WALL, state.getValue(EAST_WALL)).setValue(SOUTH_WALL, state.getValue(SOUTH_WALL)).setValue(WEST_WALL, state.getValue(WEST_WALL)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
				worldIn.setBlock(pos, bs, 11);
				worldIn.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				player.getItemInHand(handIn).hurtAndBreak(1, player, item -> item.broadcastBreakEvent(handIn));
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

}
