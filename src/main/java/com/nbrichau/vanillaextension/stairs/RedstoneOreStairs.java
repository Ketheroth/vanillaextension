package com.nbrichau.vanillaextension.stairs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;
import java.util.function.Supplier;

public class RedstoneOreStairs extends StairBlock {

	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public RedstoneOreStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(LIT, Boolean.FALSE));
	}

	private static void activate(BlockState state, Level world, BlockPos pos) {
		spawnParticles(world, pos);
		if (!state.getValue(LIT)) {
			world.setBlock(pos, state.setValue(LIT, Boolean.TRUE), 3);
		}

	}

	private static void spawnParticles(Level world, BlockPos worldIn) {
		double d0 = 0.5625D;
		Random random = world.random;

		for (Direction direction : Direction.values()) {
			BlockPos blockpos = worldIn.relative(direction);
			if (!world.getBlockState(blockpos).isSolidRender(world, blockpos)) {
				Direction.Axis direction$axis = direction.getAxis();
				double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getStepX() : (double) random.nextFloat();
				double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getStepY() : (double) random.nextFloat();
				double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getStepZ() : (double) random.nextFloat();
				world.addParticle(DustParticleOptions.REDSTONE, (double) worldIn.getX() + d1, (double) worldIn.getY() + d2, (double) worldIn.getZ() + d3, 0.0D, 0.0D, 0.0D);
			}
		}

	}

	@Override
	public void attack(BlockState state, Level worldIn, BlockPos pos, Player player) {
		activate(state, worldIn, pos);
		super.attack(state, worldIn, pos, player);
	}

	/**
	 * Called when the given entity walks on this Block
	 */
	@Override
	public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
		activate(worldIn.getBlockState(pos), worldIn, pos);
		super.stepOn(worldIn, pos, state, entityIn);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (worldIn.isClientSide) {
			spawnParticles(worldIn, pos);
		} else {
			activate(state, worldIn, pos);
		}

		ItemStack itemstack = player.getItemInHand(handIn);
		return itemstack.getItem() instanceof BlockItem && (new BlockPlaceContext(player, handIn, itemstack, hit)).canPlace() ? InteractionResult.PASS : InteractionResult.SUCCESS;
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(LIT);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		if (state.getValue(LIT)) {
			worldIn.setBlock(pos, state.setValue(LIT, Boolean.FALSE), 3);
		}

	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader world, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? 1 + RANDOM.nextInt(5) : 0;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(LIT)) {
			spawnParticles(worldIn, pos);
		}

	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, SHAPE, WATERLOGGED, LIT);
	}

}
