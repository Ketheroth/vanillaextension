package com.nbrichau.vanillaextension.slabs;

import com.nbrichau.vanillaextension.init.SlabInit;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolType;

public class DirtSlab extends SlabBlock {

	public DirtSlab(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!worldIn.isClientSide()) {
			if (player.getItemInHand(handIn).getToolTypes().contains(ToolType.HOE)) {
				BlockState bs = SlabInit.farmland_slab.get().defaultBlockState().setValue(TYPE, state.getValue(TYPE)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
				worldIn.setBlockAndUpdate(pos, bs);
				worldIn.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				player.getItemInHand(handIn).hurtAndBreak(1, player, item -> item.broadcastBreakEvent(handIn));
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(state, worldIn, pos, player, handIn, hit);
	}

}
