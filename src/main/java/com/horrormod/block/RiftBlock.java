package com.horrormod.block;

import com.horrormod.dimension.OtherworldTravel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;

// A thin fissure lying flush on the ground, two blocks long (this class is
// used for both the near and far half, which just differ in which texture
// their model references). FACING controls which way the pair's long axis
// runs, set by the Ritualistic Dagger at placement time. Walking over either
// half teleports the player between the Overworld and the Otherworld; the
// scheduled tick set when it's placed removes it after its lifespan.
public class RiftBlock extends HorizontalDirectionalBlock
{
    public RiftBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
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
