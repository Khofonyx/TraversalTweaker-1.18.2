package com.khofo.traversaltweaker.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


public class TreeTapBlock extends Block {
    private static final Map<Direction, VoxelShape> DIRECTIONALSHAPE;


    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getShape(pState);
    }

    public TreeTapBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos.relative((pState.getValue(FACING)).getOpposite()));
        return blockstate.is(BlockTags.JUNGLE_LOGS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockstate = this.defaultBlockState();
        LevelReader levelreader = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        Direction[] var5 = pContext.getNearestLookingDirections();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Direction direction = var5[var7];
            if (direction.getAxis().isHorizontal()) {
                blockstate = (BlockState)blockstate.setValue(FACING, direction);
                if (blockstate.canSurvive(levelreader, blockpos)) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    public static VoxelShape getShape(BlockState pState) {
        return (VoxelShape)DIRECTIONALSHAPE.get(pState.getValue(FACING));
    }


    static {
        DIRECTIONALSHAPE = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(5.5, 5.0, 12.0, 10.5, 11.5, 16.0),
                Direction.SOUTH, Block.box(5.5, 5.0, 0.0, 10.5, 11.5, 5.0),
                Direction.WEST, Block.box(11.0, 5.0, 5.5, 16.0, 11.5, 10.5),
                Direction.EAST, Block.box(0.0, 5.0, 5.5, 5.0, 11.5, 10.5)));
        }

}
