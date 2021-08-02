package com.ketheroth.vanillaextension.walls;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallHeight;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;
import java.util.Random;

public class RedstoneLampWall extends WallBlock {

	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	private final Map<BlockState, VoxelShape> stateToShapeMap;
	private final Map<BlockState, VoxelShape> stateToCollisionShapeMap;

	public RedstoneLampWall(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.FALSE));
		this.stateToShapeMap = this.makeShapes(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
		this.stateToCollisionShapeMap = this.makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
	}

	private Map<BlockState, VoxelShape> makeShapes(float p_235624_1_, float p_235624_2_, float p_235624_3_, float p_235624_4_, float p_235624_5_, float p_235624_6_) {
		float f = 8.0F - p_235624_1_;
		float f1 = 8.0F + p_235624_1_;
		float f2 = 8.0F - p_235624_2_;
		float f3 = 8.0F + p_235624_2_;
		VoxelShape voxelshape = Block.box((double) f, 0.0D, (double) f, (double) f1, (double) p_235624_3_, (double) f1);
		VoxelShape voxelshape1 = Block.box((double) f2, (double) p_235624_4_, 0.0D, (double) f3, (double) p_235624_5_, (double) f3);
		VoxelShape voxelshape2 = Block.box((double) f2, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_5_, 16.0D);
		VoxelShape voxelshape3 = Block.box(0.0D, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_5_, (double) f3);
		VoxelShape voxelshape4 = Block.box((double) f2, (double) p_235624_4_, (double) f2, 16.0D, (double) p_235624_5_, (double) f3);
		VoxelShape voxelshape5 = Block.box((double) f2, (double) p_235624_4_, 0.0D, (double) f3, (double) p_235624_6_, (double) f3);
		VoxelShape voxelshape6 = Block.box((double) f2, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_6_, 16.0D);
		VoxelShape voxelshape7 = Block.box(0.0D, (double) p_235624_4_, (double) f2, (double) f3, (double) p_235624_6_, (double) f3);
		VoxelShape voxelshape8 = Block.box((double) f2, (double) p_235624_4_, (double) f2, 16.0D, (double) p_235624_6_, (double) f3);
		ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

		for (Boolean obool : UP.getPossibleValues()) {
			for (WallHeight wallheight : EAST_WALL.getPossibleValues()) {
				for (WallHeight wallheight1 : NORTH_WALL.getPossibleValues()) {
					for (WallHeight wallheight2 : WEST_WALL.getPossibleValues()) {
						for (WallHeight wallheight3 : SOUTH_WALL.getPossibleValues()) {
							VoxelShape voxelshape9 = VoxelShapes.empty();
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight, voxelshape4, voxelshape8);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight2, voxelshape3, voxelshape7);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight1, voxelshape1, voxelshape5);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight3, voxelshape2, voxelshape6);
							if (obool) {
								voxelshape9 = VoxelShapes.or(voxelshape9, voxelshape);
							}

							BlockState blockstate = this.defaultBlockState().setValue(UP, obool).setValue(EAST_WALL, wallheight).setValue(WEST_WALL, wallheight2).setValue(NORTH_WALL, wallheight1).setValue(SOUTH_WALL, wallheight3);
							builder.put(blockstate.setValue(WATERLOGGED, Boolean.FALSE).setValue(LIT, Boolean.FALSE), voxelshape9);
							builder.put(blockstate.setValue(WATERLOGGED, Boolean.TRUE).setValue(LIT, Boolean.FALSE), voxelshape9);
							builder.put(blockstate.setValue(WATERLOGGED, Boolean.FALSE).setValue(LIT, Boolean.TRUE), voxelshape9);
							builder.put(blockstate.setValue(WATERLOGGED, Boolean.TRUE).setValue(LIT, Boolean.TRUE), voxelshape9);
						}
					}
				}
			}
		}

		return builder.build();
	}

	private static VoxelShape getHeightAlteredShape(VoxelShape baseShape, WallHeight height, VoxelShape lowShape, VoxelShape tallShape) {
		if (height == WallHeight.TALL) {
			return VoxelShapes.or(baseShape, tallShape);
		} else {
			return height == WallHeight.LOW ? VoxelShapes.or(baseShape, lowShape) : baseShape;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.stateToShapeMap.get(state);
	}

	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.stateToCollisionShapeMap.get(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isClientSide) {
			boolean flag = state.getValue(LIT);
			if (flag != worldIn.hasNeighborSignal(pos)) {
				if (flag) {
					worldIn.getBlockTicks().scheduleTick(pos, this, 4);
				} else {
					worldIn.setBlock(pos, state.cycle(LIT), 2);
				}
			}

		}
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (state.getValue(LIT) && !worldIn.hasNeighborSignal(pos)) {
			worldIn.setBlock(pos, state.cycle(LIT), 2);
		}

	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT);
	}


}
