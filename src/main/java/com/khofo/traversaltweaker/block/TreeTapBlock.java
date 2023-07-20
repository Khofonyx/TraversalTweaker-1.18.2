package com.khofo.traversaltweaker.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;


public class TreeTapBlock extends Block {
    private static final Map<Direction, VoxelShape> DIRECTIONALSHAPE;

    public static final IntegerProperty SAP_LEVEL = IntegerProperty.create("sap_level", 0, 1);
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

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }


    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);

        /*
        int sapLevel = state.getValue(SAP_LEVEL);
        if (sapLevel < 3)
        {
            if (sapLevel + 1 == 3)
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1f, 1f);
            this.setSapLevel(level, pos, state, sapLevel + 1, true);
        }
         */
    }

    static {
        DIRECTIONALSHAPE = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(5.5, 5.0, 12.0, 10.5, 11.5, 16.0),
                Direction.SOUTH, Block.box(5.5, 5.0, 0.0, 10.5, 11.5, 5.0),
                Direction.WEST, Block.box(11.0, 5.0, 5.5, 16.0, 11.5, 10.5),
                Direction.EAST, Block.box(0.0, 5.0, 5.5, 5.0, 11.5, 10.5)));
        }

}
