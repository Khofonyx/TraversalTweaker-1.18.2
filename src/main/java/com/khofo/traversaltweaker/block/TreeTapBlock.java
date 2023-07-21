package com.khofo.traversaltweaker.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public class TreeTapBlock extends Block {
    private static final Map<Direction, VoxelShape> DIRECTIONALSHAPE;
    public static final IntegerProperty SAP_LEVEL = BlockStateProperties.AGE_1;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getShape(pState);
    }
    public TreeTapBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(SAP_LEVEL, 0));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, SAP_LEVEL});
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
        return (Integer)pState.getValue(SAP_LEVEL) < 1;
    }
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        int i = (Integer)pState.getValue(SAP_LEVEL);
        if (i < 1 && ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pLevel.random.nextInt(5) == 0)) {
            pLevel.setBlock(pPos, (BlockState)pState.setValue(SAP_LEVEL, i + 1), 1);
            ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
        }
    }

    static {
        DIRECTIONALSHAPE = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(5.5, 5.0, 12.0, 10.5, 11.5, 16.0),
                Direction.SOUTH, Block.box(5.5, 5.0, 0.0, 10.5, 11.5, 5.0),
                Direction.WEST, Block.box(11.0, 5.0, 5.5, 16.0, 11.5, 10.5),
                Direction.EAST, Block.box(0.0, 5.0, 5.5, 5.0, 11.5, 10.5)));
        }

}
