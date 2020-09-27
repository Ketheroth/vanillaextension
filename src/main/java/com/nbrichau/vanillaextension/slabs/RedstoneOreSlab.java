package com.nbrichau.vanillaextension.slabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
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

public class RedstoneOreSlab extends SlabBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public RedstoneOreSlab(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.FALSE).with(LIT, Boolean.FALSE));
	}

	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		activate(state, worldIn, pos);
		super.onBlockClicked(state, worldIn, pos, player);
	}

	/**
	 * Called when the given entity walks on this Block
	 */
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		activate(worldIn.getBlockState(pos), worldIn, pos);
		super.onEntityWalk(worldIn, pos, entityIn);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote) {
			spawnParticles(worldIn, pos);
		} else {
			activate(state, worldIn, pos);
		}

		ItemStack itemstack = player.getHeldItem(handIn);
		return itemstack.getItem() instanceof BlockItem && (new BlockItemUseContext(player, handIn, itemstack, hit)).canPlace() ? ActionResultType.PASS : ActionResultType.SUCCESS;
	}

	private static void activate(BlockState state, World world, BlockPos pos) {
		spawnParticles(world, pos);
		if (!state.get(LIT)) {
			world.setBlockState(pos, state.with(LIT, Boolean.TRUE), 3);
		}

	}

	/**
	 * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
	 * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
	 */
	@Override
	public boolean ticksRandomly(BlockState state) {
		return state.get(LIT);
	}

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (state.get(LIT)) {
			worldIn.setBlockState(pos, state.with(LIT, Boolean.FALSE), 3);
		}

	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader world, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? 1 + RANDOM.nextInt(3) : 0;
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.get(LIT)) {
			spawnParticles(worldIn, pos);
		}

	}

	private static void spawnParticles(World world, BlockPos worldIn) {
		double d0 = 0.5625D;
		Random random = world.rand;

		for (Direction direction : Direction.values()) {
			BlockPos blockpos = worldIn.offset(direction);
			if (!world.getBlockState(blockpos).isOpaqueCube(world, blockpos)) {
				Direction.Axis direction$axis = direction.getAxis();
				double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getXOffset() : (double) random.nextFloat();
				double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getYOffset() : (double) random.nextFloat();
				double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getZOffset() : (double) random.nextFloat();
				world.addParticle(RedstoneParticleData.REDSTONE_DUST, (double) worldIn.getX() + d1, (double) worldIn.getY() + d2, (double) worldIn.getZ() + d3, 0.0D, 0.0D, 0.0D);
			}
		}

	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED, LIT);
	}
}
