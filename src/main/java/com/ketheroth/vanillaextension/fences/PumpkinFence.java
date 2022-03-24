package com.ketheroth.vanillaextension.fences;

import com.ketheroth.vanillaextension.init.VEBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PumpkinFence extends FenceBlock {

	public PumpkinFence(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		ItemStack itemstack = player.getItemInHand(handIn);
		if (itemstack.canPerformAction(ToolActions.SHEARS_CARVE)) {
			if (!worldIn.isClientSide) {
				Direction direction = hit.getDirection();
				Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : direction;
				worldIn.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
				worldIn.setBlock(pos, VEBlocks.carved_pumpkin_fence.get().defaultBlockState().setValue(NORTH, state.getValue(NORTH)).setValue(EAST, state.getValue(EAST)).setValue(SOUTH, state.getValue(SOUTH)).setValue(WEST, state.getValue(WEST)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), 11);
				ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + 0.5D + (double) direction1.getStepX() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction1.getStepZ() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
				itementity.setDeltaMovement(0.05D * (double) direction1.getStepX() + worldIn.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction1.getStepZ() + worldIn.random.nextDouble() * 0.02D);
				worldIn.addFreshEntity(itementity);
				itemstack.hurtAndBreak(1, player, (p_220282_1_) -> p_220282_1_.broadcastBreakEvent(handIn));
			}
			return InteractionResult.sidedSuccess(worldIn.isClientSide);
		} else {
			return super.use(state, worldIn, pos, player, handIn, hit);
		}
	}

}
