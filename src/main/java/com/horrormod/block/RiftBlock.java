package com.horrormod.block;

import com.horrormod.dimension.OtherworldTravel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

// The two stacked blocks (top/bottom) that make up a Rift. Walking into
// either one teleports the player between the Overworld and the Otherworld;
// the scheduled tick set when it's placed removes it after its lifespan.
public class RiftBlock extends Block
{
    public RiftBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity)
    {
        if (!level.isClientSide && entity instanceof ServerPlayer player)
        {
            OtherworldTravel.teleportToOpposite(player);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        level.removeBlock(pos, false);
    }
}
