package com.ketheroth.vanillaextension.walls;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class LogWall extends WallBlock {

	public LogWall(Properties properties) {
		super(properties);
	}

	@Nullable
	private static Block getBlockStripping(Block blockIn) {
		return ForgeRegistries.BLOCKS.getValue(ResourceLocation.of(blockIn.getRegistryName().getNamespace() + ":stripped_" + blockIn.getRegistryName().getPath(), ':'));
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		Block block = getBlockStripping(state.getBlock());
		if (block != null && player.getItemInHand(handIn).getToolTypes().contains(ToolType.AXE)) {
			worldIn.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!worldIn.isClientSide) {
				worldIn.setBlock(pos, block.defaultBlockState().setValue(UP, state.getValue(UP)).setValue(NORTH_WALL, state.getValue(NORTH_WALL)).setValue(EAST_WALL, state.getValue(EAST_WALL)).setValue(SOUTH_WALL, state.getValue(SOUTH_WALL)).setValue(WEST_WALL, state.getValue(WEST_WALL)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)), 11);
				if (player != null) {
					player.getItemInHand(handIn).hurtAndBreak(1, player, item -> item.broadcastBreakEvent(handIn));
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

}
