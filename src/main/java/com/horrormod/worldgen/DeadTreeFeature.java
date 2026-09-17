package com.horrormod.worldgen;

import com.horrormod.block.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

// A bare dead trunk: mostly standing (stacked vertically), occasionally fallen on its side.
// No leaves, branches, or saplings -- see CONTEXT.md "Dead Log".
public class DeadTreeFeature extends Feature<NoneFeatureConfiguration>
{
    private static final float FALLEN_CHANCE = 0.25F;

    public DeadTreeFeature(Codec<NoneFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        BlockPos groundPos = origin.below();
        if (!level.getBlockState(groundPos).isFaceSturdy(level, groundPos, Direction.UP))
        {
            return false;
        }

        int length = 3 + random.nextInt(3);

        if (random.nextFloat() < FALLEN_CHANCE)
        {
            Direction axisDirection = random.nextBoolean() ? Direction.EAST : Direction.SOUTH;
            BlockState logState = ModBlocks.DEAD_LOG.get().defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, axisDirection.getAxis());

            for (int i = 0; i < length; i++)
            {
                BlockPos pos = origin.relative(axisDirection, i);
                BlockPos below = pos.below();
                if (!level.getBlockState(pos).isAir() || !level.getBlockState(below).isFaceSturdy(level, below, Direction.UP))
                {
                    return i > 0;
                }
                level.setBlock(pos, logState, 2);
            }
            return true;
        }
        else
        {
            BlockState logState = ModBlocks.DEAD_LOG.get().defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);

            for (int i = 0; i < length; i++)
            {
                BlockPos pos = origin.above(i);
                if (!level.getBlockState(pos).isAir())
                {
                    return i > 0;
                }
                level.setBlock(pos, logState, 2);
            }
            return true;
        }
    }
}
