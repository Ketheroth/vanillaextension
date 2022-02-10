package com.ketheroth.vanillaextension.walls;

import com.google.common.collect.ImmutableMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
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

	private static VoxelShape getHeightAlteredShape(VoxelShape baseShape, WallSide height, VoxelShape lowShape, VoxelShape tallShape) {
		if (height == WallSide.TALL) {
			return Shapes.or(baseShape, tallShape);
		} else {
			return height == WallSide.LOW ? Shapes.or(baseShape, lowShape) : baseShape;
		}
	}

	private Map<BlockState, VoxelShape> makeShapes(float p_235624_1_, float p_235624_2_, float p_235624_3_, float p_235624_4_, float p_235624_5_, float p_235624_6_) {
		float f = 8.0F - p_235624_1_;
		float f1 = 8.0F + p_235624_1_;
		float f2 = 8.0F - p_235624_2_;
		float f3 = 8.0F + p_235624_2_;
		VoxelShape voxelshape = Block.box(f, 0.0D, f, f1, p_235624_3_, f1);
		VoxelShape voxelshape1 = Block.box(f2, p_235624_4_, 0.0D, f3, p_235624_5_, f3);
		VoxelShape voxelshape2 = Block.box(f2, p_235624_4_, f2, f3, p_235624_5_, 16.0D);
		VoxelShape voxelshape3 = Block.box(0.0D, p_235624_4_, f2, f3, p_235624_5_, f3);
		VoxelShape voxelshape4 = Block.box(f2, p_235624_4_, f2, 16.0D, p_235624_5_, f3);
		VoxelShape voxelshape5 = Block.box(f2, p_235624_4_, 0.0D, f3, p_235624_6_, f3);
		VoxelShape voxelshape6 = Block.box(f2, p_235624_4_, f2, f3, p_235624_6_, 16.0D);
		VoxelShape voxelshape7 = Block.box(0.0D, p_235624_4_, f2, f3, p_235624_6_, f3);
		VoxelShape voxelshape8 = Block.box(f2, p_235624_4_, f2, 16.0D, p_235624_6_, f3);
		ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

		for (Boolean obool : UP.getPossibleValues()) {
			for (WallSide wallheight : EAST_WALL.getPossibleValues()) {
				for (WallSide wallheight1 : NORTH_WALL.getPossibleValues()) {
					for (WallSide wallheight2 : WEST_WALL.getPossibleValues()) {
						for (WallSide wallheight3 : SOUTH_WALL.getPossibleValues()) {
							VoxelShape voxelshape9 = Shapes.empty();
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight, voxelshape4, voxelshape8);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight2, voxelshape3, voxelshape7);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight1, voxelshape1, voxelshape5);
							voxelshape9 = getHeightAlteredShape(voxelshape9, wallheight3, voxelshape2, voxelshape6);
							if (obool) {
								voxelshape9 = Shapes.or(voxelshape9, voxelshape);
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

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return this.stateToShapeMap.get(state);
	}

	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return this.stateToCollisionShapeMap.get(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isClientSide) {
			boolean flag = state.getValue(LIT);
			if (flag != worldIn.hasNeighborSignal(pos)) {
				if (flag) {
					worldIn.scheduleTick(pos, this, 4);
				} else {
					worldIn.setBlock(pos, state.cycle(LIT), 2);
				}
			}

		}
	}

	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		if (state.getValue(LIT) && !worldIn.hasNeighborSignal(pos)) {
			worldIn.setBlock(pos, state.cycle(LIT), 2);
		}

	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT);
	}

}
