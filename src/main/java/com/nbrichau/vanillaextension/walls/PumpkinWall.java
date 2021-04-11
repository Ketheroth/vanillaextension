package com.nbrichau.vanillaextension.walls;

import com.nbrichau.vanillaextension.init.WallInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class PumpkinWall extends WallBlock {
	public PumpkinWall(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		ItemStack itemstack = player.getItemInHand(handIn);
		if (itemstack.getItem() == Items.SHEARS) {
			if (!worldIn.isClientSide) {
				Direction direction = hit.getDirection();
				Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : direction;
				worldIn.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				worldIn.setBlock(pos, WallInit.carved_pumpkin_wall.get().defaultBlockState().setValue(UP, state.getValue(UP)).setValue(NORTH_WALL, state.getValue(NORTH_WALL)).setValue(EAST_WALL, state.getValue(EAST_WALL)).setValue(SOUTH_WALL, state.getValue(SOUTH_WALL)).setValue(WEST_WALL, state.getValue(WEST_WALL)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), 11);
				ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + 0.5D + (double) direction1.getStepX() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction1.getStepZ() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
				itementity.setDeltaMovement(0.05D * (double) direction1.getStepX() + worldIn.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction1.getStepZ() + worldIn.random.nextDouble() * 0.02D);
				worldIn.addFreshEntity(itementity);
				itemstack.hurtAndBreak(1, player, (p_220282_1_) -> {
					p_220282_1_.broadcastBreakEvent(handIn);
				});
			}
			return ActionResultType.sidedSuccess(worldIn.isClientSide);
		} else {
			return super.use(state, worldIn, pos, player, handIn, hit);
		}
	}
}
