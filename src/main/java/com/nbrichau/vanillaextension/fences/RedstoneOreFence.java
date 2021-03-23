package com.nbrichau.vanillaextension.fences;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class RedstoneOreFence extends FenceBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public RedstoneOreFence(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE).setValue(LIT, Boolean.FALSE));
	}

	@Override
	public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		activate(state, worldIn, pos);
		super.attack(state, worldIn, pos, player);
	}

	/**
	 * Called when the given entity walks on this Block
	 */
	@Override
	public void stepOn(World worldIn, BlockPos pos, Entity entityIn) {
		activate(worldIn.getBlockState(pos), worldIn, pos);
		super.stepOn(worldIn, pos, entityIn);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isClientSide) {
			spawnParticles(worldIn, pos);
		} else {
			activate(state, worldIn, pos);
		}

		if (player.getItemInHand(handIn).getItem() == Items.LEAD) {
			if (worldIn.isClientSide) {
				return ActionResultType.SUCCESS;
			} else {
				return LeadItem.bindPlayerMobs(player, worldIn, pos);
			}
		}
		ItemStack itemstack = player.getItemInHand(handIn);
		return itemstack.getItem() instanceof BlockItem && (new BlockItemUseContext(player, handIn, itemstack, hit)).canPlace() ? ActionResultType.PASS : ActionResultType.SUCCESS;
	}

	private static void activate(BlockState state, World world, BlockPos pos) {
		spawnParticles(world, pos);
		if (!state.getValue(LIT)) {
			world.setBlock(pos, state.setValue(LIT, Boolean.TRUE), 3);
		}

	}

	/**
	 * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
	 * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
	 */
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(LIT);
	}

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (state.getValue(LIT)) {
			worldIn.setBlock(pos, state.setValue(LIT, Boolean.FALSE), 3);
		}
	}


	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader world, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? 1 + RANDOM.nextInt(5) : 0;
	}

	/**
	 * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
	 * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
	 * of whether the block can receive random update ticks
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(LIT)) {
			spawnParticles(worldIn, pos);
		}
	}

	private static void spawnParticles(World world, BlockPos worldIn) {
		double d0 = 0.5625D;
		Random random = world.random;

		for (Direction direction : Direction.values()) {
			BlockPos blockpos = worldIn.relative(direction);
			if (!world.getBlockState(blockpos).isSolidRender(world, blockpos)) {
				Direction.Axis direction$axis = direction.getAxis();
				double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getStepX() : (double) random.nextFloat();
				double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getStepY() : (double) random.nextFloat();
				double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getStepZ() : (double) random.nextFloat();
				world.addParticle(RedstoneParticleData.REDSTONE, (double) worldIn.getX() + d1, (double) worldIn.getY() + d2, (double) worldIn.getZ() + d3, 0.0D, 0.0D, 0.0D);
			}
		}

	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED, LIT);
	}
}
